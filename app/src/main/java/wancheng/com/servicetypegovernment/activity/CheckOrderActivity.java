package wancheng.com.servicetypegovernment.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.bean.TopBean;
import wancheng.com.servicetypegovernment.view.SlideShowView;

/**
 * test
 */
public class CheckOrderActivity extends BaseActivity {


    private Button btDetail;
    private Button btStartCheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkorder);

        btDetail=(Button)this.findViewById(R.id.bt_detail);
        btDetail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setClass(CheckOrderActivity.this, CompanyDetailActivity.class);
                CheckOrderActivity.this.startActivity(intent);
            }
        });
        btStartCheck=(Button)this.findViewById(R.id.bt_start_check);
        btStartCheck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setClass(CheckOrderActivity.this, CompanyCheckListActivity.class);
                CheckOrderActivity.this.startActivity(intent);
            }
        });
        Intent intent=getIntent();
        TopBean topBean=new TopBean(intent.getStringExtra("companyType"),"返回","",true,false);
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
}
