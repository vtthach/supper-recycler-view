package com.tonicartos.superslimexample.transaction;

import com.google.gson.annotations.SerializedName;
import com.tonicartos.superslimexample.recycler.StickyItemImpl;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class AccMgtTransactionHistoryInfo extends StickyItemImpl {

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

    // Use to display into UI
    private String transactionDateDisplay;
    private long groupId;

    public AccMgtTransactionHistoryInfo(int type, int sectionManagerType, int sectionFirstPosition) {
        super(type, sectionManagerType, sectionFirstPosition);
    }

    public AccMgtTransactionHistoryInfo() {
        // Empty constructor for serialize
        super(0, 0, 0);
    }

    @Override
    public long getGroupId() {
        if (groupId == 0) {
            groupId = getBeginOfDay(transactionDateTime);
        }
        return groupId;
    }

    public long getBeginOfDay(long dateTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateTime);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public void generateGroupDisplay() {
        transactionDateDisplay = getFormatDate(getGroupId());
    }

    public String getFormatDate(long groupId) {
        return DateFormat.getDateInstance(DateFormat.LONG)
                .format(new Date(groupId));
    }

    public String getTransactionDateDisplay() {
        return transactionDateDisplay;
    }
}
