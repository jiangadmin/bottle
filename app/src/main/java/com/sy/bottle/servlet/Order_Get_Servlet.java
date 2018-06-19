package com.sy.bottle.servlet;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sy.bottle.activity.mian.other.Recharge_Activity;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.dialog.ReLogin_Dialog;
import com.sy.bottle.entity.Base_Entity;
import com.sy.bottle.entity.Const;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.utils.HttpUtil;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.SaveUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: jiangadmin
 * @date: 2018/6/19
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 获取订单
 */
public class Order_Get_Servlet extends AsyncTask<String, Integer, Base_Entity> {
    private static final String TAG = "Order_Get_Servlet";

    Activity activity;

    public Order_Get_Servlet(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Base_Entity doInBackground(String... strings) {
        Map map = new HashMap();
        map.put("type", strings[0]);
        map.put("money", strings[1]);
        map.put("price", strings[2]);

        LogUtil.e(TAG, String.valueOf(map.get("type")));
        LogUtil.e(TAG, String.valueOf(map.get("money")));
        LogUtil.e(TAG, String.valueOf(map.get("price")));

        String res = HttpUtil.request(HttpUtil.POST, Const.API + "recharges/" + SaveUtils.getString(Save_Key.UID), map);

        Base_Entity entity;
        if (TextUtils.isEmpty(res)) {
            entity = new Base_Entity();
            entity.setStatus(-1);
            entity.setMessage("连接服务器失败");
        } else {
            try {
                entity = new Gson().fromJson(res, Base_Entity.class);
            } catch (Exception e) {
                entity = new Base_Entity();
                entity.setStatus(-2);
                entity.setMessage("数据解析失败");
            }
        }

        return entity;
    }

    @Override
    protected void onPostExecute(Base_Entity entity) {
        super.onPostExecute(entity);
        Loading.dismiss();
        switch (entity.getStatus()) {
            case 200:
                if (activity instanceof Recharge_Activity) {
                    ((Recharge_Activity) activity).CallBack_Order();
                }
                break;
            case 401:
                new ReLogin_Dialog();
                break;
        }
    }
}