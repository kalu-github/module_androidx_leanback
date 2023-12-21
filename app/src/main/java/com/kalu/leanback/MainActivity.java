package com.kalu.leanback;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.kalu.leanback.tab.TabActivity;
import com.kalu.leanback.tag.TagActivity;
import com.kalu.leanback.tv.TvEpisodesActivity;

import lib.kalu.leanback.web.WebView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webView = findViewById(R.id.main_webview);
        webView.loadUrl("http://192.168.0.3:8080");

//        findViewById(R.id.button_tag).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), TagActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        findViewById(R.id.button_tab).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), TabActivity.class);
//                startActivity(intent);
//            }
//        });
//        findViewById(R.id.button_tv).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), TvEpisodesActivity.class);
//                startActivity(intent);
//            }
//        });
    }
}