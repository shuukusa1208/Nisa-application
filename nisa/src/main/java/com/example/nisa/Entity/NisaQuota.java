package com.example.nisa.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "nisa_quotas")
public class NisaQuota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "tsumitate_used", nullable = false)
    private long tsumitateUsed;

    @Column(name = "growth_used", nullable = false)
    private long growthUsed;

    public Long getId() { return id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public long getTsumitateUsed() { return tsumitateUsed; }
    public void setTsumitateUsed(long tsumitateUsed) { this.tsumitateUsed = tsumitateUsed; }
    public long getGrowthUsed() { return growthUsed; }
    public void setGrowthUsed(long growthUsed) { this.growthUsed = growthUsed; }
}
