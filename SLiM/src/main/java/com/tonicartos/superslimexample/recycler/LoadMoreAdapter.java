package com.tonicartos.superslimexample.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tonicartos.superslim.LayoutManager;
import com.tonicartos.superslimexample.R;

import java.util.List;

public class LoadMoreAdapter extends StickyAdapter<StickyItemImpl> {

    private int totalItemCount;
    private int lastVisibleItem;
    private boolean isLoading;
    private int visibleThreshold = 5;
    private OnLoadMoreListener onLoadMoreListener;

    public LoadMoreAdapter(RecyclerView recyclerView, Context context, int headerMode, List<StickyItemImpl> itemList) {
        super(context, headerMode, itemList);
        initLoadMoreListener(recyclerView);
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).getType();
    }

    public void setLoaded() {
        mItems.remove(mItems.size() - 1);
        notifyItemRemoved(mItems.size());
        isLoading = false;
    }

    private void initLoadMoreListener(RecyclerView recyclerView) {
        final LayoutManager linearLayoutManager = (LayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (preConditionToLoadMore() && isListenerAccept()) {
                    isLoading = true;
                    StickyItemImpl lastItem = mItems.get(mItems.size() - 1);
                    mItems.add(new StickyItemImpl(ViewType.TYPE_LOADMORE, LINEAR, lastItem.sectionFirstPosition()));
                    notifyItemInserted(mItems.size() - 1);
                    onLoadMoreListener.onLoadMore();
                }
            }
        });
    }

    private boolean isListenerAccept() {
        return onLoadMoreListener != null && onLoadMoreListener.canLoadMore();
    }

    private boolean preConditionToLoadMore() {
        return !isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();

        boolean canLoadMore();
    }

    public String itemToString(int position) {
        return mItems.get(position).text;
    }


    @Override
    public GenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ViewType.TYPE_LOADMORE) {
            return onCreateLoadingViewHolder(parent);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    protected GenericViewHolder onCreateLoadingViewHolder(ViewGroup parent) {
        return new LoadingViewHolder(LayoutInflater.from(mContext).inflate(R.layout.loading_view, parent, false));
    }

    @Override
    public GenericViewHolder onCreateItemHolder(ViewGroup parent) {
        return new ItemViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.text_line_item, parent, false));
    }

    @Override
    public GenericViewHolder onCreateHeaderHolder(ViewGroup parent) {
        return new HeaderViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.header_item, parent, false));
    }

    static class HeaderViewHolder extends GenericViewHolder<StickyItemImpl> {
        TextView textView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text);
        }

        @Override
        public void bindItem(int position, StickyItemImpl item) {
            textView.setText(item.text);
        }
    }

    static class ItemViewHolder extends GenericViewHolder<StickyItemImpl> {
        TextView textView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text);
        }

        @Override
        public void bindItem(int position, StickyItemImpl item) {
            textView.setText(item.text);
        }
    }

    static class LoadingViewHolder extends GenericViewHolder<StickyItem> {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.pbLoading);
        }

        @Override
        public void bindItem(int position, StickyItem item) {
            progressBar.setIndeterminate(true);
        }
    }
}
