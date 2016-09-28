package com.tonicartos.superslimexample.transaction;

import com.tonicartos.superslimexample.recycler.StickyItemGenerator;
import com.tonicartos.superslimexample.recycler.StickyUtils;

public class TransactionHistoryItemGenerator extends StickyItemGenerator<AccMgtTransactionHistoryInfo, Long, String> {
    static final String STICKY_DATE_FORMAT = "d MMMM yyyy";

    public TransactionHistoryItemGenerator(Class<AccMgtTransactionHistoryInfo> clazz) {
        super(clazz);
    }

    @Override
    public int compare(AccMgtTransactionHistoryInfo o1, AccMgtTransactionHistoryInfo o2) {
        return o2.getGroupId() != null
                ? o2.getGroupId().compareTo(o1.getGroupId())
                : o1.getGroupId() == null ? 0 : -1;
    }

    @Override
    public String getGroupDisplay(Long groupId) {
        return StickyUtils.getFormatDate(groupId, STICKY_DATE_FORMAT);
    }
}
