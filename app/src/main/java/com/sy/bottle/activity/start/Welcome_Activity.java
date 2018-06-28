package com.sy.bottle.activity.start;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.dialog.Base_Dialog;
import com.sy.bottle.servlet.Update_Servlet;
import com.sy.bottle.utils.LogUtil;

/**
 * @author: jiangadmin
 * @date: 2018/5/26
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 欢迎页
 */
public class Welcome_Activity extends Base_Activity {
    private static final String TAG = "Welcome_Activity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        PackageManager pm = getPackageManager();
        // 获取是否支持电话
        boolean telephony = pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
        // 是否支持GSM
        boolean gsm = pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY_GSM);
        boolean wifi = pm.hasSystemFeature(PackageManager.FEATURE_WIFI);
        boolean cam = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
        boolean gyr = pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE);
        boolean light = pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_LIGHT);

        if (telephony && gsm && wifi && cam && gyr && light) {
            //检测更新
            new Update_Servlet(this).execute();
        } else {
            Base_Dialog dialog = new Base_Dialog(this);
            dialog.setTitle("抱歉");
            dialog.setMessage("检测到你当前的设备为模拟器,我们无法对您此设备提供服务！");
            dialog.setOk("确定", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyApp.AppExit();
                }
            });
        }


//        if (SaveUtils.getBoolean(Save_Key.S_跳过引导)) {
//            //判断是否登录过
//
//        } else {
//            Guide_Activity.start(this);
//            MyApp.finishActivity();
//        }
    }
}
