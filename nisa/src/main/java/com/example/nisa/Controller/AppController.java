package com.example.nisa.Controller;

import com.example.nisa.Dao.UserMapper;
import com.example.nisa.Entity.User;
import com.example.nisa.Form.AssetForm;
import com.example.nisa.Form.SimulationForm;
import com.example.nisa.Service.AssetService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
public class AppController {

    private final UserMapper userMapper;
    private final AssetService assetService;

    public AppController(UserMapper userMapper, AssetService assetService) {
        this.userMapper = userMapper;
        this.assetService = assetService;
    }

    @GetMapping({"/", "/home"})
    public String home(@AuthenticationPrincipal UserDetails principal, Model model) {
        addUserInfo(principal, model);
        model.addAttribute("today", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy年M月d日")));
        return "home";
    }

    @GetMapping("/assets")
    public String assets(@AuthenticationPrincipal UserDetails principal, Model model) {
        addUserInfo(principal, model);
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

    @GetMapping("/mypage")
    public String mypage(@AuthenticationPrincipal UserDetails principal, Model model) {
        addUserInfo(principal, model);
        Optional<User> userOptional = userMapper.findByEmail(principal.getUsername());
        userOptional.ifPresent(user -> {
            model.addAttribute("registeredAt", user.getCreatedAt() != null ?
                    user.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy年M月d日")) : "-");
        });
        return "mypage";
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
    }
}
