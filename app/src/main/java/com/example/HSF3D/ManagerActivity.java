package com.example.HSF3D;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_activity);

        Button buttonQuit = findViewById(R.id.button);
        Button buttonClear = findViewById(R.id.buttonclear);
        Button buttonUpdate = findViewById(R.id.buttonupdate);
        Button buttonInsert = findViewById(R.id.buttoninsert);
        Button buttonDelete = findViewById(R.id.buttondelete);
        TextView content = findViewById(R.id.content);

        MyData myData = new MyData(getApplicationContext());
        myData.loadData(content);

        buttonQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //创建提示
                AlertDialog alertDialog =
                        new AlertDialog.Builder(ManagerActivity.this).create();
                alertDialog.setIcon(R.drawable.ic_launcher_foreground);
                alertDialog.setTitle("系统提示：");
                alertDialog.setMessage("是否清空所有已创建账号？");
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                        "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                        "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myData.clearData();
                        myData.loadData(content);
                    }
                });
                alertDialog.show();
            }
        });
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =
                        new Intent(ManagerActivity.this, NewAccountActivity.class);
                startActivity(intent);
                myData.loadData(content);
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ManagerActivity.this,
                        "此功能尚未开发", Toast.LENGTH_SHORT).show();
            }
        });
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ManagerActivity.this,
                        "此功能尚未开发", Toast.LENGTH_SHORT).show();
            }
        });

    }
}