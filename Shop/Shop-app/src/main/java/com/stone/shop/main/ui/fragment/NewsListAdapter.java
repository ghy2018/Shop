package com.stone.shop.main.ui.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.stone.shop.R;
import com.stone.shop.base.application.Options;
import com.stone.shop.base.application.ShopApplication;
import com.stone.shop.main.model.News;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻列表适配器
 *
 * @author Stone
 * @date 2014-5-3
 */
public class NewsListAdapter extends BaseAdapter {

    @SuppressWarnings("unused")
    private Context mContext;

    private LayoutInflater mInflater = null;
    private List<News> mNewsList = null; // 所选分类下的所有店铺列表
    private DisplayImageOptions options;

    public NewsListAdapter(Context context, List<News> newsList) {
        mContext = context;
        mNewsList = newsList;
        options = Options.getListOptions();
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mNewsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mNewsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // 刷新列表中的数据
    public void refresh(ArrayList<News> list) {
        mNewsList = list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NewsHolder newsHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_news, null);
            newsHolder = new NewsHolder();
            newsHolder.tvNewsType = (TextView) convertView
                    .findViewById(R.id.tv_news_type);
            newsHolder.tvNewsTitle = (TextView) convertView
                    .findViewById(R.id.tv_news_title);
            newsHolder.tvNewsContent = (TextView) convertView.findViewById(R.id.tv_news_content);
            newsHolder.tvNewsDate = (TextView) convertView
                    .findViewById(R.id.tv_news_date);
            newsHolder.imgNewsSmall = (ImageView) convertView
                    .findViewById(R.id.img_news_small);
            convertView.setTag(newsHolder);
        } else {
            newsHolder = (NewsHolder) convertView.getTag();
        }

        // 加载图片
        if (mNewsList.get(position).getPicNews() != null) {
            newsHolder.imgNewsSmall.setVisibility(View.VISIBLE);
            /*ShopApplication
                    .getInstance()
                    .getImageLoader()
                    .displayImage(
                            mNewsList.get(position).getPicNews().getFileUrl(mContext),
                            newsHolder.imgNewsSmall, options);*/
            ShopApplication.getAQuery()
                    .id(newsHolder.imgNewsSmall)
                    .image(mNewsList.get(position).getPicNews().getFileUrl(mContext), true, true);
        } else {
            newsHolder.imgNewsSmall.setVisibility(View.GONE);
        }

        // 拆分字符串，只取年月日
        String[] ss = mNewsList.get(position).getCreatedAt().split(" ");
        newsHolder.tvNewsType.setText(mNewsList.get(position).getType()); // 新闻类型
        newsHolder.tvNewsTitle.setText(mNewsList.get(position).getTitle()); // 新闻标题
        if (mNewsList.get(position).getContent().equals("")) {
            newsHolder.tvNewsContent.setVisibility(View.GONE);
        } else {
            newsHolder.tvNewsContent.setVisibility(View.VISIBLE);
            newsHolder.tvNewsContent.setText(mNewsList.get(position).getContent()); //新闻内容
        }
        newsHolder.tvNewsDate.setText("发表时间:" + ss[0]); // 新闻发布日期
        return convertView;
    }


    public class NewsHolder {

        public TextView tvNewsType;        //新闻分类
        public TextView tvNewsTitle;    //新闻标题
        public TextView tvNewsDate;        //新闻时间
        public TextView tvNewsContent;

        public ImageView imgNewsSmall;    //小图片

    }

}
