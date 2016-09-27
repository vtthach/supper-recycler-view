package com.tonicartos.superslimexample.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewAdapter.GenericViewHolder> {

    /**
     * Layout inflater from captureShopperCardView.
     */
    protected LayoutInflater inflater;
    /**
     * The captureShopperCardView which will show list item.
     */
    protected Context context;
    /**
     * Data of items.
     */
    protected List<T> itemList;


    /**
     * set is show footer.
     */
    private boolean isShowFooter = false;

    /**
     * Create list adapter.
     *
     * @param context The captureShopperCardView which will show list item.
     * @param items   List data.
     */
    public BaseRecyclerViewAdapter(Context context, List<T> items) {
        // Cache the LayoutInflate to avoid asking for a new one each time.
        this.inflater = LayoutInflater.from(context);
        this.itemList = items;
        this.context = context;
    }

    /**
     * Append more data
     *
     * @param items
     */
    public void append(List<T> items) {
        if (itemList != null && items != null) {
            itemList.addAll(items);
            notifyItemRangeInserted(this.getItemCount(), items.size());
        }
    }

    /**
     * @param from
     * @param items
     */
    public void append(int from, List<T> items) {
        if (itemList != null && items != null) {
            itemList.addAll(items);
            notifyItemRangeInserted(from, itemList.size());
        }
    }

    public void append(T item) {
        if (itemList != null && item != null) {
            itemList.add(item);
            notifyItemInserted(this.getItemCount() - 1);
        }
    }

    /**
     * To remove an message item from the list at specific position
     *
     * @param position position of item needed to remove
     */
    public void removeSelectedItem(int position) {
        if (itemList == null || itemList.size() <= position) {
            return;
        }
        itemList.remove(getItemPositionWithoutHeader(position));
        this.notifyItemRemoved(position);
        // this line is very important to update all the views belows
        // the item removed will adjust accordingly
        this.notifyItemRangeChanged(position, itemList.size());
    }

    @Override
    public GenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ViewType.TYPE_CONTENT) {
            // Item
            return onCreateContentHolder(parent);
        } else if (viewType == ViewType.TYPE_HEADER) {
            // Header
            return onCreateHeaderHolder(parent);
        } else if (viewType == ViewType.TYPE_FOOTER) {
            // Footer
            return onCreateFooterHolder(parent);
        }
        String detailMessage = "there is no type that matches the type " + viewType +
                " + make sure your using types correctly";
        throw new RuntimeException(detailMessage); // NOSONAR
    }

    public abstract GenericViewHolder onCreateFooterHolder(ViewGroup parent);

    public abstract GenericViewHolder onCreateHeaderHolder(ViewGroup parent);

    public abstract GenericViewHolder onCreateContentHolder(ViewGroup parent);

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return ViewType.TYPE_HEADER;
        } else if (isPositionFooter(position)) {
            return ViewType.TYPE_FOOTER;
        }
        return ViewType.TYPE_CONTENT;
    }

    @Override
    public void onBindViewHolder(GenericViewHolder holder, int position) {
        holder.bindItem(position, getItem(position));
    }

    /**
     * is position header.
     *
     * @param position
     * @return
     */
    protected boolean isPositionHeader(int position) {
        return isHasHeader() && position == 0;
    }

    /**
     * is position footer.
     *
     * @param position
     * @return
     */
    public boolean isPositionFooter(int position) {
        int itemCount = getItemCountReal() + getItemCountHeader();
        return isHasFooter() && position == itemCount;
    }

    @Override
    public int getItemCount() {
        int size = getItemCountReal();
        size += getItemCountHeader();
        size += getItemCountFooter();
        return size;
    }

    /**
     * get item size real.
     *
     * @return
     */
    public int getItemCountReal() {
        int size = 0;
        if (itemList != null) {
            size = itemList.size();
        }
        return size;
    }

    /**
     * get item count header.
     *
     * @return
     */
    private int getItemCountHeader() {
        if (isHasHeader()) {
            return 1;
        }
        return 0;
    }

    /**
     * get item count footer.
     *
     * @return
     */
    private int getItemCountFooter() {
        if (isHasFooter()) {
            return 1;
        }
        return 0;
    }

    /**
     * get item in list data.
     *
     * @param position
     * @return
     */
    public T getItem(int position) {
        // get real position.
        // we need get position to mapping with list
        // from header to current item.
        // so: only get without header.
        int pos = getItemPositionWithoutHeader(position);
        if (pos >= 0 && itemList != null && itemList.size() > pos) {
            return itemList.get(pos);
        }
        return null;
    }

    public void setItem(int position, T t) {
        int pos = getItemPositionWithoutHeader(position);
        if (pos >= 0 && itemList != null && itemList.size() > pos) {
            itemList.set(pos, t);
        }
    }

    /**
     * get real position in list data.
     *
     * @param position
     * @return
     */
    protected int getItemPositionWithoutHeader(int position) {
        //if we have header: real position need -1
        //otherwise: keep position
        return position - getItemCountHeader();
    }

    /**
     * is contain header.
     *
     * @return
     */
    protected boolean isHasHeader(){
        return false;
    }

    /**
     * is contain footer.
     *
     * @return
     */
    protected boolean isHasFooter() {
        return isShowFooter();
    }

    public boolean isShowFooter() {
        return isShowFooter;
    }

    /**
     * set show footer.
     *
     * @param isShow
     */
    public void setShowFooter(boolean isShow) {
        if (isShow == isShowFooter) {
            return;
        }
        isShowFooter = isShow;
        if (isShow) {
            notifyItemInserted(getItemCount());
        } else {
            notifyItemRemoved(getItemCount());
        }
    }

    public abstract static class GenericViewHolder extends RecyclerView.ViewHolder {
        public GenericViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void bindItem(int position, Object item);
    }

    public static final class ViewType {
        public static final int TYPE_STICKY_HEADER = 0x00;
        public static final int TYPE_HEADER = 0x01;
        public static final int TYPE_CONTENT = 0x02;
        public static final int TYPE_FOOTER = 0x03;

        private ViewType() {
        }
    }
}