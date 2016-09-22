package com.tonicartos.superslimexample.recycler;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class StickyItemsWrapper {
    public StickyItemsWrapper() {
        mItemList = new ArrayList<>();
    }

    List<StickyItemImpl> mItemList;

    private int headerCount;

    public void addToListData(@NonNull List<StickyItemImpl> initItemList) {
        mItemList.addAll(initItemList);
    }

    public List<StickyItemImpl> getList() {
        return mItemList;
    }

    public StickyItemImpl getLastItem() {
        return mItemList.isEmpty() ? null : mItemList.get(mItemList.size() - 1);
    }

    public int getHeaderCount() {
        return headerCount;
    }

    public void setHeaderCount(int headerCount){
        this.headerCount = headerCount;
    }
}
