package wancheng.com.servicetypegovernment.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import wancheng.com.servicetypegovernment.R;
import wancheng.com.servicetypegovernment.bean.TopBean;

public class CompanyCheckListActivity extends BaseActivity {


    private Button bt_check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_check_list);
        bt_check=(Button)findViewById(R.id.bt_check);
        bt_check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Toast.makeText(CompanyCheckListActivity.this, " 跳转告知页面", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(CompanyCheckListActivity.this, InformActivity.class);
                CompanyCheckListActivity.this.startActivity(intent);
            }
        });
        TopBean topBean=new TopBean("执法检查","返回","",true,false);
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
