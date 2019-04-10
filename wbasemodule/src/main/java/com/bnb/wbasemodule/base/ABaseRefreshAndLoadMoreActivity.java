package com.bnb.wbasemodule.base;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bnb.wbasemodule.R;
import com.bnb.wbasemodule.bean.BaseListBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Author by wzl, Date on 2019/4/9.
 */
public abstract class ABaseRefreshAndLoadMoreActivity<B, A extends BaseQuickAdapter<B, BaseViewHolder>,
        P extends ABasePresenter> extends ABaseMvpActivity<P>
        implements IRefreshView<B> {

    private int mDefEmptyId = R.layout.layout_base_empty;
    public static final int NO_EMPTY_ID = -1;

    protected SwipeRefreshLayout mSwipe;
    protected RecyclerView mRv;
    protected A mAdapter;

    private View mEmptyView;

    public static final int INIT_INDEX = 1;
    protected int mCurrentIndex = INIT_INDEX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRecycler(getRecycler());
        mSwipe = getSwipeRefreshLayout();
        mAdapter = initAdapter();
        mAdapter.bindToRecyclerView(mRv);
        mAdapter.setHeaderAndEmpty(true);
        if (getEmptyView() != null) {
            mEmptyView = getEmptyView();
        } else if (getEmptyViewId() != NO_EMPTY_ID) {
            mEmptyView = View.inflate(this, getEmptyViewId(), null);
        } else {
            mEmptyView = View.inflate(this, mDefEmptyId, null);
        }
    }

    protected void initRecycler(RecyclerView rv) {
        mRv = rv;
        rv.setLayoutManager(getLayoutManager());
    }

    @Override
    protected void initListener() {
        super.initListener();
        mSwipe.setOnRefreshListener(() -> {
            refreshData();
        });
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            onItemChildClick(view, position);
        });
        mAdapter.setOnItemClickListener((adapter, view, position) -> onItemClick(view, position));
    }

    protected void refreshData() {
        mCurrentIndex = INIT_INDEX;

    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this);
    }

    protected void onItemChildClick(View v, int pos) {

    }

    @Override
    public void getListDataSuc(BaseListBean<B> bean) {
        if (mSwipe != null) mSwipe.setRefreshing(false);
        if (bean != null && bean.getData() != null) {

        }
    }

    @Override
    public void getListDataFail() {
        if (mSwipe != null) mSwipe.setRefreshing(false);
        if (mCurrentIndex == INIT_INDEX) {
            mAdapter.setEmptyView(mEmptyView);
        } else {
            mAdapter.loadMoreFail();
        }
    }

    abstract A initAdapter();

    abstract RecyclerView getRecycler();

    abstract SwipeRefreshLayout getSwipeRefreshLayout();

    abstract View getEmptyView();

    abstract int getEmptyViewId();

    abstract void onItemClick(View v, int pos);
}
