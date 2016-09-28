package com.tonicartos.superslimexample.recycler;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class StickyItemGenerator<T extends StickyItemImpl<E, F>, E, F> implements Comparator<T> {
    List<T> itemListOriginal = new ArrayList<>();
    List<T> itemListInAdapter = new ArrayList<>();
    Map<Object, T> mapHeaders = new HashMap<>();
    Class<T> clazz;

    public StickyItemGenerator(Class<T> clazz) {
        this.clazz = clazz;
    }

    public void clear() {
        itemListOriginal.clear();
        itemListInAdapter.clear();
        mapHeaders.clear();
    }

    public List<T> processAddToListData(@NonNull List<T> itemList) {
        itemListOriginal.addAll(itemList);
        Collections.sort(itemListOriginal, this);
        try {
            return generateGroupItem(itemListOriginal);
        } catch (Exception e) { // NOSONAR
            throw new StickyGeneratorException("StickyItemGenerator processAddToListData error: " + e.getMessage(), e);
        }
    }

    private List<T> generateGroupItem(List<T> itemList) throws IllegalAccessException, InstantiationException {
        mapHeaders.clear();
        List<T> tempList = new ArrayList<>();
        int sectionFirstPosition = 0;
        for (int i = 0; i < itemList.size(); i++) {
            T item = itemList.get(i);
            E groupId = item.getGroupId();
            // Add group item first
            T groupItem = mapHeaders.get(groupId);
            if (groupItem == null) {
                sectionFirstPosition = tempList.size();
                groupItem = clazz.newInstance();
                groupItem.updateGroupInfo(BaseRecyclerViewAdapter.ViewType.TYPE_STICKY_HEADER
                        , sectionFirstPosition
                        , groupId
                        , getGroupDisplay(groupId));
                mapHeaders.put(groupId, groupItem);
                tempList.add(groupItem);
            }
            // Add item to list
            item.updateGroupInfo(BaseRecyclerViewAdapter.ViewType.TYPE_CONTENT
                    , sectionFirstPosition
                    , groupId
                    , getGroupDisplay(groupId));
            tempList.add(item);
        }
        itemListInAdapter.clear();
        itemListInAdapter.addAll(tempList);
        tempList.clear();
        return itemListInAdapter;
    }

    public abstract F getGroupDisplay(E groupId);

    public static class StickyGeneratorException extends RuntimeException {
        public StickyGeneratorException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
