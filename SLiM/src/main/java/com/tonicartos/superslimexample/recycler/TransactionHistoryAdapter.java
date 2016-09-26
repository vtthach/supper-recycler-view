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
import com.tonicartos.superslimexample.transaction.AccMgtTransactionHistoryInfo;

import java.util.List;

public class TransactionHistoryAdapter extends StickyHeaderAdapter<AccMgtTransactionHistoryInfo> {

    private int totalItemCount;
    private int lastVisibleItem;
    private boolean isLoading;
    private OnLoadMoreListener onLoadMoreListener;
    private static final int VISIBLE_ITEM_THRESHOLD = 5;

    public TransactionHistoryAdapter(RecyclerView recyclerView, Context context, int headerMode, List<AccMgtTransactionHistoryInfo> itemList) {
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
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (preConditionToLoadMore() && isListenerAccept()) {
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

    private boolean preConditionToLoadMore() {
        return !isLoading && totalItemCount <= (lastVisibleItem + VISIBLE_ITEM_THRESHOLD);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();

        boolean canLoadMore();
    }

    public String itemToString(int position) {
        return itemList.get(position).transactionDescriptionOne;
    }

    @Override
    public GenericViewHolder onCreateFooterHolder(ViewGroup parent) {
        return new LoadingViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.loading_view, parent, false));
    }

    @Override
    public GenericViewHolder onCreateContentHolder(ViewGroup parent) {
        return new ItemViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.text_line_item, parent, false));
    }

    @Override
    public GenericViewHolder onCreateStickyHeaderHolder(ViewGroup parent) {
        return new StickyHeaderViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.header_item, parent, false));
    }

    static class StickyHeaderViewHolder extends GenericViewHolder {

        TextView textView;

        public StickyHeaderViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text);
        }

        @Override
        public void bindItem(int position, Object item) {
            AccMgtTransactionHistoryInfo accMgtTransactionHistoryInfo = (AccMgtTransactionHistoryInfo) item;
            textView.setText(accMgtTransactionHistoryInfo.getTransactionDateDisplay());
        }
    }

    static class ItemViewHolder extends GenericViewHolder {
        TextView textView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text);
        }

        @Override
        public void bindItem(int position, Object item) {
            AccMgtTransactionHistoryInfo accMgtTransactionHistoryInfo = (AccMgtTransactionHistoryInfo) item;
            textView.setText(accMgtTransactionHistoryInfo.transactionDescriptionOne);
        }
    }

    static class LoadingViewHolder extends GenericViewHolder {
        private ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.pbLoading);
        }

        @Override
        public void bindItem(int position, Object item) {
            progressBar.setIndeterminate(true);
        }
    }
}
