package lib.kalu.leanback.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import lib.kalu.leanback.util.LeanBackUtil;

public class WebView extends android.webkit.WebView {
    /**********************/

    private OnInterceptShouldOverrideUrlUrlListener mOnInterceptShouldOverrideUrl;

    public WebView(@NonNull Context context) {
        super(context);
        initListener();
        initConfig();
        initSetting();
    }

    public WebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initListener();
        initConfig();
        initSetting();
    }

    public WebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initListener();
        initConfig();
        initSetting();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public WebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initListener();
        initConfig();
        initSetting();
    }

    public WebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
        initListener();
        initConfig();
        initSetting();
    }

    /**********/

    private void initListener() {
        try {
            setBackgroundColor(Color.parseColor("#171819"));
            setLongClickable(false);
            setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return false;
                }
            });
        } catch (Exception e) {
            LeanBackUtil.log("WebView => initListener => " + e.getMessage());
        }
    }

    private void initConfig() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.setAcceptThirdPartyCookies(this, true);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            // fix h5网页视频有声音没图像
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setLayerType(View.LAYER_TYPE_HARDWARE, null);
            }
            // drawing cache
            setDrawingCacheEnabled(false);
            // 设置是否显示水平滚动条
            setHorizontalScrollBarEnabled(false);
            // 设置垂直滚动条是否有叠加样式
            setVerticalScrollbarOverlay(false);
            // 设置滚动条的样式
            setScrollBarStyle(android.webkit.WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            setWebViewClient(new WebViewClient() {
                @Override
                public void onReceivedSslError(android.webkit.WebView view, SslErrorHandler handler, SslError error) {
                    super.onReceivedSslError(view, handler, error);
                    try {
                        handler.proceed();
                    } catch (Exception e) {
                        LeanBackUtil.log("WebView => initConfig => setWebViewClient => onReceivedSslError => " + e.getMessage());
                    }
                }

                @Override
                public boolean shouldOverrideUrlLoading(android.webkit.WebView webView, String url) {

                    try {
                        if (null == mOnInterceptShouldOverrideUrl)
                            throw new Exception("mOnInterceptShouldOverrideUrl warning: null");
                        boolean interceptUrl = mOnInterceptShouldOverrideUrl.onInterceptUrl(url);
                        if (!interceptUrl)
                            throw new Exception("interceptUrl error: false");
                    } catch (Exception e) {
                        LeanBackUtil.log("WebView => initConfig => setWebViewClient => shouldOverrideUrlLoading => " + e.getMessage());
                        webView.loadUrl(url);
                    }
                    return false;
                }

                @Override
                public void onPageFinished(android.webkit.WebView view, String url) {
                    super.onPageFinished(view, url);
                }
            });
            setWebChromeClient(new WebChromeClient() {

                @Override
                public void onProgressChanged(android.webkit.WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                }
            });
        } catch (Exception e) {
            LeanBackUtil.log("WebView => initConfig => " + e.getMessage());
        }
    }

    @SuppressLint("JavascriptInterface")
    private void initSetting() {
        try {
            WebSettings settings = getSettings();
            if (null == settings)
                throw new Exception("settings error: null");

            // 支持Javascript
            settings.setJavaScriptEnabled(true);
            // 支持通过JS打开新窗口
            settings.setJavaScriptCanOpenWindowsAutomatically(true);
            // 提高网页渲染的优先级
            settings.setRenderPriority(WebSettings.RenderPriority.HIGH);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
            } else {
                settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
            }

            // 启用还H5的地理定位服务
            settings.setGeolocationEnabled(true);
            // 设置定位的数据库路径，不设置无法定位
            try {
                String dir = getContext().getDir("database", Context.MODE_PRIVATE).getPath();
                settings.setGeolocationDatabasePath(dir);
            } catch (Exception e) {
            }

            // 缓存模式
            settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            // 开启 database storage API 功能
            settings.setDatabaseEnabled(false);
            // 缓存路径
            try {
                String path = getContext().getFilesDir().getAbsolutePath() + "webview_database_file";
                settings.setDatabasePath(path);
            } catch (Exception e) {
            }

            // 是否保存密码
            settings.setSavePassword(false);
            // 是否保存表单数据
            settings.setSaveFormData(false);
            // 开启 DOM storage API 功能
            settings.setDomStorageEnabled(true);
            // 设置可以访问文件
            settings.setAllowFileAccess(true);
            // 设置编码格式
            settings.setDefaultTextEncodingName("utf-8");
            // 支持缩放
            settings.setSupportZoom(false);
            // 设置WebView使用内置缩放机制时，是否展现在屏幕缩放控件上，默认true，展现在控件上
            settings.setDisplayZoomControls(false);
            settings.setAllowContentAccess(true);
            settings.setAllowFileAccess(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                settings.setAllowFileAccessFromFileURLs(true);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                settings.setAllowUniversalAccessFromFileURLs(true);
            }
            // 设置WebView是否使用其内置的变焦机制，该机制集合屏幕缩放控件使用，默认是false，不使用内置变焦机制
            settings.setBuiltInZoomControls(false);
            // 设置WebView是否使用其内置的变焦机制，该机制结合屏幕缩放控件使用，默认是false，不使用内置变焦机制
            settings.setAllowContentAccess(false);
            // 设置WebView是否使用预览模式加载界面, 缩放至屏幕的大小
            settings.setLoadWithOverviewMode(true);
            // 设置WebView中加载页面字体变焦百分比, 默认100, 整型数
            settings.setTextZoom(100);
            // 将图片调整到适合webview的大小
            settings.setUseWideViewPort(true);
            // 设置WebView是否通过手势触发播放媒体，默认是true，需要手势触发
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                settings.setMediaPlaybackRequiresUserGesture(false);
            }
            //设置webview支持插件
            settings.setPluginState(WebSettings.PluginState.ON);
            settings.setSupportMultipleWindows(true);// 新加
            // 设置WebView是否以http、https方式访问从网络加载图片资源，默认false
            settings.setBlockNetworkImage(false);
            // 不立即加载图片, 等页面加载完成后设置加载图片
            settings.setLoadsImagesAutomatically(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT);
            // 处理http 和 https 图片混合的问题
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
        } catch (Exception e) {
            LeanBackUtil.log("WebView => initSetting => " + e.getMessage());
        }
    }

    /*****************/

    @SuppressLint("JavascriptInterface")
    public final void addJavascriptInterface(@NonNull Object object) {
        try {
            if (null == object)
                throw new Exception("object error: " + null);
            removeJavascriptInterface("android");
            addJavascriptInterface(object, "android");
        } catch (Exception e) {
            LeanBackUtil.log("WebView => addJavascriptInterface => " + e.getMessage());
        }
    }

    public void setOnInterceptShouldOverrideUrlListener(@NonNull OnInterceptShouldOverrideUrlUrlListener l) {
        this.mOnInterceptShouldOverrideUrl = l;
    }

    public interface OnInterceptShouldOverrideUrlUrlListener {
        boolean onInterceptUrl(@NonNull String url);
    }
}
