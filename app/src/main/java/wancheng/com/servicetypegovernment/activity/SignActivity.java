package wancheng.com.servicetypegovernment.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.bean.TopBean;
import wancheng.com.servicetypegovernment.view.LinePathView;

public class SignActivity extends BaseActivity {


    private Button clear1;
    private Button save1;
    private LinePathView mPathView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_sign);
        clear1=(Button)findViewById(R.id.clear1);
        save1=(Button)findViewById(R.id.save1);
        mPathView=(LinePathView)findViewById(R.id.line_path_view);
        setResult(50);
        //设置保存监听
        save1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {    if (mPathView.getTouched()) {

                    try {

                        File destDir = new File("/sdcard/Wancheng/Signs/");
                        if (!destDir.exists()) {
                            destDir.mkdirs();
                        }
                        String path="/sdcard/Wancheng/Signs/"+System.currentTimeMillis()+".png";
                        mPathView.save(path, true, 10);
                        Intent intent = new Intent();
                        intent.putExtra("path", path);
                        setResult(100,intent);
                        finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {

                    Toast.makeText(SignActivity.this, "您没有签名~", Toast.LENGTH_SHORT).show();
                }
            }
        });
        clear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPathView.clear();
            }
        });
        TopBean topBean=new TopBean("签字版","返回","",true,false);
        getTopView(topBean);

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
    protected void onDestroy() {

        super.onDestroy();
    }
}
