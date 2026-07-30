package com.example.nisa.Form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 新規登録画面(signup.html)から送られてくる入力値を受け取るクラス。
 *
 * Entity(User)との違い:
 *   - passwordは平文のまま保持する(ハッシュ化はUserServiceの役目)
 *   - バリデーション(入力チェック)のアノテーションをここに集約する
 *
 * pom.xmlに spring-boot-starter-validation の追加が必要:
 *   <dependency>
 *     <groupId>org.springframework.boot</groupId>
 *     <artifactId>spring-boot-starter-validation</artifactId>
 *   </dependency>
 */
public class SignupForm {

    @NotBlank(message = "お名前を入力してください")
    private String name;

    @NotBlank(message = "メールアドレスを入力してください")
    @Email(message = "メールアドレスの形式が正しくありません")
    private String email;

    @NotBlank(message = "パスワードを入力してください")
    @Size(min = 8, message = "パスワードは8文字以上で入力してください")
    private String password;

    public SignupForm() {
    }

    // --- getter / setter ---
    // Spring MVCがフォームの値をここ経由で詰め替える(th:field と対応させる場合に使用)

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}