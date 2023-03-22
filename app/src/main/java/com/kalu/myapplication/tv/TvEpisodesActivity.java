package com.kalu.myapplication.tv;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ItemBridgeAdapter;
import androidx.leanback.widget.ObjectAdapter;
import androidx.leanback.widget.VerticalGridView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.kalu.myapplication.R;

import java.util.LinkedList;

import lib.kalu.leanback.list.LeanBackVerticalGridView;
import lib.kalu.leanback.presenter.ListTvEpisodesPlusPresenter2;
import lib.kalu.leanback.presenter.bean.TvEpisodesPlusItemBean;
import lib.kalu.leanback.util.LeanBackUtil;

public class TvEpisodesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_episodes);
        LeanBackUtil.setLogger(true);
        setAdapter1();
        setData1();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                showData1();
            }
        }, 100);

//        findViewById(R.id.show).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showData1();
//            }
//        });
    }

    private final void setAdapter1() {
        SelectorPresenter selectorPresenter = new SelectorPresenter();
        ArrayObjectAdapter objectAdapter = new ArrayObjectAdapter(selectorPresenter);
        ItemBridgeAdapter bridgeAdapter = new ItemBridgeAdapter(objectAdapter);
        LeanBackVerticalGridView gridView = findViewById(R.id.list_content);
        gridView.setAdapter(bridgeAdapter);
    }

    private final void setData1() {
        TestPresenter.TestData data = new TestPresenter.TestData();
        for (int i = 0; i < 111; i++) {
            TvEpisodesPlusItemBean bean = new TvEpisodesPlusItemBean();
            data.add(bean);
        }
        VerticalGridView verticalGridView = findViewById(R.id.list_content);
        RecyclerView.Adapter adapter = verticalGridView.getAdapter();
        ObjectAdapter objectAdapter = ((ItemBridgeAdapter) adapter).getAdapter();
        ((ArrayObjectAdapter) objectAdapter).add(data);
    }

    private final void showData1() {
        VerticalGridView verticalGridView = findViewById(R.id.list_content);
        TestPresenter presenter = verticalGridView.getPresenter(TestPresenter.class);
        RecyclerView.ViewHolder holder = verticalGridView.findViewHolderForAdapterObject(TestPresenter.TestData.class);
        presenter.showData((ViewGroup) holder.itemView, 0);
    }

    private class SelectorPresenter extends lib.kalu.leanback.selector.BasePresenterSelector {

        @Override
        protected void init() {
            addPresenterCustom(TestPresenter.TestData.class, new TestPresenter());
        }
    }

    static class TestPresenter extends ListTvEpisodesPlusPresenter2<TvEpisodesPlusItemBean> {

        @Override
        public void onBindHolderRange(@NonNull Context context, @NonNull View v, @NonNull TvEpisodesPlusItemBean item, @NonNull int position) {
            Log.e("TvEpisodesActivity", "onBindHolderRange => position = " + position + ", t = " + new Gson().toJson(item));
            ((TextView) v).setText(item.getRangeStart() + "-" + item.getRangeEnd());
            // focus
            if (item.isFocus()) {
                ((TextView) v).setBackgroundResource(R.drawable.bg_focus);
            }
            // checked
            else if (item.isChecked()) {
                ((TextView) v).setBackgroundResource(R.drawable.bg_checked);
            }
            // normal
            else {
                ((TextView) v).setBackgroundResource(R.drawable.bg);
            }
        }

        @Override
        public void onBindHolderEpisode(@NonNull Context context, @NonNull View v, @NonNull TvEpisodesPlusItemBean item, @NonNull int position) {
            Log.e("TvEpisodesActivity", "onBindHolderEpisode => position = " + position + ", t = " + new Gson().toJson(item));
            ((TextView) v).setText(item.getEpisodeIndex() + "-" + item.getEpisodeMax());
            // focus
            if (item.isFocus()) {
                ((TextView) v).setBackgroundResource(R.drawable.bg_focus);
            }
            // checked
            else if (item.isChecked()) {
                ((TextView) v).setBackgroundResource(R.drawable.bg_checked);
            }
            // normal
            else {
                ((TextView) v).setBackgroundResource(R.drawable.bg);
            }
        }

        @Override
        public int initEpisodeLayout() {
            return R.layout.activity_tv_episodes_item;
        }

        @Override
        public int initRangeLayout() {
            return R.layout.activity_tv_episodes_item;
        }

        @Override
        public int initRangeNum() {
            return 5;
        }

        @Override
        public int initEpisodeNum() {
            return 10;
        }

        @Override
        public int initRangeMarginTop(@NonNull Context context) {
            return 20;
        }

        @Override
        public int initRangePadding(@NonNull Context context) {
            return 40;
        }

        @Override
        public int initEpisodePadding(@NonNull Context context) {
            return 40;
        }

        static class TestData extends LinkedList<TvEpisodesPlusItemBean> {

        }
    }
}
