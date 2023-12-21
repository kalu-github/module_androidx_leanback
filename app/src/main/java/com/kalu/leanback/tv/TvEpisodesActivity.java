package com.kalu.leanback.tv;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
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
import com.kalu.leanback.R;

import java.util.LinkedList;

import lib.kalu.leanback.list.LeanBackVerticalGridView;
import lib.kalu.leanback.presenter.ListTvEpisodesDoubleRowPresenter;
import lib.kalu.leanback.presenter.bean.TvEpisodesPlusItemBean;
import lib.kalu.leanback.util.LeanBackUtil;

public class TvEpisodesActivity extends AppCompatActivity {

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.e("TvEpisodesActivity", "dispatchKeyEvent => aition = " + event.getAction() + ", keyCode = " + event.getKeyCode() + ", focusView = " + getCurrentFocus());
        return super.dispatchKeyEvent(event);
    }

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

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.bottom).requestFocus();
            }
        }, 400);
    }

    private void showData1() {
        VerticalGridView verticalGridView = findViewById(R.id.list_content);
        TestPresenter presenter = verticalGridView.findPresenterFirst(TestPresenter.class);
        RecyclerView.ViewHolder holder = verticalGridView.findViewHolderAtFirst(TestPresenter.TestData.class);
        presenter.checkedPlayingPosition((ViewGroup) holder.itemView, 8);
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
//        for (int i = 0; i < 80000; i++) {
        for (int i = 0; i < 1; i++) {
            TvEpisodesPlusItemBean bean = new TvEpisodesPlusItemBean();
            data.add(bean);
        }
        VerticalGridView verticalGridView = findViewById(R.id.list_content);
        RecyclerView.Adapter adapter = verticalGridView.getAdapter();
        ObjectAdapter objectAdapter = ((ItemBridgeAdapter) adapter).getAdapter();
        ((ArrayObjectAdapter) objectAdapter).add(data);
    }

    private class SelectorPresenter extends lib.kalu.leanback.selector.BasePresenterSelector {

        @Override
        protected void init() {
            addPresenterCustom(TestPresenter.TestData.class, new TestPresenter());
        }
    }

    static class TestPresenter extends ListTvEpisodesDoubleRowPresenter<TvEpisodesPlusItemBean> {

        @Override
        public void onBindHolderRange(@NonNull Context context, @NonNull View v, @NonNull TvEpisodesPlusItemBean item, @NonNull int position) {
            Log.e("TvEpisodesActivity", "onBindHolderRange => position = " + position + ", t = " + new Gson().toJson(item));
            // playing
            if (item.isPlaying()) {
                ((TextView) v).setText("playing:" + item.getRangeStart() + "-" + item.getRangeEnd());
            }
            // checked
            else if (item.isChecked()) {
                ((TextView) v).setText("checked:" + item.getRangeStart() + "-" + item.getRangeEnd());
            }
            // sample
            else {
                ((TextView) v).setText(item.getRangeStart() + "-" + item.getRangeEnd());
            }

            // focus
            if (item.isFocus()) {
                ((TextView) v).setTextColor(Color.WHITE);
                ((TextView) v).setBackgroundResource(R.drawable.bg_focus);
            }
            // playing
            else if (item.isPlaying()) {
                ((TextView) v).setTextColor(Color.BLUE);
                ((TextView) v).setBackgroundResource(R.drawable.bg);
            }
            // checked
            else if (item.isChecked()) {
                ((TextView) v).setTextColor(Color.RED);
                ((TextView) v).setBackgroundResource(R.drawable.bg);
            }
            // normal
            else {
                ((TextView) v).setTextColor(Color.BLACK);
                ((TextView) v).setBackgroundResource(R.drawable.bg);
            }
        }

        @Override
        public void onBindHolderEpisode(@NonNull Context context, @NonNull View v, @NonNull TvEpisodesPlusItemBean item, @NonNull int position) {
            Log.e("TvEpisodesActivity", "onBindHolderEpisode => position = " + position + ", t = " + new Gson().toJson(item));
//            ((TextView) v).setText(item.getEpisodeIndex() + "-" + item.getEpisodeMax());

            // playing
            if (item.isPlaying()) {
                ((TextView) v).setText("playing:" + item.getEpisodeIndex());
            }
            // checked
            else if (item.isChecked()) {
                ((TextView) v).setText("checked:" + item.getEpisodeIndex());
            }
            // sample
            else {
                ((TextView) v).setText(item.getEpisodeIndex() + "");
            }

            // focus
            if (item.isFocus()) {
                ((TextView) v).setTextColor(Color.WHITE);
                ((TextView) v).setBackgroundResource(R.drawable.bg_focus);
            }
            // playing
            else if (item.isPlaying()) {
                ((TextView) v).setTextColor(Color.BLUE);
                ((TextView) v).setBackgroundResource(R.drawable.bg);
            }
            // checked
            else if (item.isChecked()) {
                ((TextView) v).setTextColor(Color.RED);
                ((TextView) v).setBackgroundResource(R.drawable.bg);
            }
            // normal
            else {
                ((TextView) v).setTextColor(Color.BLACK);
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
