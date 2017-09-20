package wancheng.com.servicetypegovernment.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.bean.TopBean;

public class ContextDetailActivity extends BaseActivity {

    private WebView web_view;
    public int jumpType=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context_detail);
        web_view=(WebView)findViewById(R.id.web_view);
        Intent intent=getIntent();
        TopBean topBean=new TopBean(intent.getStringExtra("title"),"返回","",true, false);
        getTopView(topBean);
        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.getSettings().setUseWideViewPort(true);//web1就是你自己定义的窗口对象。
        web_view.getSettings().setLoadWithOverviewMode(true);

        //加载需要显示的网页
        web_view.loadUrl(intent.getStringExtra("url"));
        //设置Web视图
        web_view.setWebViewClient(new WebViewClient() {
            // 这个方法在用户试图点开页面上的某个链接时被调用
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null) {
                    // 如果想继续加载目标页面则调用下面的语句
                    view.loadUrl(url);
                    jumpType = 1;
                    // 如果不想那url就是目标网址，如果想获取目标网页的内容那你可以用HTTP的API把网页扒下来。
                }
                // 返回true表示停留在本WebView（不跳转到系统的浏览器）
                return true;
            }
        });
        tv_left.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (jumpType == 1) {
                    web_view.goBack(); //goBack()表示返回WebView的上一页面
                    jumpType = 0;
                } else {
                    ContextDetailActivity.this.finish();
                }

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    //设置回退
    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && web_view.canGoBack()) {
            if(jumpType==1){
                web_view.goBack(); //goBack()表示返回WebView的上一页面
                jumpType=0;
                return true;
            }
            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }
}
