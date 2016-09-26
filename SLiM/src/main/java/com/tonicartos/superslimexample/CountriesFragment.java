package com.tonicartos.superslimexample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tonicartos.superslim.LayoutManager;
import com.tonicartos.superslimexample.recycler.StickyItemsWrapper;
import com.tonicartos.superslimexample.recycler.TransactionHistoryAdapter;
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

    private TransactionHistoryAdapter mAdapter;

    private int mHeaderDisplay;

    private boolean mAreMarginsFixed;

    private Random mRng = new Random();

    private Toast mToast = null;

    public boolean areHeadersOverlaid() {
        return (mHeaderDisplay & LayoutManager.LayoutParams.HEADER_OVERLAY) != 0;
    }

    public boolean areHeadersSticky() {
        return (mHeaderDisplay & LayoutManager.LayoutParams.HEADER_STICKY) != 0;
    }

    public boolean areMarginsFixed() {
        return mAreMarginsFixed;
    }

    public int getHeaderMode() {
        return mHeaderDisplay;
    }

    public void setHeaderMode(int mode) {
        mHeaderDisplay = mode | (mHeaderDisplay & LayoutManager.LayoutParams.HEADER_OVERLAY) | (
                mHeaderDisplay & LayoutManager.LayoutParams.HEADER_STICKY);
        mAdapter.setHeaderDisplay(mHeaderDisplay);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
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
                    | LayoutManager.LayoutParams.HEADER_STICKY;
            mAreMarginsFixed = getResources().getBoolean(R.bool.default_margins_fixed);
        }
        mViews = new ViewHolder(view);
        mViews.initViews(new LayoutManager(getActivity()));
        mAdapter = new TransactionHistoryAdapter(mViews.mRecyclerView, getActivity(), mHeaderDisplay, mViews.initItemList());
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
        String s = "Scroll to position " + position
                + (mAdapter.isItemStickyHeader(position) ? ", header " : ", item ")
                + mAdapter.itemToString(position) + ".";
        if (mToast != null) {
            mToast.setText(s);
        } else {
            mToast = Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT);
        }
        mToast.show();
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
                | LayoutManager.LayoutParams.HEADER_STICKY
                : mHeaderDisplay & ~LayoutManager.LayoutParams.HEADER_STICKY;
        mAdapter.setHeaderDisplay(mHeaderDisplay);
    }

    public void setMarginsFixed(boolean areMarginsFixed) {
        mAreMarginsFixed = areMarginsFixed;
        mAdapter.setMarginsFixed(areMarginsFixed);
    }

    public void smoothScrollToRandomPosition() {
        int position = mRng.nextInt(mAdapter.getItemCount());
        String s = "Smooth scroll to position " + position
                + (mAdapter.isItemStickyHeader(position) ? ", header " : ", item ")
                + mAdapter.itemToString(position) + ".";
        if (mToast != null) {
            mToast.setText(s);
        } else {
            mToast = Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT);
        }
        mToast.show();
        mViews.smoothScrollToPosition(position);
    }

    private static class ViewHolder implements TransactionHistoryAdapter.OnLoadMoreListener {

        private final RecyclerView mRecyclerView;
        private int loadMore = 0;
        private TransactionHistoryAdapter adapter;

        public StickyItemsWrapper stickyItemWrapper;
        private int dateTest;

        public ViewHolder(View view) {
            mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
            stickyItemWrapper = new StickyItemsWrapper();
        }

        public void initViews(LayoutManager lm) {
            mRecyclerView.setLayoutManager(lm);
        }

        public void scrollToPosition(int position) {
            mRecyclerView.scrollToPosition(position);
        }

        public void setAdapter(TransactionHistoryAdapter adapter) {
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
                    stickyItemWrapper.processAddToListData(getInitItemList(loadMore + "_", R.array.country_names_2));
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
            return stickyItemWrapper.processAddToListData(getInitItemList(0 + "_", R.array.country_names));
        }

//        private List<AccMgtTransactionHistoryInfo> getInitItemList(StickyItemImpl lastItem, int arrayResStr, int size) {
//            final String[] countryNames = mRecyclerView.getResources().getStringArray(arrayResStr);
//            ArrayList<StickyItemImpl> itemList = new ArrayList<>();
//            // Insert headers into list of items.
//            String lastHeader = lastItem != null ? lastItem.text.substring(0, 1) : "";
//            int sectionManagerType = StickyHeaderAdapter.LINEAR;
//            int headerCount = 0;
//            int lastItemPosition = lastItem != null ? lastItem.sectionFirstPosition() : 0;
//            int sectionFirstPosition = lastItemPosition;
//            for (int i = 0; i < countryNames.length; i++) {
//                String header = countryNames[i].substring(0, 1);
//                if (!TextUtils.equals(lastHeader, header)) { // is need to create new group
//                    // Insert new header view and update section data.
////                sectionManager = (sectionManager + 1) % 2;
//                    sectionFirstPosition = size + itemList.size();
//                    lastHeader = header;
//                    itemList.add(getItem(ViewType.TYPE_HEADER, countryNames[i], sectionManagerType, sectionFirstPosition));
//                }
//                itemList.add(getItem(ViewType.TYPE_CONTENT, countryNames[i], sectionManagerType, sectionFirstPosition));
//            }
//            return itemList;
//        }

        private List<AccMgtTransactionHistoryInfo> getInitItemList(String prefixList, int arrayResStr) {
            final String[] countryNames = mRecyclerView.getResources().getStringArray(arrayResStr);
            ArrayList<AccMgtTransactionHistoryInfo> mItems = new ArrayList<>();
            // Insert headers into list of items.
            for (int i = 0; i < countryNames.length; i++) {
                int size = new Random().nextInt(5);
                long date = getDateTest();
                for (int j = 0; j < 3; j++) {
                    AccMgtTransactionHistoryInfo accMgtTransactionHistoryInfo = new AccMgtTransactionHistoryInfo();
                    accMgtTransactionHistoryInfo.transactionDescriptionOne = prefixList + "-" + (i == 0 ? "End" + countryNames[i] + "--- " + j : countryNames[i] + "--- " + j);
                    accMgtTransactionHistoryInfo.transactionDateTime = date;
                    accMgtTransactionHistoryInfo.generateGroupDisplay();
                    mItems.add(accMgtTransactionHistoryInfo);
                }
            }
            return mItems;
        }

        private long getDateTest() {
            dateTest--;
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, dateTest);
            return calendar.getTimeInMillis();
        }
    }
}
