package com.sy.bottle.activity.mian.other;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.servlet.Report_Photo_Servlet;
import com.sy.bottle.servlet.Report_Servlet;
import com.sy.bottle.utils.ImageUtils;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.view.TabToast;

/**
 * @author: jiangyao
 * @date: 2018/6/21
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 投诉
 */
public class Report_Activity extends Base_Activity implements View.OnClickListener {
    private static final String TAG = "Report_Activity";
    private static final String identify = "Report_Activity";

    public static void start(Context context, String id) {
        Intent intent = new Intent();
        intent.setClass(context, Report_Activity.class);
        intent.putExtra(identify, id);
        context.startActivity(intent);
    }

    Spinner spinner;
    EditText message;
    ImageView photos, photos_clean;
    Button submit;
    TextView report_info;

    /**
     * 请求相册
     */
    private static final int REQUEST_PICK = 201;

    String[] string = {"请选择举报理由", "扔瓶子内容（违规）", "色情低俗（文字/图片/语音）", "广告/垃圾信息", "欺诈信息", "发送政治/违法/恐怖内容", "其他违规行为"};

    private ArrayAdapter<String> adapter;

    String id, imageurl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        setTitle("投诉");
        setBack(true);

        id = getIntent().getStringExtra(identify);

        initview();
    }

    private void initview() {
        spinner = findViewById(R.id.recharge_spinner);
        message = findViewById(R.id.report_content);
        photos = findViewById(R.id.report_photo);
        photos_clean = findViewById(R.id.report_photo_clear);
        submit = findViewById(R.id.report_submit);
        report_info = findViewById(R.id.report_info);

        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, string);

        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //将adapter 添加到spinner中
        spinner.setAdapter(adapter);

        //设置默认值
        spinner.setVisibility(View.VISIBLE);

        submit.setOnClickListener(this);
        photos.setOnClickListener(this);
        photos_clean.setOnClickListener(this);
        report_info.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.waiter:
                Report_Info_Activity.start(this);
                break;
            case R.id.report_submit:

                if (spinner.getSelectedItemPosition() == 0) {
                    TabToast.makeText("请选择举报理由");
                    return;
                }

                Loading.show(this, "投诉中");

                String text = message.getText().toString();
                if (TextUtils.isEmpty(imageurl)) {
                    new Report_Servlet().execute(id, string[spinner.getSelectedItemPosition()], TextUtils.isEmpty(text) ? "无" : text);
                } else {
                    new Report_Servlet().execute(id, string[spinner.getSelectedItemPosition()], TextUtils.isEmpty(text) ? "无" : text, imageurl);
                }

                break;
            case R.id.report_photo:
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, REQUEST_PICK);// //适用于4.4及以上android版本
                break;

            case R.id.report_photo_clear:
                photos.setImageResource(R.drawable.ic_image_a);
                photos_clean.setVisibility(View.GONE);
                imageurl = null;
                break;
            case R.id.report_info:
                Report_Info_Activity.start(this);
                break;
        }
    }

    /**
     * 图片返回
     *
     * @param url
     */
    public void CallBcak(String url) {
        LogUtil.e(TAG, "图片地址 " + url);
        imageurl = url;
        Glide.with(this).load(url).into(photos);
        photos_clean.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        LogUtil.e("evan", "requestCode:" + requestCode + "--resultCode:" + resultCode);
        switch (requestCode) {

            //调用系统相册返回
            case REQUEST_PICK:
                if (resultCode == RESULT_OK) {

                    String file = ImageUtils.getRealFilePath(this, intent.getData());

                    if (!file.substring(file.length() - 5).contains(".jpg") &&
                            !file.substring(file.length() - 5).contains(".png") &&
                            !file.substring(file.length() - 5).contains(".jpeg")) {

                        TabToast.makeText("请选择图片上传");
                    } else {
                        Loading.show(this, "正在上传", false);
                        new Report_Photo_Servlet(this).execute(file);
                    }
                }
                break;
        }
    }
}
