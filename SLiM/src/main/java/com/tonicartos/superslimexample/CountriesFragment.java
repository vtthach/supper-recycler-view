package com.tonicartos.superslimexample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tonicartos.superslim.LayoutManager;
import com.tonicartos.superslimexample.recycler.StickyUtils;
import com.tonicartos.superslimexample.recycler.LoadMoreAdapter;
import com.tonicartos.superslimexample.recycler.StickyItemGenerator;
import com.tonicartos.superslimexample.transaction.AccMgtTransactionHistoryAdapter;
import com.tonicartos.superslimexample.transaction.AccMgtTransactionHistoryInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Fragment that displays a list of country names.
 */
public class CountriesFragment extends Fragment {

    private static final String KEY_HEADER_POSITIONING = "key_header_mode";

    private static final String KEY_MARGINS_FIXED = "key_margins_fixed";

    private ViewHolder mViews;

    private AccMgtTransactionHistoryAdapter mAdapter;

    private int mHeaderDisplay;

    private boolean mAreMarginsFixed;

    private Random mRng = new Random();

    public boolean areHeadersOverlaid() {
        return (mHeaderDisplay & LayoutManager.LayoutParams.HEADER_OVERLAY) != 0;
    }

    public boolean areHeadersSticky() {
        return (mHeaderDisplay & LayoutManager.LayoutParams.HEADER_STICKY) != 0; // NOSONAR will be replace in next version of SLiM library
    }

    public boolean areMarginsFixed() {
        return mAreMarginsFixed;
    }

    public int getHeaderMode() {
        return mHeaderDisplay;
    }

