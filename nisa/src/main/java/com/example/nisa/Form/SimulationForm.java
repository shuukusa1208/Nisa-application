package com.example.nisa.Form;

public class SimulationForm {

    private long initialInvestment = 0;
    private long monthlyContribution = 40000;
    private int years = 20;
    private double annualReturnRate = 5.0;
    private String frame = "つみたて投資枠";

    public long getInitialInvestment() {
        return initialInvestment;
    }

    public void setInitialInvestment(long initialInvestment) {
        this.initialInvestment = initialInvestment;
    }

    public long getMonthlyContribution() {
        return monthlyContribution;
    }

    public void setMonthlyContribution(long monthlyContribution) {
        this.monthlyContribution = monthlyContribution;
    }

    public int getYears() {
        return years;
    }

    public void setYears(int years) {
        this.years = years;
    }

    public double getAnnualReturnRate() {
        return annualReturnRate;
    }

    public void setAnnualReturnRate(double annualReturnRate) {
        this.annualReturnRate = annualReturnRate;
    }

    public String getFrame() {
        return frame;
    }

    public void setFrame(String frame) {
        this.frame = frame;
    }
}