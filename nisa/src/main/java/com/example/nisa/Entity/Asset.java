package com.example.nisa.Entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "assets")
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 100)
    private String code;

    @Column(nullable = false)
    private long quantity;

    @Column(nullable = false)
    private long acquisition;

    @Column(name = "current_value", nullable = false)
    private long currentValue;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @Column(nullable = false, length = 50)
    private String frame;

    @Column(length = 1000)
    private String memo;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public long getQuantity() { return quantity; }
    public void setQuantity(long quantity) { this.quantity = quantity; }
    public long getAcquisition() { return acquisition; }
    public void setAcquisition(long acquisition) { this.acquisition = acquisition; }
    public long getCurrentValue() { return currentValue; }
    public void setCurrentValue(long currentValue) { this.currentValue = currentValue; }
    public LocalDate getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }
    public String getFrame() { return frame; }
    public void setFrame(String frame) { this.frame = frame; }
    public String getMemo() { return memo; }
    public void setMemo(String memo) { this.memo = memo; }
}
