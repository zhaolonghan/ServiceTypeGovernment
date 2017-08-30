package wancheng.com.servicetypegovernment.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.adspter.NewsAdspter;
import wancheng.com.servicetypegovernment.bean.TopBean;

/**
 * test
 */
public class NewsInfoActivity extends BaseActivity {
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsinfo);
        Intent intent=getIntent();
        int index= intent.getIntExtra("index", 0);
        String id=intent.getStringExtra("ids");
        getJumpFoot(this, index);
        String titlename="详情";
        if(index==4){
            titlename="个人中心";
            RelativeLayout layout =  (RelativeLayout)findViewById(R.id.newsinfo);
            ((TextView)layout.getChildAt(1)).setText("");
            ((TextView)layout.getChildAt(2)).setText("");
            ((TextView)layout.getChildAt(3)).setText("");

        }
        TopBean topBean=new TopBean(titlename,"返回","",true,true);
        getTopView(topBean);
       tv_left.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               onBackPressed();
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
    protected void onStop() {
        super.onStop();
        this.finish();
    }
}
