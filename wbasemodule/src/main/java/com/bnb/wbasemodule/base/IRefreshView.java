package com.bnb.wbasemodule.base;

import java.util.List;

public interface IRefreshView<D> {

    void getListDataSuc(List<D> dataArr);

    void getListDataFail();
}
