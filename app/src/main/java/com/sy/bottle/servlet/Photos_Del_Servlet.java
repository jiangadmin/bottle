package com.sy.bottle.servlet;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sy.bottle.activity.mian.mine.Edit_Mine_Info_Activity;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.dialog.ReLogin_Dialog;
import com.sy.bottle.entity.Base_Entity;
import com.sy.bottle.entity.Const;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.utils.HttpUtil;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.SaveUtils;
import com.sy.bottle.view.TabToast;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: jiangadmin
 * @date: 2018/5/31
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 删除相册
 */
public class Photos_Del_Servlet extends AsyncTask<String, Integer, Base_Entity> {
    private static final String TAG = "Photos_Set_Servlet";

    Activity activity;


    public Photos_Del_Servlet(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Base_Entity doInBackground(String... strings) {
        Map map = new HashMap();
        map.put("photo_id", strings[0]);
        String res = HttpUtil.request(HttpUtil.DEL, Const.API + "photos/" + SaveUtils.getString(Save_Key.UID), map);
        LogUtil.e(TAG,res);
        Base_Entity entity;

        if (TextUtils.isEmpty(res)) {
            entity = new Base_Entity();
            entity.setStatus(-1);
            entity.setMessage("连接服务器失败！");
        } else {
            try {
                entity = new Gson().fromJson(res, Base_Entity.class);
            } catch (Exception e) {
                entity = new Base_Entity();
                entity.setStatus(-2);
                entity.setMessage("数据解析失败！");
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

                if (activity instanceof Edit_Mine_Info_Activity) {
                    ((Edit_Mine_Info_Activity) activity).initphotos();
                }
                break;
            case 401:
                new ReLogin_Dialog();
                break;
            default:
                TabToast.makeText(entity.getMessage());
                break;
        }
    }
}
