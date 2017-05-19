package com.mzth.huaruiloan.ui.activity.sub;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mzth.huaruiloan.R;
import com.mzth.huaruiloan.common.LocalSaveKey;
import com.mzth.huaruiloan.ui.activity.base.BaseBussActivity;
import com.mzth.huaruiloan.util.SharedPreferencesUtil;

/**
 * Created by Administrator on 2017/5/16.
 */

public class WebOrderActivity extends BaseBussActivity {
    private TextView tv_big_title;
    private ImageView iv_back;
    private WebView web_order;
    private ProgressBar pb;
    @Override
    protected void setCustomLayout(Bundle savedInstanceState) {
        super.setCustomLayout(savedInstanceState);
        _context = WebOrderActivity.this;
        setContentView(R.layout.web_order);
    }

    @Override
    protected void initView() {
        super.initView();
        //返回键
        iv_back = (ImageView) findViewById(R.id.iv_back);
        //标题
        tv_big_title = (TextView) findViewById(R.id.tv_big_title);
        web_order = (WebView) findViewById(R.id.web_order);
        pb=(ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    protected void initData() {
        super.initData();
        tv_big_title.setText("订单信息");
        initWebView();
        String userId= (String) SharedPreferencesUtil.getParam(_context, LocalSaveKey.USER_ID,"");
        web_order.loadUrl("http://1355.bibang110.com/api/send/pay?user_id="+userId);

    }

    @Override
    protected void BindComponentEvent() {
        super.BindComponentEvent();
        iv_back.setOnClickListener(myonclick);

    }
    /**
     * 初始化WebView的配置
     */
    private void initWebView() {
        WebSettings webSettings = web_order.getSettings();
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(true); //支持缩放
        /**
         * 用WebView显示图片，可使用这个参数
         * 设置网页布局类型：
         * 1、LayoutAlgorithm.NARROW_COLUMNS ： 适应内容大小
         * 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
         */
        webSettings.setUseWideViewPort(true);//适应屏幕 这行代码必须加
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setDefaultTextEncodingName("utf-8"); //设置文本编码
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//设置缓存模式
        webSettings.setJavaScriptEnabled(true);
        //添加Javascript调用java对象
        //web_order.addJavascriptInterface(new JavaScriptInterface(_context), "injectedObject");
        //该方法作用为当一个网页跳转另一个网页时，仍然在当前webview中显示
        web_order.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);
//				设置进度条隐藏
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
//				设置显示进度条
                pb.setVisibility(View.VISIBLE);
                pb.setProgress(0);
                pb.setMax(100);
            }

        });

        web_order.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                // TODO Auto-generated method stub
                super.onReceivedTitle(view, title);
//    			获取当前加载页面的标题
                setTitle(title);
            }
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                super.onProgressChanged(view, newProgress);
                pb.setProgress(newProgress);
                System.out.println("当前获取的进度:"+newProgress);
            }

        });
    }
    private View.OnClickListener myonclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_back://返回键
                    onBackPressed();
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {

//        if(web_order.canGoBack()){
//            web_order.goBack();//返回webview页面
//        }else{
            setResult(RESULT_OK);
            super.onBackPressed();
        //}
    }
}