    public void setHeaderMode(int mode) {
        mHeaderDisplay = mode | (mHeaderDisplay & LayoutManager.LayoutParams.HEADER_OVERLAY) | (
                mHeaderDisplay & LayoutManager.LayoutParams.HEADER_STICKY);// NOSONAR will be replace in next version of SLiM library
        mAdapter.setHeaderDisplay(mHeaderDisplay);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.account_transaction_history_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            mHeaderDisplay = savedInstanceState
                    .getInt(KEY_HEADER_POSITIONING,
                            getResources().getInteger(R.integer.default_header_display));
            mAreMarginsFixed = savedInstanceState
                    .getBoolean(KEY_MARGINS_FIXED,
                            getResources().getBoolean(R.bool.default_margins_fixed));
        } else {
            mHeaderDisplay = LayoutManager.LayoutParams.HEADER_INLINE
                    | LayoutManager.LayoutParams.HEADER_STICKY; // NOSONAR will be replace in next version of SLiM library
            mAreMarginsFixed = getResources().getBoolean(R.bool.default_margins_fixed);
        }
        mViews = new ViewHolder(view);
        mViews.initViews(new LayoutManager(getActivity()));
        mAdapter = new AccMgtTransactionHistoryAdapter(mViews.mRecyclerView, getActivity(), mHeaderDisplay, mViews.initItemList());
        mAdapter.setMarginsFixed(mAreMarginsFixed);
        mAdapter.setHeaderDisplay(mHeaderDisplay);
        mViews.setAdapter(mAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_HEADER_POSITIONING, mHeaderDisplay);
        outState.putBoolean(KEY_MARGINS_FIXED, mAreMarginsFixed);
    }

    public void scrollToRandomPosition() {
        int position = mRng.nextInt(mAdapter.getItemCount());
        mViews.scrollToPosition(position);
    }

    public void setHeadersOverlaid(boolean areHeadersOverlaid) {
        mHeaderDisplay = areHeadersOverlaid ? mHeaderDisplay
                | LayoutManager.LayoutParams.HEADER_OVERLAY
                : mHeaderDisplay & ~LayoutManager.LayoutParams.HEADER_OVERLAY;
        mAdapter.setHeaderDisplay(mHeaderDisplay);
    }

    public void setHeadersSticky(boolean areHeadersSticky) {
        mHeaderDisplay = areHeadersSticky ? mHeaderDisplay
                | LayoutManager.LayoutParams.HEADER_STICKY // NOSONAR will be replace in next version of SLiM library
                : mHeaderDisplay & ~LayoutManager.LayoutParams.HEADER_STICKY; // NOSONAR will be replace in next version of SLiM library
        mAdapter.setHeaderDisplay(mHeaderDisplay);
    }

    public void setMarginsFixed(boolean areMarginsFixed) {
        mAreMarginsFixed = areMarginsFixed;
        mAdapter.setMarginsFixed(areMarginsFixed);
    }

    public void smoothScrollToRandomPosition() {
        int position = mRng.nextInt(mAdapter.getItemCount());
        mViews.smoothScrollToPosition(position);
    }

    private static class ViewHolder implements LoadMoreAdapter.OnLoadMoreListener {
        public final StickyItemGenerator<AccMgtTransactionHistoryInfo, Long, String> stickyItemGenerator;
        private final RecyclerView mRecyclerView;
        private int loadMore = 0;
        private LoadMoreAdapter adapter;
        private int dateTest;

        public ViewHolder(View view) {
            mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
            stickyItemGenerator = new StickyItemGenerator<AccMgtTransactionHistoryInfo, Long, String>(AccMgtTransactionHistoryInfo.class) {

                @Override
                public int compare(AccMgtTransactionHistoryInfo o1, AccMgtTransactionHistoryInfo o2) {
                    return o2.getGroupId() != null
                            ? o2.getGroupId().compareTo(o1.getGroupId())
                            : o1.getGroupId() == null ? 0 : -1;
                }

                @Override
                public String getGroupDisplay(Long groupId) {
                    return StickyUtils.getFormatDateLong(groupId);
                }
            };
        }

        public void initViews(LayoutManager lm) {
            mRecyclerView.setLayoutManager(lm);
        }

        public void scrollToPosition(int position) {
            mRecyclerView.scrollToPosition(position);
        }

        public void setAdapter(LoadMoreAdapter adapter) {
            this.adapter = adapter;
            mRecyclerView.setAdapter(adapter);
            adapter.setOnLoadMoreListener(this);
        }

        public void smoothScrollToPosition(int position) {
            mRecyclerView.smoothScrollToPosition(position);
        }

        @Override
        public void onLoadMore() {
            mRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.i("vtt", "Start get data OnLoadMore:" + loadMore);
                    adapter.setLoaded();
                    stickyItemGenerator.processAddToListData(getInitItemList(loadMore + "_", R.array.country_names_2));
                    adapter.notifyDataSetChanged();
                }
            }, 4000);
        }

        @Override
        public boolean canLoadMore() {
            if (loadMore < 10) {
                loadMore++;
                return true;
            } else {
                return false;
            }
        }

        public List<AccMgtTransactionHistoryInfo> initItemList() {
            return stickyItemGenerator.processAddToListData(getInitItemList(0 + "_", R.array.country_names));
        }

        private List<AccMgtTransactionHistoryInfo> getInitItemList(String prefixList, int arrayResStr) {
            final String[] countryNames = mRecyclerView.getResources().getStringArray(arrayResStr);
            ArrayList<AccMgtTransactionHistoryInfo> items = new ArrayList<>();
            // Insert headers into list of items.
            for (int i = 0; i < countryNames.length; i++) {
                long date = getDateTest();
                for (int j = 0; j < 3; j++) {
                    AccMgtTransactionHistoryInfo accMgtTransactionHistoryInfo = new AccMgtTransactionHistoryInfo();
                    accMgtTransactionHistoryInfo.transactionDescriptionOne = prefixList + "-" + (i == 0 ? "End" + countryNames[i] + "--- " + j : countryNames[i] + "--- " + j);
                    accMgtTransactionHistoryInfo.transactionDateTime = date;
                    items.add(accMgtTransactionHistoryInfo);
                }
            }
            return items;
        }

        private long getDateTest() {
            dateTest--;
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, dateTest);
            return calendar.getTimeInMillis();
        }
    }
}
