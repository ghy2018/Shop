package com.stone.shop.shop.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.stone.shop.R;
import com.stone.shop.user.ui.activity.UserInfoActivity;
import com.stone.shop.shop.model.Comment;
import com.stone.shop.base.util.ToastUtils;

import java.util.List;

public class CommentsListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Comment> mCommentsList; // 商品列表信息
    private LayoutInflater mInflater = null;

    public CommentsListAdapter(Context context, List<Comment> CommentsList) {
        mContext = context;
        mCommentsList = CommentsList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mCommentsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCommentsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // 刷新列表中的数据
    public void refresh(List<Comment> list) {
        mCommentsList = list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CommentsHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_comment, null);
            holder = new CommentsHolder();
            holder.tvUsername = (TextView) convertView.findViewById(R.id.tv_comment_username);
            holder.tvContent = (TextView) convertView
                    .findViewById(R.id.tv_comment_content);
            holder.imgType = (ImageView) convertView
                    .findViewById(R.id.img_comment_type);
            holder.tvTimeAt = (TextView) convertView.findViewById(R.id.tv_comment_at_time);
            holder.comment = mCommentsList.get(position);
            convertView.setTag(holder);
        } else {
            holder = (CommentsHolder) convertView.getTag();
        }


        String nickname;
        String comment = "";
        if (!holder.comment.isShowname())  //判断是否匿名显示
            nickname = Comment.COMMENT_USERNAME_D;
        else {
            if (holder.comment.getUser() == null)
                nickname = "黑名单用户";
            else  {
                nickname = holder.comment.getUser().getNickname();
                if (nickname.equals(""))
                    nickname = holder.comment.getUser().getUsername();
            }
        }
        comment = holder.comment.getContent();
        holder.tvUsername.setText(String.format("%s:  ", nickname));
        holder.tvContent.setText(comment);
        holder.tvUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.comment.getUser() == null || holder.comment.getUser().getObjectId().equals("")) {
                    ToastUtils.showToast("该用户已被管理员强制删除");
                    return;
                }
                Intent intent = new Intent(mContext, UserInfoActivity.class);
                intent.putExtra(UserInfoActivity.EXTRA_KEY_USER_ID, holder.comment.getUser().getObjectId());
                mContext.startActivity(intent);
            }
        });
        holder.tvTimeAt.setText("发表于：" + holder.comment.getCreatedAt().split(" ")[0]);
        return convertView;
    }


    /**
     * 商品视图
     *
     * @author Stone
     * @date 2014-4-26
     */
    public class CommentsHolder {

        public ImageView imgType;  //类型
        public TextView tvUsername;
        public TextView tvContent;  //评论内容
        public TextView tvTimeAt;  //评论时间

        public Comment comment;

    }

}
