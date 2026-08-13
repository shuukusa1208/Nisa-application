package com.example.nisa.Controller;

import com.example.nisa.Dao.UserMapper;
import com.example.nisa.Entity.User;
import com.example.nisa.Form.AssetForm;
import com.example.nisa.Form.SimulationForm;
import com.example.nisa.Service.AssetService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Controller
public class AppController {

    private final UserMapper userMapper;
    private final AssetService assetService;
    private final PasswordEncoder passwordEncoder;

    public AppController(UserMapper userMapper, AssetService assetService, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.assetService = assetService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping({"/", "/home"})
    public String home(@AuthenticationPrincipal UserDetails principal, Model model) {
        addUserInfo(principal, model);
        model.addAttribute("today", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy年M月d日")));
        addAssetSummary(principal, model);
        return "home";
    }

    @GetMapping("/assets")
    public String assets(@AuthenticationPrincipal UserDetails principal, Model model) {
        addUserInfo(principal, model);
        addAssetSummary(principal, model);
        model.addAttribute("assets", assetService.listAssets(principal.getUsername()));
        return "assets";
    }

    @GetMapping("/asset-edit")
    public String assetEdit(@AuthenticationPrincipal UserDetails principal, Model model) {
        addUserInfo(principal, model);
        model.addAttribute("assetForm", new AssetForm());
        return "asset-edit";
    }

    @PostMapping("/asset-edit")
    public String saveAsset(@AuthenticationPrincipal UserDetails principal,
                            @ModelAttribute AssetForm assetForm,
                            RedirectAttributes redirectAttributes) {
        assetService.saveAsset(principal.getUsername(), assetForm);
        redirectAttributes.addFlashAttribute("assetSaved", true);
        return "redirect:/assets";
    }

    @GetMapping("/simulation")
    public String simulation(@AuthenticationPrincipal UserDetails principal, Model model) {
        addUserInfo(principal, model);
        addAssetSummary(principal, model);
        model.addAttribute("simulationForm", new SimulationForm());
        addSimulationResult(new SimulationForm(), model);
        return "simulation";
    }

    @PostMapping("/simulation")
    public String runSimulation(@AuthenticationPrincipal UserDetails principal,
                                @ModelAttribute SimulationForm simulationForm,
                                Model model) {
        addUserInfo(principal, model);
        addSimulationResult(simulationForm, model);
        model.addAttribute("simulationForm", simulationForm);
        return "simulation";
    }

    @GetMapping("/result")
    public String result(@AuthenticationPrincipal UserDetails principal,
                         @RequestParam(defaultValue = "0") long initialInvestment,
                         @RequestParam(defaultValue = "40000") long monthlyContribution,
                         @RequestParam(defaultValue = "20") int years,
                         @RequestParam(defaultValue = "5.0") double annualReturnRate,
                         @RequestParam(defaultValue = "つみたて投資枠") String frame,
                         Model model) {
        addUserInfo(principal, model);
        SimulationForm simulationForm = new SimulationForm();
        simulationForm.setInitialInvestment(initialInvestment);
        simulationForm.setMonthlyContribution(monthlyContribution);
        simulationForm.setYears(years);
        simulationForm.setAnnualReturnRate(annualReturnRate);
        simulationForm.setFrame(frame);
        addSimulationResult(simulationForm, model);
        model.addAttribute("simulationForm", simulationForm);
        return "result";
    }

    @PostMapping("/result")
    public String showDetailedResult(@AuthenticationPrincipal UserDetails principal,
                                     @ModelAttribute SimulationForm simulationForm,
                                     Model model) {
        addUserInfo(principal, model);
        addSimulationResult(simulationForm, model);
        model.addAttribute("simulationForm", simulationForm);
        return "result";
    }

    @GetMapping("/mypage")
    public String mypage(@AuthenticationPrincipal UserDetails principal, Model model) {
        addUserInfo(principal, model);
        addAssetSummary(principal, model);
        Optional<User> userOptional = userMapper.findByEmail(principal.getUsername());
        userOptional.ifPresent(user -> {
            model.addAttribute("registeredAt", user.getCreatedAt() != null ?
                    user.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy年M月d日")) : "-");
        });
        return "mypage";
    }

    @GetMapping("/password-change")
    public String passwordChange(@AuthenticationPrincipal UserDetails principal, Model model) {
        addUserInfo(principal, model);
        return "password-change";
    }

    @PostMapping("/password-change")
    public String updatePassword(@AuthenticationPrincipal UserDetails principal,
                                 @RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        addUserInfo(principal, model);

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("errorMessage", "新しいパスワードが一致しません。" );
            return "password-change";
        }

        Optional<User> userOptional = userMapper.findByEmail(principal.getUsername());
        if (userOptional.isEmpty()) {
            model.addAttribute("errorMessage", "ユーザー情報が見つかりませんでした。" );
            return "password-change";
        }

        User user = userOptional.get();
        if (!passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
            model.addAttribute("errorMessage", "現在のパスワードが正しくありません。" );
            return "password-change";
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userMapper.save(user);
        redirectAttributes.addFlashAttribute("successMessage", "パスワードを変更しました。" );
        return "redirect:/mypage";
    }

    private void addUserInfo(UserDetails principal, Model model) {
        if (principal == null) {
            return;
        }
        model.addAttribute("userName", principal.getUsername());
        model.addAttribute("userEmail", principal.getUsername());
        // ユーザー名をDBから取れるときは name を使用したい
        userMapper.findByEmail(principal.getUsername())
                .ifPresent(user -> model.addAttribute("userName", user.getName()));
    }

    private void addAssetSummary(UserDetails principal, Model model) {
        if (principal == null) {
            return;
        }
        List<AssetForm> assets = assetService.listAssets(principal.getUsername());
        long totalValue = assets.stream().mapToLong(AssetForm::getCurrentValue).sum();
        long totalInvestment = assets.stream().mapToLong(AssetForm::getAcquisition).sum();
        long profit = totalValue - totalInvestment;
        double profitRate = totalInvestment != 0 ? (double) profit / totalInvestment * 100.0 : 0.0;
        long yearContribution = calculateCurrentYearContribution(assets);
        long monthlyPace = yearContribution / 12;

        long tsum = assets.stream()
                .filter(asset -> "つみたて投資枠".equals(asset.getFrame()))
                .mapToLong(AssetForm::getAcquisition)
                .sum();
        long gsum = assets.stream()
                .filter(asset -> "成長投資枠".equals(asset.getFrame()))
                .mapToLong(AssetForm::getAcquisition)
                .sum();
        int tsumPercent = (int) Math.min(100, tsum * 100 / 1_200_000);
        int gsumPercent = (int) Math.min(100, gsum * 100 / 2_400_000);
        String accountType = determineAccountType(assets);

        model.addAttribute("assetTotalValue", String.format("¥%,d", totalValue));
        model.addAttribute("assetTotalInvestment", String.format("¥%,d", totalInvestment));
        model.addAttribute("assetProfit", String.format("%s¥%,d", profit >= 0 ? "+" : "", profit));
        model.addAttribute("assetProfitRate", String.format("%+.2f%%", profitRate));
        model.addAttribute("assetDailyChange", "+¥0 (+0.00%)");
        model.addAttribute("assetYearlyContribution", String.format("¥%,d", yearContribution));
        model.addAttribute("monthlyPaceLabel", String.format("月%sペース", formatMonthlyPace(monthlyPace)));
        model.addAttribute("tSum", String.format("¥%,d", tsum));
        model.addAttribute("gSum", String.format("¥%,d", gsum));
        model.addAttribute("tPercent", tsumPercent);
        model.addAttribute("gPercent", gsumPercent);
        model.addAttribute("accountType", accountType);
    }

    private String determineAccountType(List<AssetForm> assets) {
        boolean hasTsumitate = assets.stream().anyMatch(asset -> "つみたて投資枠".equals(asset.getFrame()));
        boolean hasGrowth = assets.stream().anyMatch(asset -> "成長投資枠".equals(asset.getFrame()));
        if (hasTsumitate && hasGrowth) {
            return "成長投資枠 + つみたて投資枠";
        }
        if (hasTsumitate) {
            return "つみたて投資枠";
        }
        if (hasGrowth) {
            return "成長投資枠";
        }
        return "-";
    }

    private long calculateCurrentYearContribution(List<AssetForm> assets) {
        int currentYear = LocalDate.now().getYear();
        return assets.stream()
                .filter(asset -> asset.getPurchaseDate() != null)
                .filter(asset -> {
                    try {
                        LocalDate purchaseDate = LocalDate.parse(asset.getPurchaseDate());
                        return purchaseDate.getYear() == currentYear;
                    } catch (DateTimeParseException e) {
                        return false;
                    }
                })
                .mapToLong(AssetForm::getAcquisition)
                .sum();
    }

    private String formatMonthlyPace(long monthlyPace) {
        if (monthlyPace % 10000 == 0) {
            return String.format("%d万円", monthlyPace / 10000);
        }
        return String.format("%d円", monthlyPace);
    }

    private void addSimulationResult(SimulationForm form, Model model) {
        double monthlyRate = form.getAnnualReturnRate() / 100.0 / 12.0;
        int months = form.getYears() * 12;
        double futureValue = form.getInitialInvestment() * Math.pow(1 + monthlyRate, months);
        if (monthlyRate > 0) {
            futureValue += form.getMonthlyContribution() * (Math.pow(1 + monthlyRate, months) - 1) / monthlyRate;
        } else {
            futureValue += form.getMonthlyContribution() * months;
        }
        long resultTotal = Math.round(futureValue);
        long principalTotal = form.getInitialInvestment() + form.getMonthlyContribution() * (long) months;
        long profit = resultTotal - principalTotal;

        model.addAttribute("simulatedTotal", String.format("¥%,d", resultTotal));
        model.addAttribute("simulatedPrincipal", String.format("¥%,d", principalTotal));
        model.addAttribute("simulatedProfit", String.format("%s¥%,d", profit >= 0 ? "+" : "", profit));
        addSimulationBreakdown(form, model);
    }

    private void addSimulationBreakdown(SimulationForm form, Model model) {
        double monthlyRate = form.getAnnualReturnRate() / 100.0 / 12.0;
        int[] targetYears = new int[]{5, 10, 15, 20};
        for (int year : targetYears) {
            int months = year * 12;
            double value = form.getInitialInvestment() * Math.pow(1 + monthlyRate, months);
            if (monthlyRate > 0) {
                value += form.getMonthlyContribution() * (Math.pow(1 + monthlyRate, months) - 1) / monthlyRate;
            } else {
                value += form.getMonthlyContribution() * months;
            }
            long principal = form.getInitialInvestment() + form.getMonthlyContribution() * (long) months;
            model.addAttribute("year" + year + "Principal", String.format("¥%,d", principal));
            model.addAttribute("year" + year + "Value", String.format("¥%,d", Math.round(value)));
        }
    }
}
