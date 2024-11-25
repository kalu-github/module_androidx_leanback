# 避免混淆泛型
-keepattributes Signature
# bean
-keep class lib.kalu.leanback.presenter.bean.**{*;}
-keep class * extends lib.kalu.leanback.presenter.bean.TvEpisodesPlusItemBean  {
      public <init>();
 }
-keep class * extends lib.kalu.leanback.presenter.bean.TvEpisodesGridItemBean  {
      public <init>();
 }
# 接口
-keep public interface lib.kalu.leanback.presenter.impl.ListTvPresenterImpl{ *; }
-keep class * implements lib.kalu.leanback.presenter.impl.ListTvPresenterImpl{ *; }