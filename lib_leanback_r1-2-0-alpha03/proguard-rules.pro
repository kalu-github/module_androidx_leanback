# 检测并移除没有用到的类，变量，方法和属性；
-dontshrink
# 优化代码，非入口节点类会加上private/static/final, 没有用到的参数会被删除，一些方法可能会变成内联代码
-dontoptimize

# 指定外部模糊字典
-obfuscationdictionary proguard-rules-dict-mini.txt
# 指定class模糊字典
-classobfuscationdictionary proguard-rules-dict-mini.txt
# 指定package模糊字典
-packageobfuscationdictionary proguard-rules-dict-mini.txt

# leanback
-keep class androidx.leanback.widget.**  {
    *;
}
-keep class androidx.leanback.widget.Presenter$* {
    *;
}

# bold
-keep class lib.kalu.leanback.bold.BoldTextView  {
    protected <methods>;
    public <methods>;
    public <fields>;
}

# clazz
-keep class lib.kalu.leanback.clazz.ClassLayoutImpl  {
    protected <methods>;
    public <methods>;
    public <fields>;
}
-keep class lib.kalu.leanback.clazz.ClassBean  {
    protected <methods>;
    public <methods>;
    public <fields>;
}
-keep class lib.kalu.leanback.clazz.ClassImageSpan  {
    protected <methods>;
    public <methods>;
    public <fields>;
}
-keep class lib.kalu.leanback.clazz.ClassScrollView  {
    protected <methods>;
    public <methods>;
    public <fields>;
}
-keep class lib.kalu.leanback.clazz.OnCheckedChangeListener  {
    protected <methods>;
    public <methods>;
    public <fields>;
}

# clock
-keep class lib.kalu.leanback.clock.TextClock  {
    protected <methods>;
    public <methods>;
    public <fields>;
}

# flexbox
-keep class lib.kalu.leanback.flexbox.**  {
    *;
}

# list
-keep class lib.kalu.leanback.list.**  {
    *;
}

# loading
-keep class lib.kalu.leanback.loading.LoadingView  {
    protected <methods>;
    public <methods>;
    public <fields>;
}

# marquee
-keep class lib.kalu.leanback.marquee.MarqueeTextView  {
    protected <methods>;
    public <methods>;
    public <fields>;
}
# page
-keep class lib.kalu.leanback.page.PageView  {
    protected <methods>;
    public <methods>;
    public <fields>;
}
-keep class lib.kalu.leanback.page.OnPageChangeListener  {
    protected <methods>;
    public <methods>;
    public <fields>;
}

# plus
-keep class lib.kalu.leanback.plus.TextViewPlus  {
    protected <methods>;
    public <methods>;
    public <fields>;
}

# popu
-keep class lib.kalu.leanback.popu.PopuTextView  {
    protected <methods>;
    public <methods>;
    public <fields>;
}
-keep class lib.kalu.leanback.popu.PopuMarqueeTextView  {
    protected <methods>;
    public <methods>;
    public <fields>;
}

# presenter
-keep class lib.kalu.leanback.presenter.**  {
    *;
}
-keep class lib.kalu.leanback.presenter.ListTvEpisodesDoubleRowPresenter$* {
    *;
}


# presenter
-keep class lib.kalu.leanback.radio.RadioButton  {
    protected <methods>;
    public <methods>;
    public <fields>;
}
-keep class lib.kalu.leanback.radio.RadioGroupHorizontal  {
    protected <methods>;
    public <methods>;
    public <fields>;
}

# recycler
-keep class lib.kalu.leanback.recycler.RecyclerView  {
    protected <methods>;
    public <methods>;
    public <fields>;
}

# round
-keep class lib.kalu.leanback.round.**  {
    *;
}

# scale
-keep class lib.kalu.leanback.scale.**  {
    *;
}

# selector
-keep class lib.kalu.leanback.selector.**  {
    protected <methods>;
    public <methods>;
    public <fields>;
}

# tab
-keep class lib.kalu.leanback.tab.TabLayout  {
    protected <methods>;
    public <methods>;
    public <fields>;
}
-keep class lib.kalu.leanback.tab.model.**  {
    protected <methods>;
    public <methods>;
    public <fields>;
}
-keep class lib.kalu.leanback.tab.listener.OnTabChangeListener  {
    public <methods>;
}
-keep class lib.kalu.leanback.tab.listener.OnTabCheckedListener  {
    public <methods>;
}
-keep class lib.kalu.leanback.tab.listener.OnTabUnCheckedListener  {
    public <methods>;
}

# tags
-keep class lib.kalu.leanback.tags.TagsLayout  {
    protected <methods>;
    public <methods>;
    public <fields>;
}
-keep class lib.kalu.leanback.tags.model.**  {
    protected <methods>;
    public <methods>;
    public <fields>;
}
-keep class lib.kalu.leanback.tags.listener.OnTagsChangeListener  {
    protected <methods>;
    public <methods>;
    public <fields>;
}


# util
-keep class lib.kalu.leanback.util.LeanBackUtil  {
    protected <methods>;
    public <methods>;
    public <fields>;
}

# web
-keep class lib.kalu.leanback.web.WebView  {
    protected <methods>;
    public <methods>;
    public <fields>;
}
-keep class lib.kalu.leanback.web.WebView2  {
    protected <methods>;
    public <methods>;
    public <fields>;
}