package com.tonicartos.superslimexample.recycler;

public interface StickyItem {
    /**
     * @return is header item or not
     */
    boolean isStickyHeader();

    /**
     * @return Must be one of LinearSLM.ID or GridSLM.ID
     */
    int sectionManagerType();

    /**
     * @return first index of position group
     */
    int sectionFirstPosition();

    /**
     * @return View type of item
     */
    int getType();
}
