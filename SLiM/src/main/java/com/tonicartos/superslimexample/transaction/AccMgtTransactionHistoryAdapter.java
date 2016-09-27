package com.tonicartos.superslimexample.transaction;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tonicartos.superslimexample.R;
import com.tonicartos.superslimexample.recycler.LoadMoreAdapter;

import java.util.List;

public class AccMgtTransactionHistoryAdapter extends LoadMoreAdapter<AccMgtTransactionHistoryInfo> {


    public AccMgtTransactionHistoryAdapter(RecyclerView recyclerView, Context context, int headerMode, List<AccMgtTransactionHistoryInfo> itemList) {
        super(recyclerView, context, headerMode, itemList);
    }

    @Override
    public GenericViewHolder onCreateFooterHolder(ViewGroup parent) {
        return new LoadingViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.loading_view, parent, false));
    }

    @Override
    public GenericViewHolder onCreateContentHolder(ViewGroup parent) {
        return new ItemViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.text_line_item, parent, false));
    }

    @Override
    public GenericViewHolder onCreateStickyHeaderHolder(ViewGroup parent) {
        return new StickyHeaderViewHolder(LayoutInflater.from(context)
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
            textView.setText(accMgtTransactionHistoryInfo.getGroupDisplay());
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
