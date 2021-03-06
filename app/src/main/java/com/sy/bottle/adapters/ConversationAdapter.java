package com.sy.bottle.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.sy.bottle.R;
import com.sy.bottle.model.Conversation;
import com.sy.bottle.servlet.UserInfo_Servlet;
import com.sy.bottle.utils.TimeUtil;

import java.util.List;

/**
 * 会话界面adapter
 */
public class ConversationAdapter extends ArrayAdapter<Conversation> {
    private static final String TAG = "ConversationAdapter";

    private int resourceId;
    private View view;
    private ViewHolder viewHolder;

    Context context;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public ConversationAdapter(Context context, int resource, List<Conversation> objects) {
        super(context, resource, objects);
        this.context = context;
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView != null) {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.tvName = view.findViewById(R.id.name);
            viewHolder.guan = view.findViewById(R.id.guan);
            viewHolder.avatar = view.findViewById(R.id.avatar);
            viewHolder.lastMessage = view.findViewById(R.id.last_message);
            viewHolder.time = view.findViewById(R.id.message_time);
            viewHolder.unread = view.findViewById(R.id.unread_num);
            view.setTag(viewHolder);
        }
        final Conversation data = getItem(position);

        viewHolder.tvName.setText(data.getName());

        //判断是c2c聊天
        if (!TextUtils.isEmpty(data.getIdentify())) {
            //判断是否是官方账号
            if (data.getIdentify().contains("1000")) {
                viewHolder.guan.setVisibility(View.VISIBLE);
            } else {
                viewHolder.guan.setVisibility(View.GONE);
            }

//            //判断有没有头像
            if (data.getAvatar() != null) {
                Glide.with(context).load(data.getAvatar()).into(viewHolder.avatar);
            } else {
                new UserInfo_Servlet(convertView,viewHolder).execute(data.getIdentify());
            }
        } else {
            viewHolder.avatar.setImageResource(data.getAvatarID());
        }
        viewHolder.lastMessage.setText(data.getLastMessageSummary());
        viewHolder.time.setText(TimeUtil.getTimeStr(data.getLastMessageTime()));
        long unRead = data.getUnreadNum();
        if (unRead <= 0) {
            viewHolder.unread.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.unread.setVisibility(View.VISIBLE);
            String unReadStr = String.valueOf(unRead);
            if (unRead < 10) {
                viewHolder.unread.setBackground(getContext().getResources().getDrawable(R.mipmap.point1));
            } else {
                viewHolder.unread.setBackground(getContext().getResources().getDrawable(R.mipmap.point2));
                if (unRead > 99) {
                    unReadStr = "99+";
                }
            }
            viewHolder.unread.setText(unReadStr);
        }
        return view;
    }

    public class ViewHolder {
        public ImageView guan;
        public TextView tvName;
        public ImageView avatar;
        public TextView lastMessage;
        public TextView time;
        public TextView unread;

    }
}
