package com.sy.bottle.servlet;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.dialog.Base_Dialog;
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
 * @author: jiangyao
 * @date: 2018/6/22
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO:发起投诉
 */
public class Report_Servlet extends AsyncTask<String, Integer, Base_Entity> {
    private static final String TAG = "Report_Servlet";

    @Override
    protected Base_Entity doInBackground(String... strings) {
        Map map = new HashMap();
        map.put("to_uid", strings[0]);
        map.put("keyword", strings[1]);
        map.put("content", strings[2]);
        if (strings.length > 3)
            map.put("pic_urls", strings[3]);

        String res = HttpUtil.request(HttpUtil.POST, Const.API + "complaints/" + SaveUtils.getString(Save_Key.UID), map);
        LogUtil.e(TAG, res);
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
                Base_Dialog base_dialog = new Base_Dialog(MyApp.currentActivity());
                base_dialog.setMessage("提交成功,平台作为中立服务者，会按照相关法律规定，判断并根据实际情况进行处理，12小时内通过系统消息反馈结果给你");
                base_dialog.setOk("我知道了", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MyApp.finishActivity();
                    }
                });
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
