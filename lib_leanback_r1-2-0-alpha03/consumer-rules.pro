# bean
-keep class lib.kalu.leanback.presenter.bean.**{*;}
-keep class * extends lib.kalu.leanback.presenter.bean.TvEpisodesPlusItemBean  {
      public <init>();
 }
-keep class * extends lib.kalu.leanback.presenter.bean.TvEpisodesGridItemBean  {
      public <init>();
 }
# 避免混淆泛型
-keepattributes Signature
# 接口
-keep public interface lib.kalu.leanback.presenter.impl.ListTvPresenterImpl{ *; }
-keep class * implements lib.kalu.leanback.presenter.impl.ListTvPresenterImpl{ *; }



# leanback
-keep class * extends android.widget.View.**  {
      public <methods>;
      public <fields>;
}

# leanback
-keep class androidx.leanback.widget.**  {
      public <methods>;
      public <fields>;
}

# bold
-keep class lib.kalu.leanback.bold.BoldTextView  {
      public <methods>;
      public <fields>;
}

# tab
-keep class lib.kalu.leanback.tab.TabLayout  {
      public <methods>;
      public <fields>;
}
-keep class lib.kalu.leanback.tab.model.**  {
      public <methods>;
      public <fields>;
}
-keep class lib.kalu.leanback.tab.listener.OnTabChangeListener  {
      public <methods>;
      public <fields>;
}

# tags
-keep class lib.kalu.leanback.tags.TagsLayout  {
      public <methods>;
      public <fields>;
}
-keep class lib.kalu.leanback.tags.model.**  {
      public <methods>;
      public <fields>;
}
-keep class lib.kalu.leanback.tags.listener.OnTagsChangeListener  {
      public <methods>;
      public <fields>;
}

# util
-keep class lib.kalu.leanback.util.LeanBackUtil  {
      public <methods>;
      public <fields>;
}

# web
-keep class lib.kalu.leanback.web.WebView  {
      public <methods>;
      public <fields>;
}
-keep class lib.kalu.leanback.web.WebView2  {
      public <methods>;
      public <fields>;
}