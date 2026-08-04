package com.example.nisa.Form;

public class AssetForm {

    private String name;
    private String code;
    private long quantity;
    private long acquisition;
    private long currentValue;
    private String purchaseDate;
    private String frame;
    private String memo;

    public AssetForm() {
        this.frame = "つみたて投資枠";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public long getAcquisition() {
        return acquisition;
    }

    public void setAcquisition(long acquisition) {
        this.acquisition = acquisition;
    }

    public long getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(long currentValue) {
        this.currentValue = currentValue;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getFrame() {
        return frame;
    }

    public void setFrame(String frame) {
        this.frame = frame;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}