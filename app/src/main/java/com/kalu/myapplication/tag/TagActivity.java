package com.kalu.myapplication.tag;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.kalu.myapplication.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lib.kalu.leanback.tags.TagsLayout;
import lib.kalu.leanback.tags.model.TagBean;

public class TagActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);

        HashMap<String, List<TagBean>> map = new HashMap<>();

        for (int j = 0; j < 5; j++) {

            ArrayList<TagBean> list = new ArrayList<>();
            for (int i = 0; i < 20; i++) {

                int finalI = i;
                TagBean model = new TagBean();
                model.setText(finalI == 0 ? "全部" : "=>" + finalI + "<=");
                list.add(model);
            }

            map.put(String.valueOf(j), list);
        }

        TagsLayout tagsLayout = findViewById(R.id.test_tags);
        tagsLayout.update(map);
    }
}