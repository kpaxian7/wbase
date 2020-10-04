package com.bnb.wbasemodule.base;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bnb.wbasemodule.R;
import com.bnb.wbasemodule.bean.BaseListBean;
import com.bnb.wbasemodule.utils.ListUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Author by wzl, Date on 2019/4/9.
 */
public abstract class ABaseRefreshAndLoadMoreActivity<B, A extends BaseQuickAdapter<B, BaseViewHolder>,
        P extends IPresenter> extends ABaseMvpActivity<P>
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
    protected void initView() {
        super.initView();
        mSwipe = findViewById(getSwipeRefreshLayoutId());
        mAdapter = initAdapter();
        initRecycler(findViewById(getRecyclerId()));
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
        mAdapter.setOnLoadMoreListener(() -> getData(), mRv);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            onItemChildClick(view, position);
        });
        mAdapter.setOnItemClickListener((adapter, view, position) -> onItemClick(view, position));
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        refreshData();
    }

    protected void refreshData() {
        mCurrentIndex = INIT_INDEX;
        mAdapter.setEnableLoadMore(false);
        if (mSwipe != null) mSwipe.setRefreshing(true);
        getData();
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this);
    }

    protected void onItemChildClick(View v, int pos) {

    }

    @Override
    public void getListDataSuc(List<B> dataArr) {
        if (mSwipe != null) mSwipe.setRefreshing(false);
        if (!ListUtils.isEmpty(dataArr)) {
            if (mCurrentIndex == INIT_INDEX) {
                mAdapter.setNewData(dataArr);
            } else {
                mAdapter.addData(dataArr);
                mAdapter.loadMoreComplete();
            }
            mAdapter.setEnableLoadMore(true);
            mCurrentIndex++;
        } else if (mCurrentIndex == INIT_INDEX) {
            showInitNoDataView();
        } else {
            mAdapter.loadMoreEnd();
        }
    }

    private void showInitNoDataView() {
        mAdapter.setNewData(null);
        if (mEmptyView != null) {
            mAdapter.setEmptyView(mEmptyView);
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

    protected View getEmptyView() {
        return null;
    }

    protected int getEmptyViewId() {
        return -1;
    }

    protected abstract A initAdapter();

    protected abstract int getRecyclerId();

    protected abstract int getSwipeRefreshLayoutId();

    protected abstract void onItemClick(View v, int pos);

    protected abstract void getData();
}
