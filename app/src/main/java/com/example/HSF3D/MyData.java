package com.example.HSF3D;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

public class MyData {
    private Context context;
    public MyData(Context context) {
        this.context = context;
    }

    public void insertData(String inputUsername, String inputPassword) {
        String name = context.getResources().getString(R.string.MY_DATA);//文件名
        SharedPreferences shp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shp.edit();

        if (shp.getString(inputUsername, null) == null) {
            editor.putString(inputUsername, inputPassword);
            editor.commit();
        } else {
            Toast.makeText(context, "该用户已存在", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteData(String inputUsername) {
        String name = context.getResources().getString(R.string.MY_DATA);//文件名
        SharedPreferences shp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shp.edit();

        if (shp.getString(inputUsername, null) != null) {
            editor.remove(inputUsername);
            editor.commit();
        } else {
            Toast.makeText(context, "该用户不存在", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateData(String inputUsername, String newPassword) {
        String name = context.getResources().getString(R.string.MY_DATA);//文件名
        SharedPreferences shp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shp.edit();

        if (shp == null)return;

        if (shp.getString(inputUsername, null) != null) {
            editor.remove(inputUsername);
            editor.putString(inputUsername, newPassword);
            editor.commit();
        } else {
            Toast.makeText(context, "该用户不存在", Toast.LENGTH_SHORT).show();
        }
    }

    public void clearData() {
        String name = context.getResources().getString(R.string.MY_DATA);//文件名
        SharedPreferences shp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shp.edit();

        if (shp == null)return;

        //editor.clear();

        Map<String, ?> map = shp.getAll();
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            deleteData(entry.getKey());
        }

    }

    public void loadData(TextView content) {
        String name = context.getResources().getString(R.string.MY_DATA);//文件名
        SharedPreferences shp = context.getSharedPreferences(name, Context.MODE_PRIVATE);

        Map<String, ?> map = shp.getAll();
        if (map == null) {
            content.setText("无已创建账号");
            return;
        }
        String textView = "";
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            textView += "username: " + entry.getKey() + "; password: " + entry.getValue() + '\n';
        }
        if (textView.length() == 0) {
            content.setText("无已创建账号");
        } else {
            content.setText(textView);
        }
    }

}
