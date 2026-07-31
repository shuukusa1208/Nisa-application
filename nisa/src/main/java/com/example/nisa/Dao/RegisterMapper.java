package com.example.nisa.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.nisa.Entity.User;

/**
 * 新規登録用のDAO。
 * Spring Data JPAを使用してデータベース操作を行う。
 * 
 * 	実際にDBとやり取りする部分。save()やcountByEmail()を呼ぶだけで、裏でSQLが自動生成される(JPAの機能)
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
