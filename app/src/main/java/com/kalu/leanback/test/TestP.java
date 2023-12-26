package com.kalu.leanback.test;

import lib.kalu.leanback.presenter.ListTvGridPresenter;

public class TestP extends ListTvGridPresenter<Object> {
    @Override
    protected int initLayout(int viewType) {
        return 0;
    }

    @Override
    protected int initSpan() {
        return 0;
    }
}
