package com.tonicartos.superslimexample.recycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.tonicartos.superslim.GridSLM;
import com.tonicartos.superslim.LinearSLM;
import com.tonicartos.superslimexample.R;

import java.util.List;

public abstract class StickyAdapter<T extends StickyItem> extends RecyclerView.Adapter<StickyAdapter.GenericViewHolder> {

    public static final int LINEAR = 0;

    protected final List<T> mItems;

    private int mHeaderDisplay;

    private boolean mMarginsFixed;

    protected final Context mContext;

    public StickyAdapter(Context context, int headerMode, @NonNull List<T> itemList) {
        mContext = context;
        mHeaderDisplay = headerMode;
        mItems = itemList;
    }

    public boolean isItemHeader(int position) {
        return mItems.get(position).isHeader();
    }

    @Override
    public GenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ViewType.TYPE_HEADER) {
            return onCreateHeaderHolder(parent);
        } else if (viewType == ViewType.TYPE_CONTENT) {
            return onCreateItemHolder(parent);
        } else {
            throw new RuntimeException("StickyAdapter: View type not found!!");
        }
    }

    public abstract GenericViewHolder onCreateItemHolder(ViewGroup parent);

    public abstract GenericViewHolder onCreateHeaderHolder(ViewGroup parent);

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(GenericViewHolder holder, int position) {
        final T item = mItems.get(position);
        holder.bindItem(position, item);
        updateLayoutParam(holder, item);
    }

    private void updateLayoutParam(GenericViewHolder holder, StickyItem item) {
        final View itemView = holder.itemView;
        final GridSLM.LayoutParams lp = GridSLM.LayoutParams.from(itemView.getLayoutParams());
        // Overrides xml attrs, could use different layouts too.
        if (item.isHeader()) {
            lp.headerDisplay = mHeaderDisplay;
            if (lp.isHeaderInline() || (mMarginsFixed && !lp.isHeaderOverlay())) {
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            } else {
                lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            }
            lp.headerEndMarginIsAuto = !mMarginsFixed;
            lp.headerStartMarginIsAuto = !mMarginsFixed;
        }
        lp.setSlm(item.sectionManagerType() == LINEAR ? LinearSLM.ID : GridSLM.ID);
        lp.setColumnWidth(mContext.getResources().getDimensionPixelSize(R.dimen.grid_column_width));
        lp.setFirstPosition(item.sectionFirstPosition()); // Importance
        itemView.setLayoutParams(lp);
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).isHeader() ? ViewType.TYPE_HEADER : ViewType.TYPE_CONTENT;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setHeaderDisplay(int headerDisplay) {
        mHeaderDisplay = headerDisplay;
        notifyHeaderChanges();
    }

    public void setMarginsFixed(boolean marginsFixed) {
        mMarginsFixed = marginsFixed;
        notifyHeaderChanges();
    }

    private void notifyHeaderChanges() {
        for (int i = 0; i < mItems.size(); i++) {
            StickyItem item = mItems.get(i);
            if (item.isHeader()) {
                notifyItemChanged(i);
            }
        }
    }

    public abstract static class GenericViewHolder<T extends StickyItem> extends RecyclerView.ViewHolder {
        public GenericViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void bindItem(int position, T item);
    }
}
