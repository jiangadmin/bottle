package com.sy.bottle.model;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sy.bottle.R;
import com.sy.bottle.activity.mian.chat.ChatActivity;
import com.sy.bottle.adapters.ChatAdapter;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.entity.Const;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.utils.EmoticonUtil;
import com.sy.bottle.utils.SaveUtils;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMElemType;
import com.tencent.imsdk.TIMFaceElem;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMTextElem;
import com.tencent.imsdk.ext.message.TIMMessageDraft;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 文本消息数据
 */
public class TextMessage extends Message {

    public TextMessage(TIMMessage message) {
        this.message = message;
    }

    public TextMessage(String s) {
        message = new TIMMessage();
        TIMTextElem elem = new TIMTextElem();
        elem.setText(s);
        message.addElement(elem);
    }

    public TextMessage(TIMMessageDraft draft) {
        message = new TIMMessage();
        for (TIMElem elem : draft.getElems()) {
            message.addElement(elem);
        }
    }

    private List<ImageSpan> sortByIndex(final Editable editInput, ImageSpan[] array) {
        ArrayList<ImageSpan> sortList = new ArrayList<>();
        for (ImageSpan span : array) {
            sortList.add(span);
        }
        Collections.sort(sortList, new Comparator<ImageSpan>() {
            @Override
            public int compare(ImageSpan lhs, ImageSpan rhs) {
                return editInput.getSpanStart(lhs) - editInput.getSpanStart(rhs);
            }
        });

        return sortList;
    }

    public TextMessage(Editable s) {
        message = new TIMMessage();
        ImageSpan[] spans = s.getSpans(0, s.length(), ImageSpan.class);
        List<ImageSpan> listSpans = sortByIndex(s, spans);
        int currentIndex = 0;
        for (ImageSpan span : listSpans) {
            int startIndex = s.getSpanStart(span);
            int endIndex = s.getSpanEnd(span);
            if (currentIndex < startIndex) {
                TIMTextElem textElem = new TIMTextElem();
                textElem.setText(s.subSequence(currentIndex, startIndex).toString());
                message.addElement(textElem);
            }
            TIMFaceElem faceElem = new TIMFaceElem();
            int index = Integer.parseInt(s.subSequence(startIndex, endIndex).toString());
            faceElem.setIndex(index);
            if (index < EmoticonUtil.emoticonData.length) {
                faceElem.setData(EmoticonUtil.emoticonData[index].getBytes(Charset.forName("UTF-8")));
            }
            message.addElement(faceElem);
            currentIndex = endIndex;
        }
        if (currentIndex < s.length()) {
            TIMTextElem textElem = new TIMTextElem();
            textElem.setText(s.subSequence(currentIndex, s.length()).toString());
            message.addElement(textElem);
        }
    }

    /**
     * 在聊天界面显示消息
     *
     * @param viewHolder 界面样式
     * @param context    显示消息的上下文
     */
    @Override
    public void showMessage(ChatAdapter.ViewHolder viewHolder, final Context context, final int position) {
        clearView(viewHolder);
        if (checkRevoke(viewHolder)) return;

        viewHolder.rightMessage.setBackgroundResource(R.drawable.bg_bubble_blue);
        viewHolder.leftMessage.setBackgroundResource(R.drawable.bg_bubble_gray);

        String friendfaceurl = SaveUtils.getString(Save_Key.S_头像 + message.getSender());
        if (!TextUtils.isEmpty(friendfaceurl)) {
            Glide.with(context).load(friendfaceurl).apply(new RequestOptions().placeholder(R.drawable.head_other)).into(viewHolder.leftAvatar);
        }
        Glide.with(context).load(SaveUtils.getString(Save_Key.S_头像)).apply(new RequestOptions().placeholder(R.drawable.head_me)).into(viewHolder.rightAvatar);

        boolean hasText = false;
        TextView tv = new TextView(MyApp.getInstance());
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        tv.setTextColor(MyApp.getInstance().getResources().getColor(isSelf() ? R.color.white : R.color.black));
        List<TIMElem> elems = new ArrayList<>();
        for (int i = 0; i < message.getElementCount(); ++i) {
            elems.add(message.getElement(i));
            if (message.getElement(i).getType() == TIMElemType.Text) {
                hasText = true;
            }
        }
        SpannableStringBuilder stringBuilder = getString(elems, context);
        if (!hasText) {
            stringBuilder.insert(0, " ");
        }

        //包含阅后即焚关键字
        if (stringBuilder.toString().contains(Const.ReadDes)) {
            tv.setText("【点开、长按查看、松手即毁】");
        } else {
            tv.setText(stringBuilder);
        }

        final SpannableStringBuilder stringBuilder1 = stringBuilder;
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (stringBuilder1.toString().contains(Const.ReadDes)) {
                    android.os.Message message = android.os.Message.obtain();
                    message.obj = stringBuilder1;
                    message.what = 2;
                    message.arg1 = position;
                    ChatActivity.mHandler.sendMessage(message);
                }

                //删除此条记录
                remove();
            }
        });
        getBubbleView(viewHolder).addView(tv);
        showStatus(viewHolder);

    }

    /**
     * 获取消息摘要
     */
    @Override
    public String getSummary() {
        String str = getRevokeSummary();
        if (str != null) return str;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < message.getElementCount(); ++i) {
            switch (message.getElement(i).getType()) {
                case Face:
                    TIMFaceElem faceElem = (TIMFaceElem) message.getElement(i);
                    byte[] data = faceElem.getData();
                    if (data != null) {
                        result.append(new String(data, Charset.forName("UTF-8")));
                    }
                    break;
                case Text:
                    TIMTextElem textElem = (TIMTextElem) message.getElement(i);
                    result.append(textElem.getText());
                    break;
            }

        }

        //包含阅后即焚关键字
        if (result.toString().contains(Const.ReadDes)) {
            return "[阅后即焚·文字]";
        } else {
            return result.toString();
        }

    }

    /**
     * 保存消息或消息文件
     */
    @Override
    public void save() {

    }

    private static int getNumLength(int n) {
        return String.valueOf(n).length();
    }

    public static SpannableStringBuilder getString(List<TIMElem> elems, Context context) {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        for (int i = 0; i < elems.size(); ++i) {
            switch (elems.get(i).getType()) {
                case Face:
                    TIMFaceElem faceElem = (TIMFaceElem) elems.get(i);
                    int startIndex = stringBuilder.length();
                    try {
                        AssetManager am = context.getAssets();
                        InputStream is = am.open(String.format("emoticon/%d.png", faceElem.getIndex()));
                        if (is == null) continue;
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        Matrix matrix = new Matrix();
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();
                        matrix.postScale(1.8f, 1.8f);
                        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
                        ImageSpan span = new ImageSpan(context, resizedBitmap, ImageSpan.ALIGN_BASELINE);
                        stringBuilder.append(String.valueOf(faceElem.getIndex()));
                        stringBuilder.setSpan(span, startIndex, startIndex + getNumLength(faceElem.getIndex()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        is.close();
                    } catch (IOException e) {

                    }
                    break;
                case Text:
                    TIMTextElem textElem = (TIMTextElem) elems.get(i);
                    stringBuilder.append(textElem.getText());
                    break;
            }

        }
        return stringBuilder;
    }

}
