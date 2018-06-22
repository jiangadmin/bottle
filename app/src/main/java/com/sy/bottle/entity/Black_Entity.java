package com.sy.bottle.entity;

import java.util.List;

/**
 * @author: jiangyao
 * @date: 2018/6/20
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 黑名单列表
 */
public class Black_Entity extends Base_Entity {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * user_id : 1000003
         * friend_id : 1000005
         * nikename : 一个人心潮澎湃。
         * avatar : http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJafX8ibIBv9lYh4sxwhIfls0KKBbpexPcJqUzINAibW0VYvQBy2V7ahLx52eC9ibl3VYsRJBmT1Cmew/132
         * sign : 这个人很懒，什么都没有留下
         * content : null
         */

        private int id;
        private String user_id;
        private String friend_id;
        private String nikename;
        private String avatar;
        private String sign;
        private Object content;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getFriend_id() {
            return friend_id;
        }

        public void setFriend_id(String friend_id) {
            this.friend_id = friend_id;
        }

        public String getNikename() {
            return nikename;
        }

        public void setNikename(String nikename) {
            this.nikename = nikename;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public Object getContent() {
            return content;
        }

        public void setContent(Object content) {
            this.content = content;
        }
    }
}
