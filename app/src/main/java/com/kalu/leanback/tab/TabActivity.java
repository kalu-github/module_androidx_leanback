package com.kalu.leanback.tab;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.appcompat.app.AppCompatActivity;

import com.kalu.leanback.R;

import java.util.ArrayList;

import lib.kalu.leanback.tab.TabLayout;
import lib.kalu.leanback.tab.listener.OnTabChangeListener;
import lib.kalu.leanback.tab.model.TabModel;
import lib.kalu.leanback.tab.model.TabModelImage;
import lib.kalu.leanback.tab.model.TabModelText;
import lib.kalu.leanback.util.LeanBackUtil;

public class TabActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        // log
        LeanBackUtil.setLogger(true);

        init();
//            findViewById(R.id.four).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    TabLayout tabLayout = findViewById(R.id.tab_plus);
//                    tabLayout.select(3, true, true);
//                }
//            });
//
//            findViewById(R.id.five).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    TabLayout tabLayout = findViewById(R.id.tab_plus);
//                    tabLayout.select(4, false, true);
//                }
//            });
//
//            findViewById(R.id.left1).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    TabLayout tabLayout = findViewById(R.id.tab_plus);
//                    tabLayout.left();
//                }
//            });
//
//            findViewById(R.id.left2).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    TabLayout tabLayout = findViewById(R.id.tab_plus);
//                    tabLayout.left(2);
//                }
//            });
//
//            findViewById(R.id.right1).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    TabLayout tabLayout = findViewById(R.id.tab_plus);
//                    tabLayout.right();
//                }
//            });
//
//            findViewById(R.id.right2).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    TabLayout tabLayout = findViewById(R.id.tab_plus);
//                    tabLayout.right(2);
//                }
//            });
    }

    private void init() {


        new Thread(new Runnable() {
            @Override
            public void run() {

                //
                SystemClock.sleep(200);

                //
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<Fragment> list0 = new ArrayList<>();
                        for (int i = 0; i < 20; i++) {
                            TabFragment fragment = new TabFragment();
                            fragment.setText("fragment => " + i);
                            list0.add(fragment);
                        }

                        ArrayList<TabModel> list = new ArrayList<>();
                        for (int i = 1; i <= 20; i++) {
                            TabModel temp;
                            if (i == 4) {
                                temp = new TabModelImage();
                                temp.setImageUrlNormal("http://129.211.42.21:80/img/public/2021/e7cffa9ddf154e4b95092f8fdc84a798.png");
                                temp.setImageUrlFocus("http://129.211.42.21:80/img/public/2021/4884e1f436b84f3fb767b0eff425ce45.png");
                                temp.setImageUrlChecked("http://129.211.42.21/img/public/2021/6079d10f913240ae8458bc68530fba11.png");
                                temp.setBackgroundColorNormal(Color.GREEN);
                                temp.setBackgroundColorFocus(Color.BLUE);
                                temp.setBackgroundColorChecked(Color.WHITE);
//                return new String[]{null, "2/test.9.png", null};
//                return new int[]{R.drawable.module_tablayout_ic_shape_background_normal, R.drawable.ic_test, R.drawable.module_tablayout_ic_shape_background_select};
                            } else {
                                temp = new TabModelText();
                                temp.setText("tab" + i);
                                temp.setTextColorNormal(Color.parseColor("#000000"));
                                temp.setTextColorFocus(Color.parseColor("#ffffff"));
                                temp.setTextColorChecked(Color.parseColor("#999999"));
                                temp.setBackgroundColorNormal(Color.parseColor("#bbbbbb"));
                                temp.setBackgroundColorFocus(Color.parseColor("#ababab"));
                                temp.setBackgroundColorChecked(Color.parseColor("#ffffff"));
//                return new String[]{null, "2/test.9.png", null};
//                return new int[]{R.drawable.module_tablayout_ic_shape_background_normal, R.drawable.ic_test, R.drawable.module_tablayout_ic_shape_background_select};
                            }
                            list.add(temp);
                        }

                        TabLayout tabLayout = findViewById(R.id.tab_plus);
                        tabLayout.update(list);
                        tabLayout.setOnTabChangeListener(new OnTabChangeListener() {
                            @Override
                            public void onChecked(int position, int old) {

                            }
                        });
                    }
                });
            }
        }).start();

    }
}