package com.example.HSF3D;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button newAccount = findViewById(R.id.button2);
        Button logIn = findViewById(R.id.button);

        //新建用户界面
        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =
                        new Intent(MainActivity.this, NewAccountActivity.class);
                startActivity(intent);
            }
        });

        //登录界面
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText account = findViewById(R.id.accountNumber);
                EditText password = findViewById(R.id.passwordNumber);

                final String managerId = "111111";
                final String managerPassword = "111111";

                if (account.getText().toString().equals(managerId) &&
                        password.getText().toString().equals(managerPassword)) {
                    Intent intent =
                            new Intent(MainActivity.this, ManagerActivity.class);
                    startActivity(intent);
                }else if (checkAccount(account.getText().toString(), password.getText().toString())) {
                Intent intent =
                        new Intent(MainActivity.this, ChoosingActivity.class);
                startActivity(intent);
                }
            }
        });

    }

    private boolean checkAccount(String account, String password) {
        String name = getString(R.string.MY_DATA);//文件名
        SharedPreferences shp = getSharedPreferences(name, Context.MODE_PRIVATE);

        if (account.length() == 0){
            Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
        } else if (shp.getString(account, null) == null) {
            Toast.makeText(this, "该账号不存在", Toast.LENGTH_SHORT).show();
        } else if (password.length() == 0) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
        } else if (shp.getString(account, null).equals(password)) {
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
        }

        return false;
    }
}