package com.tonicartos.superslimexample.recycler;

import android.support.annotation.NonNull;

import com.tonicartos.superslim.LinearSLM;
import com.tonicartos.superslimexample.transaction.AccMgtTransactionHistoryInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StickyItemsWrapper implements Comparator<AccMgtTransactionHistoryInfo> {
    List<AccMgtTransactionHistoryInfo> itemListOriginal = new ArrayList<>();
    List<AccMgtTransactionHistoryInfo> itemListInAdapter = new ArrayList<>();

    Map<Object, AccMgtTransactionHistoryInfo> mapHeaders = new HashMap<>();

    public void clear() {
        itemListOriginal.clear();
        itemListInAdapter.clear();
    }

    public List<AccMgtTransactionHistoryInfo> getItemListToDisplays() {
        return itemListInAdapter;
    }

    public List<AccMgtTransactionHistoryInfo> processAddToListData(@NonNull List<AccMgtTransactionHistoryInfo> itemList) {
        sortByDate(itemList);
        itemListOriginal.addAll(itemList);
        return processGroupInAdapter(itemList);
    }

    private List<AccMgtTransactionHistoryInfo> processGroupInAdapter(List<AccMgtTransactionHistoryInfo> itemList) {
        int sectionFirstPosition = 0;
        for (int i = 0; i < itemList.size(); i++) {
            AccMgtTransactionHistoryInfo item = itemList.get(i);
            Object groupId = itemList.get(i).getGroupId();
            AccMgtTransactionHistoryInfo groupItem = mapHeaders.get(groupId);
            if (groupItem == null) {
                sectionFirstPosition = itemListInAdapter.size();
                groupItem = new AccMgtTransactionHistoryInfo(ViewType.TYPE_STICKY_HEADER, LinearSLM.ID, sectionFirstPosition);
                groupItem.transactionDateTime = item.transactionDateTime;
                groupItem.generateGroupDisplay();
                mapHeaders.put(groupId, groupItem);
                itemListInAdapter.add(groupItem);
            }
            item.updateGroupInfo(ViewType.TYPE_CONTENT, LinearSLM.ID, sectionFirstPosition);
            itemListInAdapter.add(item);
        }
        return itemListInAdapter;
    }

    private void sortByDate(List itemList) {
        Collections.sort(itemList, this);
    }

    @Override
    public int compare(AccMgtTransactionHistoryInfo o1, AccMgtTransactionHistoryInfo o2) {
        return ((Long) o2.getGroupId()).compareTo(o1.getGroupId());
    }
}
