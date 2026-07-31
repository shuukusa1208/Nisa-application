package com.example.nisa.Dao;

import com.example.nisa.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 新規登録用のDAO。
 * Spring Data JPAを使用してデータベース操作を行う。
 */
@Repository
public interface RegisterMapper extends JpaRepository<User, Long> {

    /**
     * メールアドレスでユーザーを検索する。
     * @param email メールアドレス
     * @return ユーザーが見つかった場合の件数。0以上の値を返す。
     */
    long countByEmail(String email);

    /**
     * ユーザーを登録(挿入)する。
     * JpaRepository の save() メソッドを使用するため、ここでは明示的に定義しない。
     */
}
