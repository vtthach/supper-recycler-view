package com.tonicartos.superslimexample.transaction;

import com.google.gson.annotations.SerializedName;

public class AccMgtTransactionHistoryInfo {
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
}
