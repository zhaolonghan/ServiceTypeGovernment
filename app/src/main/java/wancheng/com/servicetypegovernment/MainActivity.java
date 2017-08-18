package wancheng.com.servicetypegovernment;


import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import wancheng.com.servicetypegovernment.activity.BaseActivity;
import wancheng.com.servicetypegovernment.activity.IndexActivity;
import wancheng.com.servicetypegovernment.util.Sha1;

public class MainActivity extends BaseActivity {

    Button btnLogin;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogin =(Button)findViewById(R.id.btn_login);
        final EditText ed=(EditText)findViewById(R.id.editText1);
        username=ed.getText().toString();

        final EditText ed2=(EditText)findViewById(R.id.editText2);


        btnLogin.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {


                Toast.makeText(MainActivity.this, "登录ing", Toast.LENGTH_SHORT).show();
                Log.e("11111111111111111", Sha1.sHA1(MainActivity.this));
                Intent intent = new Intent();
                intent.putExtra("username", username);
                intent.setClass(MainActivity.this, IndexActivity.class);
                MainActivity.this.startActivity(intent);
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


}
