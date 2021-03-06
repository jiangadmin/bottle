package com.sy.bottle.model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.sy.bottle.R;
import com.sy.bottle.activity.GroupManageMessageActivity;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.presenter.GroupManagerPresenter;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.SaveUtils;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.ext.group.TIMGroupPendencyItem;

import java.util.Calendar;

/**
 * 群管理会话
 */
public class GroupManageConversation extends Conversation {

    private final String TAG = "GroupManageConversation";

    private TIMGroupPendencyItem lastMessage;

    private long unreadCount;


    public GroupManageConversation(TIMGroupPendencyItem message) {
        lastMessage = message;
    }


    /**
     * 获取最后一条消息的时间
     */
    @Override
    public long getLastMessageTime() {
        return lastMessage.getAddTime();
    }

    /**
     * 获取未读消息数量
     */
    @Override
    public long getUnreadNum() {
        return unreadCount;
    }

    /**
     * 将所有消息标记为已读
     */
    @Override
    public void readAllMessage() {
        //不能传入最后一条消息时间，由于消息时间戳的单位是秒
        GroupManagerPresenter.readGroupManageMessage(Calendar.getInstance().getTimeInMillis(), new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                LogUtil.i(TAG, "read all message error,code " + i);
            }

            @Override
            public void onSuccess() {
                LogUtil.i(TAG, "read all message succeed");
            }
        });
    }

    @Override
    public String getAvatar() {
        return null;
    }

    /**
     * 获取头像
     */
    @Override
    public Integer getAvatarID() {
//        return null;
        return R.mipmap.ic_news;
    }

    /**
     * 跳转到聊天界面或会话详情
     *
     * @param context 跳转上下文
     */
    @Override
    public void navToDetail(Context context) {
        readAllMessage();
        Intent intent = new Intent(context, GroupManageMessageActivity.class);
        context.startActivity(intent);
    }

    /**
     * 获取最后一条消息摘要
     */
    @Override
    public String getLastMessageSummary() {
        if (lastMessage == null) return "";
        String from = lastMessage.getFromUser();
        String to = lastMessage.getToUser();

        boolean isSelf = from.equals(SaveUtils.getString(Save_Key.UID));
        switch (lastMessage.getPendencyType()) {
            case INVITED_BY_OTHER:
                if (isSelf) {
                    return "我邀请 " + to + " 加入群";
                } else {
                    if (to.equals(SaveUtils.getString(Save_Key.UID))) {
                        return from + "邀请我加入群";
                    } else {
                        return from + " 邀请 " + to + "加入群";
                    }
                }
            case APPLY_BY_SELF:
                if (isSelf) {
                    return "我申请加群" + GroupInfo.getInstance().getGroupName(lastMessage.getGroupId());
                } else {
                    return from + "申请加群" + GroupInfo.getInstance().getGroupName(lastMessage.getGroupId());
                }

            default:
                return "";
        }
    }

    /**
     * 获取名称
     */
    @Override
    public String getName() {
        return "群组消息";
    }


    /**
     * 设置最后一条消息
     */
    public void setLastMessage(TIMGroupPendencyItem message) {
        lastMessage = message;
    }


    /**
     * 设置未读数量
     *
     * @param count 未读数量
     */
    public void setUnreadCount(long count) {
        unreadCount = count;
    }


}
