package com.example.nisa.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    /**
     * ログイン画面の表示。
     * GET /login
     *
     * 実際の認証処理(POST /login)は、Controllerではなく
     * SecurityConfigのformLoginConfig側が自動で処理する。
     * このControllerは「画面を表示するだけ」の役割。
     */
    @GetMapping("/login")
    public String login() {
        return "login"; // src/main/resources/templates/login.html
    }
}