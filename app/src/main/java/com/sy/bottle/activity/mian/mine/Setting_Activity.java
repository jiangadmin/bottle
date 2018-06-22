package com.sy.bottle.activity.mian.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;
import com.sy.bottle.activity.mian.other.MessageNotifySettingActivity;
import com.sy.bottle.dialog.Esc_Dialog;

/**
 * @author: jiangyao
 * @date: 2018/6/4
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 设置
 */
public class Setting_Activity extends Base_Activity implements View.OnClickListener {
    private static final String TAG = "Setting_Servlet";

    Button blackList,about, esc, notify;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, Setting_Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);

        setTitle("设置");
        setBack(true);

        initview();
    }

    private void initview() {
        blackList = findViewById(R.id.blackList);
        about = findViewById(R.id.setting_about);
        esc = findViewById(R.id.setting_out);
        notify = findViewById(R.id.setting_notify);

        blackList.setOnClickListener(this);
        about.setOnClickListener(this);
        esc.setOnClickListener(this);
        notify.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.blackList:
                Black_Activity.start(this);
                break;
            case R.id.setting_notify:
                MessageNotifySettingActivity.start(this);
                break;
            case R.id.setting_about:
                About_Activity.start(this);
                break;
            case R.id.setting_out:
                new Esc_Dialog(this);
                break;
        }
    }
}
