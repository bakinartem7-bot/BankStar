package com.starbank.recommendations.model;

import java.util.UUID;

public class UserAggregates {
    private UUID userId;
    private boolean hasDebit;
    private boolean hasInvest;
    private boolean hasCredit;
    private long sumDebitDeposits;      // сумма пополнений по DEBIT
    private long sumDebitWithdrawals;   // сумма трат по DEBIT
    private long sumSavingDeposits;     // сумма пополнений по SAVING

    public UserAggregates() {}

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public boolean isHasDebit() { return hasDebit; }
    public void setHasDebit(boolean hasDebit) { this.hasDebit = hasDebit; }

    public boolean isHasInvest() { return hasInvest; }
    public void setHasInvest(boolean hasInvest) { this.hasInvest = hasInvest; }

    public boolean isHasCredit() { return hasCredit; }
    public void setHasCredit(boolean hasCredit) { this.hasCredit = hasCredit; }

    public long getSumDebitDeposits() { return sumDebitDeposits; }
    public void setSumDebitDeposits(long sumDebitDeposits) { this.sumDebitDeposits = sumDebitDeposits; }

    public long getSumDebitWithdrawals() { return sumDebitWithdrawals; }
    public void setSumDebitWithdrawals(long sumDebitWithdrawals) { this.sumDebitWithdrawals = sumDebitWithdrawals; }

    public long getSumSavingDeposits() { return sumSavingDeposits; }
    public void setSumSavingDeposits(long sumSavingDeposits) { this.sumSavingDeposits = sumSavingDeposits; }
}
