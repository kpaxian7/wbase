package com.bnb.wbasemodule.base;

import com.bnb.wbasemodule.bean.BaseListBean;

public interface IRefreshView<D> {

    void getListDataSuc(BaseListBean<D> bean);

    void getListDataFail();
}
