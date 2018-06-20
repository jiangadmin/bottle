package com.sy.bottle.activity.mian.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;
import com.sy.bottle.adapters.Gift_Get_Log_Adapter;
import com.sy.bottle.adapters.Gift_Set_Log_Adapter;
import com.sy.bottle.entity.Gift_Get_Log_Entity;
import com.sy.bottle.entity.Gift_Set_Log_Entity;
import com.sy.bottle.servlet.Gift_Get_Log_Servlet;
import com.sy.bottle.servlet.Gift_Set_Log_Servlet;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: jiangyao
 * @date: 2018/6/9
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 赠送记录
 */
public class Log_Activity extends Base_Activity implements View.OnClickListener {
    private static final String TAG = "Gift_Activity";

    ListView listView;

    LinearLayout view_null;

    TextView gifts_get, gifts_set;

    Gift_Set_Log_Adapter gift_set_log_adapter;
    Gift_Get_Log_Adapter gift_get_log_adapter;

    List<Gift_Set_Log_Entity.DataBean> setbeans = new ArrayList<>();
    List<Gift_Get_Log_Entity.DataBean> getbeans = new ArrayList<>();

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, Log_Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        setTitle("礼物记录");

        setBack(true);

        listView = findViewById(R.id.gift_log_list);
        view_null = findViewById(R.id.view_null);
        gifts_set = findViewById(R.id.log_gifts_set);
        gifts_get = findViewById(R.id.log_gifts_get);
        gift_get_log_adapter = new Gift_Get_Log_Adapter(this);
        gift_set_log_adapter = new Gift_Set_Log_Adapter(this);

        gifts_get.setOnClickListener(this);
        gifts_set.setOnClickListener(this);

        new Gift_Get_Log_Servlet(this).execute();
        gifts_get.setEnabled(false);
        gifts_set.setEnabled(true);
        gifts_get.setTextColor(getResources().getColor(R.color.style_color));

    }

    /**
     * 送礼物记录
     *
     * @param beans
     */
    public void CallBack_Gift_Set(List<Gift_Set_Log_Entity.DataBean> beans) {

        if (setbeans != null && setbeans.size() > 0) {
            setbeans.clear();
            setbeans.addAll(beans);
            listView.setAdapter(gift_set_log_adapter);
            view_null.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            gift_set_log_adapter.setDataBeans(setbeans);
            gift_set_log_adapter.notifyDataSetChanged();
        } else {
            view_null.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
    }

    /**
     * 送礼物记录
     *
     * @param beans
     */
    public void CallBack_Gift_Get(List<Gift_Get_Log_Entity.DataBean> beans) {

        if (getbeans != null && getbeans.size() > 0) {
            getbeans.clear();
            getbeans.addAll(beans);
            listView.setAdapter(gift_get_log_adapter);
            view_null.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            gift_get_log_adapter.setDataBeans(getbeans);
            gift_get_log_adapter.notifyDataSetChanged();
        } else {
            view_null.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.log_gifts_get:
                gifts_get.setTextColor(getResources().getColor(R.color.style_color));
                gifts_set.setTextColor(getResources().getColor(R.color.gray_6));
                gifts_get.setEnabled(false);
                gifts_set.setEnabled(true);
                if (getbeans.size() > 0) {
                    CallBack_Gift_Get(getbeans);
                } else {
                    new Gift_Get_Log_Servlet(this).execute();
                }
                break;
            case R.id.log_gifts_set:
                gifts_get.setTextColor(getResources().getColor(R.color.gray_6));
                gifts_set.setTextColor(getResources().getColor(R.color.style_color));
                gifts_get.setEnabled(true);
                gifts_set.setEnabled(false);
                if (setbeans.size() > 0) {
                    CallBack_Gift_Set(setbeans);
                } else {
                    new Gift_Set_Log_Servlet(this).execute();
                }
                break;

        }
    }
}
