package com.example.liujingjing.smstext;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    EditText ed_mmscode;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getApplicationContext()
                .checkSelfPermission(Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            requestMusicPermissions();

        }
        ed_mmscode=(EditText) findViewById(R.id.ed_mms);
        SmsContent content = new SmsContent(new Handler());
        //×¢²á¶ÌÐÅ±ä»¯ŒàÌý
        this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, content);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestMusicPermissions() {
        this.requestPermissions(new String[]{Manifest.permission.READ_SMS},
                REQUEST_EXTERNAL_STORAGE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {


            }
        }

    }


    /**
     * ŒàÌý¶ÌÐÅÊýŸÝ¿â
     */
    class SmsContent extends ContentObserver {

        private Cursor cursor = null;

        public SmsContent(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {

            super.onChange(selfChange);
            Uri uri = Uri.parse("content://sms/inbox");
            Cursor c = getContentResolver().query(uri, null, null, null, "date desc");
            if (c != null) {
                while (c.moveToNext()) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date d = new Date(c.getLong(c.getColumnIndex("date")));
                    String date = dateFormat.format(d);
                    StringBuilder sb = new StringBuilder();
                    sb.append("发件人手机号码: " + c.getString(c.getColumnIndex("address")))
                            .append("信息内容: " + c.getString(c.getColumnIndex("body")))
                            .append(" 是否查看: " + c.getInt(c.getColumnIndex("read")))
                            .append(" 类型： " + c.getInt(c.getColumnIndex("type"))).append(date);
                    Log.i("xuzhi", sb.toString());

                }
                c.close();
            }
        }
    }






}
