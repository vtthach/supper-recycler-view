package com.tonicartos.superslimexample.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.tonicartos.superslim.LayoutManager;

import java.util.List;

public abstract class LoadMoreAdapter<T extends StickyItemImpl> extends StickyHeaderAdapter<T> {

    private static final int VISIBLE_ITEM_THRESHOLD = 5;

    private OnLoadMoreListener onLoadMoreListener;

    private boolean isLoading;

    public LoadMoreAdapter(RecyclerView recyclerView, Context context, int headerMode, List<T> itemList) {
        super(context, headerMode, itemList);
        initLoadMoreListener(recyclerView);
    }

    public void setLoaded() {
        setShowFooter(false);
        isLoading = false;
    }

    private void initLoadMoreListener(RecyclerView recyclerView) {
        final LayoutManager linearLayoutManager = (LayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (preConditionToLoadMore(linearLayoutManager.getItemCount(), linearLayoutManager.findLastVisibleItemPosition())
                        && isListenerAccept()) {
                    isLoading = true;
                    setShowFooter(true);
                    onLoadMoreListener.onLoadMore();
                }
            }
        });
    }

    private boolean isListenerAccept() {
        return onLoadMoreListener != null && onLoadMoreListener.canLoadMore();
    }

    private boolean preConditionToLoadMore(int totalItemCount, int lastVisibleItem) {
        return !isLoading && totalItemCount <= (lastVisibleItem + VISIBLE_ITEM_THRESHOLD);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();

        boolean canLoadMore();
    }
}
