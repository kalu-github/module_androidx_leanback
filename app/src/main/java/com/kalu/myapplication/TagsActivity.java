package com.kalu.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class TagsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags);

        HashMap<String, List<TagsModel>> map = new HashMap<>();

        for (int j = 0; j < 5; j++) {

            ArrayList<TagsModel> list = new ArrayList<>();
            for (int i = 0; i < 20; i++) {

                int finalI = i;
                TagsModelTest model = new TagsModelTest() {
                    @Override
                    public int initId() {
                        return 0;
                    }

                    @Override
                    public String initText() {
                        return finalI == 0 ? "全部" : "=>" + finalI + "<=";
                    }

                };
                list.add(model);
            }

            map.put(String.valueOf(j), list);
        }

        TagsLayout tagsLayout = findViewById(R.id.test_tags);
        tagsLayout.update(map);
    }
}