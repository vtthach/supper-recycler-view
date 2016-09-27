package com.tonicartos.superslimexample.transaction;

import com.google.gson.annotations.SerializedName;
import com.tonicartos.superslimexample.recycler.StickyUtils;
import com.tonicartos.superslimexample.recycler.StickyItemImpl;

public class AccMgtTransactionHistoryInfo extends StickyItemImpl<Long, String> {

    @SerializedName("AvailableBalance")
    public double availableBalance;
    @SerializedName("CurrentBalance")
    public double currentBalance;
    @SerializedName("TransactionAmountCredit")
    public double transactionAmountCredit;
    @SerializedName("TransactionAmountDebit")
    public double transactionAmountDebit;
    @SerializedName("TransactionDateTime")
    public long transactionDateTime;
    @SerializedName("TransactionDescriptionOne")
    public String transactionDescriptionOne;
    @SerializedName("TransactionDescriptionTwo")
    public String transactionDescriptionTwo;
    @SerializedName("TransactionId")
    public String transactionId;
    @SerializedName("TransactionType")
    public String transactionType;
    @SerializedName("ServiceFee")
    public double serviceFee;

    public AccMgtTransactionHistoryInfo() {
        // Empty constructor for serialize
    }

    @Override
    public Long getGroupId() {
        if (groupId == null) {
            groupId = StickyUtils.getBeginOfDay(transactionDateTime);
        }
        return groupId;
    }

    @Override
    public String getGroupDisplay() {
        if (groupDisplay == null) {
            groupDisplay = StickyUtils.getFormatDateLong(getGroupId());
        }
        return groupDisplay;
    }
}
