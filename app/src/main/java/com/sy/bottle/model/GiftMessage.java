package com.sy.bottle.model;

import android.content.Context;
import android.os.Environment;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.sy.bottle.adapters.ChatAdapter;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.entity.Gift_Entity;
import com.sy.bottle.utils.FileUtil;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.PicassoUtlis;
import com.sy.bottle.view.TabToast;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMFileElem;
import com.tencent.imsdk.TIMMessage;

import org.json.JSONObject;

/**
 * 礼物消息
 */
public class GiftMessage extends Message {

    public GiftMessage(TIMMessage message) {
        this.message = message;
    }

    public GiftMessage(String giftid, JSONObject gift) {
        message = new TIMMessage();
        TIMFileElem elem = new TIMFileElem();
        elem.setPath(gift.toString());
        elem.setFileName(giftid);
        message.addElement(elem);
    }

    /**
     * 显示消息
     *
     * @param viewHolder 界面样式
     * @param context    显示消息的上下文
     */
    @Override
    public void showMessage(ChatAdapter.ViewHolder viewHolder, Context context) {
        clearView(viewHolder);
        if (checkRevoke(viewHolder)) return;
        TIMFileElem e = (TIMFileElem) message.getElement(0);

        ImageView imageView = new ImageView(MyApp.getInstance());

        Gift_Entity.DataBean bean = new Gson().fromJson(e.getPath(), Gift_Entity.DataBean.class);

        PicassoUtlis.Cornersimg(bean.getPic_url(), imageView);

        getBubbleView(viewHolder).addView(imageView);
        showStatus(viewHolder);
    }

    /**
     * 获取消息摘要
     */
    @Override
    public String getSummary() {
        String str = getRevokeSummary();
        if (str != null) return str;
        return "[礼物]";
    }

    /**
     * 保存消息或消息文件
     */
    @Override
    public void save() {
        if (message == null) return;
        final TIMFileElem e = (TIMFileElem) message.getElement(0);
        String[] str = e.getFileName().split("/");
        String filename = str[str.length - 1];
        if (FileUtil.isFileExist(filename, Environment.DIRECTORY_DOWNLOADS)) {
            TabToast.makeText("文件已存在");
            return;
        }

        e.getToFile(FileUtil.getCacheFilePath(filename), new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                LogUtil.e(TAG, "getFile failed. code: " + i + " errmsg: " + s);
            }

            @Override
            public void onSuccess() {

            }
        });

    }
}
