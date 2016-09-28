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

import butterknife.Bind;
import butterknife.ButterKnife;

public class AccMgtTransactionHistoryAdapter extends LoadMoreAdapter<AccMgtTransactionHistoryInfo> {

    public AccMgtTransactionHistoryAdapter(RecyclerView recyclerView, Context context, int headerMode, List<AccMgtTransactionHistoryInfo> itemList) {
        super(recyclerView, context, headerMode, itemList);
    }

    @Override
    public GenericViewHolder onCreateFooterHolder(ViewGroup parent) {
        return new LoadingViewHolder(inflateView(parent, R.layout.account_transaction_history_loading_view));
    }

    @Override
    public GenericViewHolder onCreateContentHolder(ViewGroup parent) {
        return new ItemViewHolder(inflateView(parent, R.layout.account_transaction_history_view_item_content));
    }

    @Override
    public GenericViewHolder onCreateStickyHeaderHolder(ViewGroup parent) {
        return new StickyHeaderViewHolder(inflateView(parent, R.layout.account_transaction_history_view_item_sticky_header));
    }

    private View inflateView(ViewGroup parent, int layoutId) {
        return LayoutInflater.from(context).inflate(layoutId, parent, false);
    }

    static class StickyHeaderViewHolder extends GenericViewHolder {

        TextView textView;

        public StickyHeaderViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tvHeaderDate);
        }

        @Override
        public void bindItem(int position, Object item) {
            AccMgtTransactionHistoryInfo accMgtTransactionHistoryInfo = (AccMgtTransactionHistoryInfo) item;
            textView.setText(accMgtTransactionHistoryInfo.getGroupDisplay());
        }
    }

    static class ItemViewHolder extends GenericViewHolder {
        @Bind(R.id.tvTransType)
        TextView tvTransType;
        @Bind(R.id.tvTransAmount)
        TextView tvTransAmount;
        @Bind(R.id.tvTransDescription)
        TextView tvTransDescription;
        @Bind(R.id.tvTransFees)
        TextView tvTransFees;

        Context context;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        @Override
        public void bindItem(int position, Object item) {
            AccMgtTransactionHistoryInfo info = (AccMgtTransactionHistoryInfo) item;
            tvTransDescription.setText(info.transactionDescriptionOne);
            tvTransType.setText(info.transactionType);
            double amount = info.transactionAmountDebit - info.transactionAmountCredit;
            tvTransAmount.setText(getAmountFormat(amount));
            tvTransFees.setText(getAmountFormat(info.serviceFee));
            updateColorAmount(amount > 0
                            ? R.color.account_transaction_history_amount_green
                            : R.color.account_transaction_history_amount_red
                    , tvTransAmount);
        }

        private void updateColorAmount(int colorResId, TextView tv) {
            tv.setTextColor(context.getResources().getColor(colorResId));
        }

        private String getAmountFormat(double amount) {
            // TODO vtt format money here
            String sig = amount > 0 ? "" : "-";
            return sig + "R" + Math.abs(amount);
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
