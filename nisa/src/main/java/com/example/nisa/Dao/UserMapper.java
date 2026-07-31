package com.example.nisa.Dao;

import com.example.nisa.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * ログイン用のDAO。
 * Spring Data JPAを使用してデータベース操作を行う。
 */
@Repository
public interface UserMapper extends JpaRepository<User, Long> {

    /**
     * メールアドレスでユーザーを検索する。
     * @param email メールアドレス
     * @return ユーザー情報（見つからない場合は空のOptional）
     */
    Optional<User> findByEmail(String email);
}
