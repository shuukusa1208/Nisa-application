package com.example.nisa.Controller;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nisa.Dao.PasswordResetTokenMapper;
import com.example.nisa.Dao.UserMapper;
import com.example.nisa.Entity.PasswordResetToken;
import com.example.nisa.Entity.User;
import com.example.nisa.Form.PasswordResetForm;
import com.example.nisa.Form.PasswordResetRequestForm;

/**
 * パスワード再設定の画面と処理を担当するController。
 *
 * 本番では、発行した resetUrl をメール送信サービスから利用者へ送る。
 * 現在はメール送信機能が未導入のため、開発確認用として申請画面にURLを表示する。
 */
@Controller
public class PasswordResetController {

    private static final int TOKEN_VALID_MINUTES = 30;

    private final UserMapper userMapper;
    private final PasswordResetTokenMapper tokenMapper;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetController(UserMapper userMapper,
                                   PasswordResetTokenMapper tokenMapper,
                                   PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.tokenMapper = tokenMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /** ログイン画面のリンクから再設定申請フォームを表示する。 */
    @GetMapping("/password-reset")
    public String requestForm(Model model) {
        model.addAttribute("requestForm", new PasswordResetRequestForm());
        return "password-reset";
    }

    /**
     * メールアドレスからユーザーを探し、期限付きトークンを発行する。
     * アカウントの有無に関係なく同じ案内文を表示し、メールアドレスの存在を漏らしにくくする。
     */
    @PostMapping("/password-reset")
    @Transactional
    public String createResetToken(@ModelAttribute PasswordResetRequestForm form, Model model) {
        model.addAttribute("requestForm", form);
        String message = "登録されている場合は、パスワード再設定の案内を確認してください。";
        model.addAttribute("message", message);

        User user = userMapper.findByEmail(form.getEmail()).orElse(null);
        if (user == null) {
            return "password-reset";
        }

        // 同じユーザーの古いトークンを上書きし、常に最新のURLだけを有効にする。
        PasswordResetToken resetToken = tokenMapper.findByUserEmail(form.getEmail())
                .orElseGet(PasswordResetToken::new);
        resetToken.setToken(UUID.randomUUID().toString());
        resetToken.setUser(user);
        resetToken.setExpiresAt(LocalDateTime.now().plusMinutes(TOKEN_VALID_MINUTES));
        resetToken.setUsed(false);
        tokenMapper.save(resetToken);

        // メール送信導入後は、このURLをメール本文へ渡す。現状は開発用に画面表示する。
        model.addAttribute("resetUrl", "/password-reset/new?token=" + resetToken.getToken());
        return "password-reset";
    }

    /** トークンが有効な場合だけ、新しいパスワード入力画面を表示する。 */
    @GetMapping("/password-reset/new")
    public String resetForm(@RequestParam String token, Model model) {
        if (!isValid(token)) {
            model.addAttribute("errorMessage", "再設定URLが無効または期限切れです。もう一度申請してください。");
            return "password-reset";
        }
        PasswordResetForm resetForm = new PasswordResetForm();
        resetForm.setToken(token);
        model.addAttribute("resetForm", resetForm);
        return "password-reset-new";
    }

    /** 新しいパスワードを検証し、BCryptハッシュを users.password_hash に保存する。 */
    @PostMapping("/password-reset/new")
    @Transactional
    public String resetPassword(@ModelAttribute PasswordResetForm form,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (!isValid(form.getToken())) {
            model.addAttribute("errorMessage", "再設定URLが無効または期限切れです。もう一度申請してください。");
            return "password-reset";
        }
        if (form.getNewPassword() == null || form.getNewPassword().length() < 8) {
            model.addAttribute("errorMessage", "パスワードは8文字以上で入力してください。");
            model.addAttribute("resetForm", form);
            return "password-reset-new";
        }
        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
            model.addAttribute("errorMessage", "新しいパスワードが一致しません。");
            model.addAttribute("resetForm", form);
            return "password-reset-new";
        }

        PasswordResetToken resetToken = tokenMapper.findByToken(form.getToken()).orElseThrow();
        User user = resetToken.getUser();
        user.setPasswordHash(passwordEncoder.encode(form.getNewPassword()));
        userMapper.save(user);

        // 成功後は同じURLを再利用できないようにする。
        resetToken.setUsed(true);
        tokenMapper.save(resetToken);
        redirectAttributes.addFlashAttribute("resetCompleted", true);
        return "redirect:/login";
    }

    private boolean isValid(String token) {
        return token != null
                && tokenMapper.findByToken(token)
                    .filter(saved -> !saved.isUsed())
                    .filter(saved -> saved.getExpiresAt().isAfter(LocalDateTime.now()))
                    .isPresent();
    }
}
