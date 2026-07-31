package com.example.nisa.Serviceimp;

import com.example.nisa.Entity.User;
import com.example.nisa.Dao.RegisterMapper;
import com.example.nisa.Service.SignupService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * SignupService の実装クラス。
 * DAOは RegisterMapper のみを使用する(ログイン用のDAOには依存しない)。
 */
@Service
public class SignupServiceImpl implements SignupService {

    private final RegisterMapper registerMapper;
    private final PasswordEncoder passwordEncoder;

    public SignupServiceImpl(RegisterMapper registerMapper, PasswordEncoder passwordEncoder) {
        this.registerMapper = registerMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean register(String email, String rawPassword, String name) {
        if (registerMapper.countByEmail(email) > 0) {
            return false; // 重複登録防止
        }

        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(rawPassword)); // 平文→BCryptハッシュに変換
        user.setName(name);

        registerMapper.save(user);
        return true;
    }
}