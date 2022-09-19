package com.example.HSF3D;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class NewAccountActivity extends Activity {
    private final String TAG = "NewAccountActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newaccount_activity);

        TextView quitButton = findViewById(R.id.buttonquit);
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button buttonConfirm = findViewById(R.id.buttonconfirm);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText newAccount = findViewById(R.id.newaccount);
                EditText newPassword = findViewById(R.id.newpassword);
                EditText confirmNewPassword = findViewById(R.id.confirmnewpassword);

                String newAccountNum = newAccount.getText().toString();
                String newPasswordNum = newPassword.getText().toString();
                String confirmNewPasswordNum = confirmNewPassword.getText().toString();

                if (newAccountNum.length() == 0) {
                    Toast.makeText(NewAccountActivity.this,
                            "请输入新账号", Toast.LENGTH_SHORT).show();
                } else if (newPasswordNum.length() == 0) {
                    Toast.makeText(NewAccountActivity.this,
                            "请输入密码", Toast.LENGTH_SHORT).show();
                } else if (confirmNewPasswordNum.length() == 0) {
                    Toast.makeText(NewAccountActivity.this,
                            "请确认密码", Toast.LENGTH_SHORT).show();
                } else if (!newPasswordNum.equals(confirmNewPasswordNum)) {
                    Toast.makeText(NewAccountActivity.this,
                            "密码不正确", Toast.LENGTH_SHORT).show();
                } else {
                    MyData myData = new MyData(getApplicationContext());
                    myData.insertData(newAccountNum, newPasswordNum);
                    Toast.makeText(NewAccountActivity.this,
                            "账号创建成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}
