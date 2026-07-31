//ログインするためのユーザー情報をDBから習得するためのクラス

package com.example.nisa.Serviceimp;

import com.example.nisa.Entity.User;
import com.example.nisa.Dao.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Spring Securityが認証時に自動で呼び出すクラス。
 * loadUserByUsername() の中で「メールアドレス→DB検索→UserDetailsに変換」を行う。
 *
 * ここで例外を投げる(ユーザーが見つからない)と、Securityが自動で
 * ログイン失敗(/login?error)として扱ってくれる。
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    // コンストラクタインジェクション。Spring BootがUserMapperを自動で渡してくれる。
    public UserDetailsServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userMapper.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: " + email));

        // Spring Securityが理解できる形(UserDetails)に変換して返す
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPasswordHash(),   // BCryptハッシュ済みの値。平文と自動で比較される
                Collections.emptyList()   // ロール/権限。今回は未使用のため空リスト
        );
    }
}