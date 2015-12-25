package com.stone.shop.hbut.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.stone.shop.R;
import com.stone.shop.base.application.Options;
import com.stone.shop.base.application.ShopApplication;
import com.stone.shop.hbut.model.Cool;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 新闻列表适配器
 *
 * @author Stone
 * @date 2014-5-3
 */
public class CoolListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater = null;
    private List<Cool> mCoolList = null; // 所选分类下的所有店铺列表
    private DisplayImageOptions options;

    public CoolListAdapter(Context context, List<Cool> coolList) {
        mContext = context;
        mCoolList = coolList;
        options = Options.getListOptions();
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (mCoolList == null)
            return 0;
        return mCoolList.size();
    }

    @Override
    public Object getItem(int position) {
        if (mCoolList == null)
            return null;
        return mCoolList.get(position);
    }

    @Override
    public long getItemId(int position) {
        if (mCoolList == null)
            return 0;
        return position;
    }

    // 刷新列表中的数据
    public void refresh(ArrayList<Cool> list) {
        mCoolList = list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CoolHolder coolHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_cool, null);
            coolHolder = new CoolHolder();
            coolHolder.imgCoolPhoto = (CircleImageView) convertView
                    .findViewById(R.id.img_cool_photo);
            coolHolder.tvCoolName = (TextView) convertView
                    .findViewById(R.id.tv_cool_name);
            coolHolder.btnCoolVote = (Button) convertView
                    .findViewById(R.id.btn_cool_vote);

            convertView.setTag(coolHolder);

        } else {
            coolHolder = (CoolHolder) convertView.getTag();
        }

        // Left
        if (mCoolList.get(position).getPhoto() != null) {
            final int pos = position;
            // mCoolList
            // .get(position * 2)
            // .getPhoto()
            // .loadImageThumbnail(mContext, coolHolder.imgCoolPhotoLeft,
            // 150, 150, 50);
            /*ShopApplication
                    .getInstance()
                    .getImageLoader()
                    .displayImage(
                            mCoolList.get(position).getPhoto().getFileUrl(mContext),
                            coolHolder.imgCoolPhoto, options);*/
            ShopApplication.getAQuery().id(coolHolder.imgCoolPhoto)
                    .image(mCoolList.get(position).getPhoto().getFileUrl(mContext), true, true);

            if (mCoolList.get(position).getSex().equals("男")) {
                coolHolder.tvCoolName.setTextColor(mContext.getResources().getColor(R.color.color_bule));
                coolHolder.btnCoolVote.setBackgroundColor(mContext.getResources().getColor(R.color.color_bule));
            } else {
                coolHolder.tvCoolName.setTextColor(mContext.getResources().getColor(R.color.color_pink));
                coolHolder.btnCoolVote.setBackgroundColor(mContext.getResources().getColor(R.color.color_pink));
            }
            coolHolder.tvCoolName.setText(mCoolList.get(position)
                    .getUsername());
            coolHolder.btnCoolVote.setText("投票("
                    + mCoolList.get(position).getVotes().toString() + ")");
            coolHolder.btnCoolVote
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            mCoolList.get(pos).increment("votes"); // 分数递增1
                            mCoolList.get(pos).update(mContext,
                                    new UpdateListener() {

                                        @Override
                                        public void onSuccess() {
                                            // 更新本地的数据
                                            mCoolList.get(pos).setVotes(
                                                    mCoolList.get(pos)
                                                            .getVotes() + 1);
                                            refresh((ArrayList<Cool>) mCoolList);
                                            coolHolder.btnCoolVote
                                                    .setTextColor(R.color.text_color_normal);
                                            coolHolder.btnCoolVote
                                                    .setEnabled(false);
                                            coolHolder.btnCoolVote.setBackgroundColor(mContext.getResources().getColor(R.color.text_color_normal));
                                        }

                                        @Override
                                        public void onFailure(int arg0,
                                                              String arg1) {
                                            Log.i("计数器", "更新失败");
                                        }
                                    });
                        }
                    });

        }

//		// Right
//		if (mCoolList.size() >= position * 2 + 2) {
//			if (mCoolList.get(position * 2 + 1).getPhoto() != null) {
//				final int pos = position * 2 + 1;
//
//				coolHolder.imgCoolPhotoRight.setVisibility(View.VISIBLE);
//				coolHolder.tvCoolNameRight.setVisibility(View.VISIBLE);
//				coolHolder.btnCoolVotesRight.setVisibility(View.VISIBLE);
//				coolHolder.btnCoolPhotoRight.setVisibility(View.VISIBLE);
//
////				mCoolList
////						.get(position * 2 + 1)
////						.getPhoto()
////						.loadImageThumbnail(mContext,
////								coolHolder.imgCoolPhotoRight, 150, 150, 100);
//				ShopApplication
//				.getInstance()
//				.getImageLoader()
//				.displayImage(
//						mCoolList.get(position * 2 + 1).getPhoto().getFileUrl(mContext),
//						coolHolder.imgCoolPhotoRight, options);
//				coolHolder.tvCoolNameRight.setText(mCoolList.get(
//						position * 2 + 1).getUsername());
//				coolHolder.btnCoolVotesRight.setText("投票("
//						+ mCoolList.get(position * 2 + 1).getVotes().toString()
//						+ ")");
//				coolHolder.btnCoolVotesRight
//						.setOnClickListener(new OnClickListener() {
//
//							@Override
//							public void onClick(View v) {
//								mCoolList.get(pos).increment("votes"); // 分数递增1
//								mCoolList.get(pos).update(mContext,
//										new UpdateListener() {
//
//											@Override
//											public void onSuccess() {
//												// 更新本地的数据
//												mCoolList
//														.get(pos)
//														.setVotes(
//																mCoolList
//																		.get(pos)
//																		.getVotes() + 1);
//												refresh((ArrayList<Cool>) mCoolList);
//												coolHolder.btnCoolVotesRight
//														.setTextColor(R.color.text_color_normal);
//												coolHolder.btnCoolVotesRight
//														.setEnabled(false);
//											}
//
//											@Override
//											public void onFailure(int arg0,
//													String arg1) {
//												Log.i("计数器", "投票失败");
//											}
//										});
//							}
//
//						});
//			}
//		}
//		if (mCoolList.size() == position * 2 + 1) {
//			coolHolder.imgCoolPhotoRight.setVisibility(View.GONE);
//			coolHolder.tvCoolNameRight.setVisibility(View.GONE);
//			coolHolder.btnCoolVotesRight.setVisibility(View.GONE);
//			coolHolder.btnCoolPhotoRight.setVisibility(View.GONE);
//		}

        return convertView;
    }


    public class CoolHolder {

        public CircleImageView imgCoolPhoto;        //照片
        public TextView tvCoolName;            //姓名
        public Button btnCoolVote;            //票数

    }
}
