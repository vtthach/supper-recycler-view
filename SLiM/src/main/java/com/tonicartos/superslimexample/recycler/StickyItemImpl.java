package com.tonicartos.superslimexample.recycler;

import com.tonicartos.superslim.LinearSLM;

public abstract class StickyItemImpl<T, V> implements StickyItem {
    private int sectionManagerType = LinearSLM.ID; // Default layout type -> use GridSLM.ID if want sticky item grid
    private int sectionFirstPosition;
    private int type;
    protected T groupId;
    protected V groupDisplay;

    public T getGroupId() {
        return groupId;
    }

    public V getGroupDisplay() {
        return groupDisplay;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public boolean isStickyHeader() {
        return type == BaseRecyclerViewAdapter.ViewType.TYPE_STICKY_HEADER;
    }

    @Override
    public int sectionManagerType() {
        return sectionManagerType;
    }

    @Override
    public int sectionFirstPosition() {
        return sectionFirstPosition;
    }

    public void updateGroupInfo(int viewType, int sectionFirstPosition, T groupId, V groupDisplay) {
        this.type = viewType;
        this.sectionFirstPosition = sectionFirstPosition;
        this.groupId = groupId;
        this.groupDisplay = groupDisplay;
    }
}
