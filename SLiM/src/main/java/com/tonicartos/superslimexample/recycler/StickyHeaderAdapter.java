package com.tonicartos.superslimexample.recycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.tonicartos.superslim.GridSLM;
import com.tonicartos.superslim.LinearSLM;
import com.tonicartos.superslimexample.R;

import java.util.List;

public abstract class StickyHeaderAdapter<T extends StickyItem> extends BaseRecyclerViewAdapter<T> {

    private int mHeaderDisplay;

    private boolean mMarginsFixed;

    public StickyHeaderAdapter(Context context, int headerMode, @NonNull List<T> itemList) {
        super(context, itemList);
        mHeaderDisplay = headerMode;
    }

    public boolean isItemStickyHeader(int position) {
        return getItem(position).isStickyHeader();
    }

    @Override
    public BaseRecyclerViewAdapter.GenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ViewType.TYPE_STICKY_HEADER) {
            return onCreateStickyHeaderHolder(parent);
        } else {
            return super.onCreateViewHolder(parent, viewType);
        }
    }

    public abstract BaseRecyclerViewAdapter.GenericViewHolder onCreateStickyHeaderHolder(ViewGroup parent);

    @Override
    public void onBindViewHolder(BaseRecyclerViewAdapter.GenericViewHolder holder, int position) {
        // When wanna support HEADER_TYPE vs STICKY_HEADER_TYPE ->> Update here
        final T item = (position == itemList.size() ? null : itemList.get(position));
        holder.bindItem(position, item);
        if (item == null) {
            Log.i("vtt", "item null:" + position);
            Log.i("vtt", "List size when null:" + itemList.size());
        }
        updateLayoutParam(holder, item);
    }

    private void updateLayoutParam(BaseRecyclerViewAdapter.GenericViewHolder holder, StickyItem item) {
        final View itemView = holder.itemView;
        final GridSLM.LayoutParams lp = GridSLM.LayoutParams.from(itemView.getLayoutParams());
        // Overrides xml attrs, could use different layouts too.
        if (item == null) {
            // Layout for footer
            lp.setSlm(LinearSLM.ID);
            lp.setFirstPosition(itemList.get(itemList.size() - 1).sectionFirstPosition()); // Importance
        } else {
            if (item.isStickyHeader()) {
                lp.headerDisplay = mHeaderDisplay;
                if (lp.isHeaderInline() || (mMarginsFixed && !lp.isHeaderOverlay())) {
                    lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                } else {
                    lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                }
                lp.headerEndMarginIsAuto = !mMarginsFixed;
                lp.headerStartMarginIsAuto = !mMarginsFixed;
            }
            lp.setSlm(item.sectionManagerType());
            lp.setColumnWidth(mContext.getResources().getDimensionPixelSize(R.dimen.grid_column_width));
            lp.setFirstPosition(item.sectionFirstPosition()); // Importance
        }
        itemView.setLayoutParams(lp);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == itemList.size()) {
            return super.getItemViewType(position);
        }
        return itemList.get(position).isStickyHeader() ? ViewType.TYPE_STICKY_HEADER : super.getItemViewType(position);
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
        for (int i = 0; i < itemList.size(); i++) {
            StickyItem item = itemList.get(i);
            if (item.isStickyHeader()) {
                notifyItemChanged(i);
            }
        }
    }

    @Override
    public GenericViewHolder onCreateHeaderHolder(ViewGroup parent) {
        throw new UnsupportedOperationException();
    }
}
