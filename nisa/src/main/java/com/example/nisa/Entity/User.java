package com.example.nisa.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * usersテーブルに対応するJPAエンティティ。
 * //Spring Data JPA：Spring Bootの専門ツールの一つ。データベース（MySQLやPostgreSQLなど）とのやり取りだけを楽にするための専用ツール。
 *
 * @Entity      : このクラスがDBのテーブルと対応することを示す
 * @Table       : 対応するテーブル名を指定(省略するとクラス名から自動推測される)
 * @Id          : 主キーを示す
 * @GeneratedValue : AUTO_INCREMENTのように、DB側で自動採番される値であることを示す
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQLのAUTO_INCREMENTに対応
    private Long id;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    // DBの列名(password_hash)とJavaのフィールド名(passwordHash)が違うため、
    // @Column(name = "...") で明示的に対応付ける
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    // created_at, updated_at はDB側の DEFAULT CURRENT_TIMESTAMP に任せるため、
    // insertable = false, updatable = false にして、JPAからは書き込ませない
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    public User() {
    }

    // --- getter / setter ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}