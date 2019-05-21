package work.cisst.com.gpsapp;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import work.cisst.com.gpsapp.entity.User;
import work.cisst.com.gpsapp.mapper.UserMapper;

public class MainActivity extends AppCompatActivity {

    //初始化页面
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //设置点击登录按钮事件
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText name = (EditText) findViewById(R.id.editText);
                EditText password = (EditText) findViewById(R.id.editText2);
                //判断用户名、密码是否为空
                if(name.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this,"请输入用户名",Toast.LENGTH_LONG).show();
                }else if(password.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this,"请输入密码",Toast.LENGTH_LONG).show();
                }else{
                    //从数据库中获取数据
                    UserMapper userMapper = new UserMapper();
                    User user = userMapper.getUserByName(name.getText().toString());
                    //判断密码是否正确
                    if(user.getId()==null){
                        Toast.makeText(MainActivity.this,"密码错误！",Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (user.getPassword().equals(password.getText().toString())) {
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, Map.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("id", user.getId());
                        bundle.putString("name", user.getName());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }else{
                        Toast.makeText(MainActivity.this,"密码错误！",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    //设置页面跳转方法
    public void gotoRegist(View view) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, Regist.class);
        startActivity(intent);
        finish();
    }



}
