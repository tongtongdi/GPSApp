package work.cisst.com.gpsapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import work.cisst.com.gpsapp.entity.User;
import work.cisst.com.gpsapp.mapper.UserMapper;

public class Regist extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ((EditText)findViewById(R.id.editText3)).getText().toString();
                String password1 = ((EditText)findViewById(R.id.editText4)).getText().toString();
                String password2 = ((EditText)findViewById(R.id.editText6)).getText().toString();
                UserMapper userMapper = new UserMapper();
                User user = userMapper.getUserByName(name);
                if(user.getId()!=null){
                    Toast.makeText(Regist.this,"用户名已被占用！",Toast.LENGTH_LONG).show();
                    return;
                }
                if(password1.length()<6){
                    Toast.makeText(Regist.this,"密码最少6位数！",Toast.LENGTH_LONG).show();
                    return;
                }
                if (name.equals("") || password1.equals("") || password2.equals("")) {
                    Toast.makeText(Regist.this,"请完善信息",Toast.LENGTH_LONG).show();
                }else if(!password1.equals(password2)){
                    Toast.makeText(Regist.this,"两次密码不一致",Toast.LENGTH_LONG).show();
                }else{
                    userMapper.addUser(name,password1);
                    Toast.makeText(Regist.this,"注册成功",Toast.LENGTH_LONG).show();
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent();
                    intent.setClass(Regist.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public void gotoLogin(View view) {
        Intent intent = new Intent();
        intent.setClass(Regist.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
