//新規登録画面の表示と、登録処理を行うcontroller

package com.example.nisa.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.nisa.Form.SignupForm;
import com.example.nisa.Service.SignupService;

import jakarta.validation.Valid;


//Serviceを使うためコンストラクタで宣言
@Controller
public class RegisterController {

    private final SignupService signupService;

    public RegisterController(SignupService signupService) {
        this.signupService = signupService;
    }

    /**
     * 新規登録画面の表示。
     * GET /signup
     *
     * "signupForm" という名前でModelに空のFormを渡しておくことで、
     * signup.html側で th:object="${signupForm}" のように参照できる。
     */
    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("signupForm", new SignupForm());
        return "signup";
    }

    /**
     * 新規登録フォームの送信処理。
     * POST /signup
     *
     * @Valid を付けると、SignupFormに書いたアノテーション(@NotBlank等)を
     * 自動でチェックしてくれる。チェック結果は BindingResult に入る。
     *
     * 処理の流れ:
     *   1. 入力チェック(@Valid)でエラーがあれば、signup.htmlに戻してエラーメッセージを表示
     *   2. 問題なければ UserService.register() を呼ぶ
     *   3. メール重複などで失敗したら、エラーメッセージ付きで signup.html に戻す
     *   4. 成功したら /login にリダイレクト
     */
    @PostMapping("/signup")
    public String signup(
            @Valid @ModelAttribute("signupForm") SignupForm form,
            BindingResult bindingResult,
            Model model
    ) {
        // ① 入力チェックでエラーがあれば、ここで画面に戻す
        if (bindingResult.hasErrors()) {
            return "signup";
        }

        // ② ビジネスロジック(重複チェック・BCryptハッシュ化・DB登録)はSignupServiceに任せる
        boolean success = signupService.register(form.getEmail(), form.getPassword(), form.getName());

        if (!success) {
            model.addAttribute("errorMessage", "このメールアドレスは既に登録されています。");
            return "signup";
        }

        // ③ 登録成功 → ログイン画面へ
        return "redirect:/login?registered";
    }
}