package com.example.train_scheduling_and_booking_system.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class FinanceReportResponse {

    private BigDecimal totalRevenue;
    private BigDecimal todayRevenue;
    private long totalTransactions;
    private long successfulTransactions;
    private BigDecimal totalRefunds;
    private List<Map<String, Object>> recentTransactions;

    public FinanceReportResponse() {}

    public FinanceReportResponse(BigDecimal totalRevenue, BigDecimal todayRevenue, long totalTransactions,
                                 long successfulTransactions, BigDecimal totalRefunds,
                                 List<Map<String, Object>> recentTransactions) {
        this.totalRevenue = totalRevenue;
        this.todayRevenue = todayRevenue;
        this.totalTransactions = totalTransactions;
        this.successfulTransactions = successfulTransactions;
        this.totalRefunds = totalRefunds;
        this.recentTransactions = recentTransactions;
    }

    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }

    public BigDecimal getTodayRevenue() { return todayRevenue; }
    public void setTodayRevenue(BigDecimal todayRevenue) { this.todayRevenue = todayRevenue; }

    public long getTotalTransactions() { return totalTransactions; }
    public void setTotalTransactions(long totalTransactions) { this.totalTransactions = totalTransactions; }

    public long getSuccessfulTransactions() { return successfulTransactions; }
    public void setSuccessfulTransactions(long successfulTransactions) { this.successfulTransactions = successfulTransactions; }

    public BigDecimal getTotalRefunds() { return totalRefunds; }
    public void setTotalRefunds(BigDecimal totalRefunds) { this.totalRefunds = totalRefunds; }

    public List<Map<String, Object>> getRecentTransactions() { return recentTransactions; }
    public void setRecentTransactions(List<Map<String, Object>> recentTransactions) { this.recentTransactions = recentTransactions; }
}
