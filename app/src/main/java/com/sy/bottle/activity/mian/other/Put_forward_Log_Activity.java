package com.sy.bottle.activity.mian.other;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;
import com.sy.bottle.adapters.Put_Forward_Log_Adapter;
import com.sy.bottle.entity.Put_Forward_Log_Entity;
import com.sy.bottle.servlet.Put_Forward_Log_Servlet;

import java.util.List;

/**
 * @author: jiangyao
 * @date: 2018/6/22
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 提现记录
 */
public class Put_forward_Log_Activity extends Base_Activity {
    private static final String TAG = "Put_forward_Log_Activit";

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, Put_forward_Log_Activity.class);
        context.startActivity(intent);
    }

    LinearLayout view_null;
    ListView listView;

    Put_Forward_Log_Adapter put_forward_log_adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put_forward_log);

        setTitle("提取记录");
        setBack(true);

        initview();
    }

    private void initview() {
        view_null = findViewById(R.id.view_null);
        listView = findViewById(R.id.log_list);

        put_forward_log_adapter = new Put_Forward_Log_Adapter(this);
        listView.setAdapter(put_forward_log_adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Put_Forward_Log_Servlet(this).execute();
    }

    /**
     * 提取记录返回
     * @param dataBeans
     */
    public void CallBack(List<Put_Forward_Log_Entity.DataBean> dataBeans) {

        if (dataBeans != null) {
            view_null.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            put_forward_log_adapter.setDataBeans(dataBeans);
            put_forward_log_adapter.notifyDataSetChanged();
        } else {
            view_null.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }

    }
}

