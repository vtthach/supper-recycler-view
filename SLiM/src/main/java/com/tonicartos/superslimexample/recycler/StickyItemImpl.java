package com.tonicartos.superslimexample.recycler;

public class StickyItemImpl implements StickyItem {

    private final int type;
    private final int sectionManagerType;
    private final int sectionFirstPosition;

    public String text;

    public int getType() {
        return type;
    }

    public StickyItemImpl(int type, int sectionManagerType, int sectionFirstPosition) {
        this.type = type;
        this.sectionManagerType = sectionManagerType;
        this.sectionFirstPosition = sectionFirstPosition;
    }

    @Override
    public boolean isHeader() {
        return type == ViewType.TYPE_HEADER;
    }

    @Override
    public int sectionManagerType() {
        return sectionManagerType;
    }

    @Override
    public int sectionFirstPosition() {
        return sectionFirstPosition;
    }
}
