package com.example.nisa.Service;

/**
 * 新規登録処理のインターフェース。
 * Controllerはこのinterfaceの型を通じて呼び出すことで、
 * 実装(SignupServiceImpl)の詳細を意識せずに済む。
 */
public interface SignupService {

    /**
     * 新規ユーザーを登録する。
     * @return true: 登録成功 / false: メールアドレスが既に使われている
     */
    boolean register(String email, String rawPassword, String name);
}