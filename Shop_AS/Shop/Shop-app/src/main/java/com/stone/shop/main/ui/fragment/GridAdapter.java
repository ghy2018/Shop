package com.stone.shop.main.ui.fragment;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.stone.shop.R;
import com.stone.shop.base.config.FunctionType;

/**
 * 小菜-- 网格布局(ImageView+TextView)适配器
 *
 * @author Stone
 * @date 2014-4-24
 */
public class GridAdapter extends BaseAdapter {

    private Context mContext;
    private int mIndex = 0; // 代表当前需要适配页面中第几个GridView

    // 校园
    public static String[] mCampusTexts = FunctionType.typeCampusList;
    // private int[] mCampusImages = { R.drawable.ic_shop_wsq,
    // R.drawable.ic_shop_news, R.drawable.ic_shop_bxt,
    // R.drawable.ic_shop_party, R.drawable.ic_shop_road,
    // R.drawable.ic_shop_activity, R.drawable.ic_shop_score,
    // R.drawable.ic_shop_library };

    private int[] mCampusImages = {R.drawable.ic_shop_wsq,
            R.drawable.ic_shop_news, R.drawable.ic_shop_bxt,
            R.drawable.ic_shop_party, R.drawable.ic_shop_library};

    // 发现
    public static String[] mLifeTexts = FunctionType.typeLifeList;
    // private int[] mLifeImages = { R.drawable.ic_finder_lucky,
    // R.drawable.ic_finder_ggmm, R.drawable.ic_finder_lover };

    private int[] mLifeImages = {R.drawable.ic_finder_ggmm,
            R.drawable.ic_finder_lover};

    public GridAdapter(Context context, int index) {
        mContext = context;
        mIndex = index;
    }

    @Override
    public int getCount() {
        int count = 0;
        switch (mIndex) {
            case 0:
                count = mCampusImages.length;
                break;
            case 1:
                count = mLifeImages.length;
                break;
            default:
                break;
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(mContext, R.layout.item_shop_grid, null);

//        ColorGenerator generator = ColorGenerator.DEFAULT;
//        // generate random color
//        int color1 = generator.getRandomColor();
//        // generate color based on a key (same key returns the same color), useful for list/grid views
//        int color2 = generator.getColor("stonekity@gmail.com");
//
//        TextDrawable drawable1 = TextDrawable.builder()
//                .beginConfig()
//                .textColor(Color.WHITE)
//                .useFont(Typeface.DEFAULT)
//                .fontSize(40) /* size in px */
//                .toUpperCase()
//                .endConfig()
//                .buildRound(mCampusTexts[position], color1);
//        image.setImageDrawable(drawable1);

        ImageView image = (ImageView) view.findViewById(R.id.img_chooseImage);
        TextView text = (TextView) view.findViewById(R.id.tv_chooseText);
        switch (mIndex) {
            case 0:
                image.setImageResource(mCampusImages[position]);
                text.setText(mCampusTexts[position]);
                break;
            case 1:
                image.setImageResource(mLifeImages[position]);
                text.setText(mLifeTexts[position]);
                break;
            default:
                break;
        }

        return view;
    }

}
