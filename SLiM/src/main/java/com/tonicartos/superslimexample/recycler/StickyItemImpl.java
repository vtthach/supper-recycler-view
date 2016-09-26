package com.tonicartos.superslimexample.recycler;

public abstract class StickyItemImpl implements StickyItem {
    private int sectionManagerType;
    private int sectionFirstPosition;
    private int type;

    public StickyItemImpl(int type, int sectionManagerType, int sectionFirstPosition) {
        this.type = type;
        this.sectionManagerType = sectionManagerType;
        this.sectionFirstPosition = sectionFirstPosition;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public boolean isStickyHeader() {
        return type == ViewType.TYPE_STICKY_HEADER;
    }

    @Override
    public int sectionManagerType() {
        return sectionManagerType;
    }

    @Override
    public int sectionFirstPosition() {
        return sectionFirstPosition;
    }

    public abstract long getGroupId();

    public void updateGroupInfo(int viewType, int sectionManagerType, int sectionFirstPosition){
        this.type = viewType;
        this.sectionManagerType = sectionManagerType;
        this.sectionFirstPosition = sectionFirstPosition;
    }
}
