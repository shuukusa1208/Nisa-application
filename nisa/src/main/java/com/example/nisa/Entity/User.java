package com.example.nisa.Entity;

import java.time.LocalDateTime;

/**
 * usersテーブルに対応するエンティティ。
 * MyBatisはこのクラスのフィールド名とSQLのカラム名(またはエイリアス)を
 * マッピングして自動で詰め替えてくれる。
 */
public class User {

    private Long id;
    private String email;
    private String passwordHash;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User() {
    }

    // --- getter / setter ---
    // MyBatisはgetter/setter経由でフィールドに値をセットするため必須

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

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
