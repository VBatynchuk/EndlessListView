package com.batynchuk.endlesslistview;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;

import com.batynchuk.endlesslistview.adapters.UserAdapter;
import com.batynchuk.endlesslistview.data.DbHandler;
import com.batynchuk.endlesslistview.models.User;
import com.batynchuk.endlesslistview.utils.Constants;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements AbsListView.OnScrollListener {

    @Bind(R.id.lv_users)
    ListView mLvUsers;

    private boolean mIsLoading;
    private boolean mIsScrollingUp;

    private int mLastFirstVisibleItem;

    private int mTopBound;
    private int mBottomBound;
    private int mCleanTop;
    private int mCleanBottom;
    private int mLoadNext;
    private int mLoadPrev;

    private UserAdapter mUsersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        mIsLoading = true;

        mUsersAdapter = new UserAdapter(MainActivity.this);
        mUsersAdapter.setUserCount(DbHandler.getInstance(this).getUserCount());
        mUsersAdapter.setUsers(DbHandler.getInstance(MainActivity.this).getUserList(0), 0);

        mLvUsers.setAdapter(mUsersAdapter);
        mLvUsers.setOnScrollListener(this);

        mIsLoading = false;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        if (firstVisibleItem > mLastFirstVisibleItem) {
            mIsScrollingUp = false;
        } else if (firstVisibleItem < mLastFirstVisibleItem) {
            mIsScrollingUp = true;
        }

        mLastFirstVisibleItem = firstVisibleItem;

        if (!mIsLoading)
            loadOnScroll(firstVisibleItem, visibleItemCount);
    }


    private void loadOnScroll(int firstVisiblePosition, int visibleItemCount) {
        mTopBound = (firstVisiblePosition - Constants.LOAD_OFFSET) / Constants.PAGE_LIMIT;
        mBottomBound = (firstVisiblePosition + visibleItemCount +
                Constants.LOAD_OFFSET) / Constants.PAGE_LIMIT;

        if (mTopBound != mCleanTop)
            cleanTop();

        if (mBottomBound != mCleanBottom && mBottomBound != 0)
            cleanBottom();

        if (mBottomBound != mLoadNext)
            loadNext();

        if (mTopBound != mLoadPrev)
            loadPrev();
    }

    private void cleanTop() {
        if (!mIsScrollingUp) {
            Log.d("TAG1", "cleanUp page " + mCleanTop);
            mUsersAdapter.cleanUp(mCleanTop);
        }
        mCleanTop = mTopBound;
    }

    private void cleanBottom() {
        if (mIsScrollingUp) {
            Log.d("TAG1", "cleanUp page " + mCleanBottom);
            mUsersAdapter.cleanUp(mCleanBottom);
        }
        mCleanBottom = mBottomBound;
    }

    private void loadNext() {
        mLoadNext = mBottomBound;
        if (!mIsScrollingUp) {
            Log.d("TAG1", "1-> load page " + mLoadNext);
            getNextPageAsync(mLoadNext);
        }
    }

    private void loadPrev() {
        mLoadPrev = mTopBound;
        if (mIsScrollingUp) {
            Log.d("TAG1", "2 -> load page " + mLoadPrev);
            getNextPageAsync(mLoadPrev);
        }
    }

    private void getNextPageAsync(final int page) {
        mIsLoading = true;
        new AsyncTask<Integer, Void, List<User>>() {
            @Override
            protected List<User> doInBackground(Integer... params) {
                return DbHandler.getInstance(MainActivity.this).getUserList(params[0]);
            }

            @Override
            protected void onPostExecute(List<User> users) {
                mUsersAdapter.setUsers(users, page);
                mIsLoading = false;
            }
        }.execute(page);
    }
}
