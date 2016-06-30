package com.stone.shop.activity.old;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.stone.shop.user.ui.activity.MineActivity;
import com.stone.shop.shop.ui.activity.ShopActivity;
import com.stone.shop.shop.ui.activity.ShopAllActivity;
import com.stone.shop.base.config.FunctionType;
import com.stone.shop.R;

/**
 * 应用主界面
 * 
 * @date 2014-4-24
 * @author Stone
 */
@SuppressWarnings("deprecation")
public class OldMainActivity extends TabActivity {

	@SuppressWarnings("unused")
	private static final String TAG = "OldMainActivity";

	private TabHost tabHost;
	private LayoutInflater layoutInflater;

	int[] mIcon = new int[] { R.drawable.ic_shop, R.drawable.ic_sale,
			R.drawable.ic_car, R.drawable.ic_mine };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_old);

		initTabView();
	}

	public View getTabItemView(int i) {
		View view = layoutInflater.inflate(R.layout.item_tab_widget, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.tab_icon);
		imageView.setImageResource(mIcon[i]);
		TextView textView = (TextView) view.findViewById(R.id.tab_title);
		textView.setText(FunctionType.typeTabs[i]);
		textView.setTypeface(Typeface.MONOSPACE);
		textView.setTextColor(R.color.text_color_normal);
		textView.setTextSize(12);
		return view;
	}

	public void initTabView() {

		/**
		 * tabHost.newTabSpec("artist")创建一个标签项，其中artist为它的标签标识符，
		 * 相当于jsp页面标签的name属性
		 * setIndicator("艺术标签",resources.getDrawable(R.drawable
		 * .ic_tab))设置标签显示文本以及标签上的图标（该图标并不是一个图片，而是一个xml文件哦）
		 * setContent(intent)为当前标签指定一个意图 tabHost.addTab(spec); 将标签项添加到标签中
		 */

		tabHost = getTabHost();
		layoutInflater = LayoutInflater.from(this);
		TabHost.TabSpec spec;

		// 首页
		Intent intent1 = new Intent(this, ShopActivity.class);
		spec = tabHost.newTabSpec(FunctionType.typeTabs[0]).setIndicator(getTabItemView(0))
				.setContent(intent1);
		tabHost.addTab(spec);

		// 小卖铺
		Intent intent2 = new Intent(this, ShopAllActivity.class);
        intent2.putExtra("isFrom", "小卖铺");
		spec = tabHost.newTabSpec(FunctionType.typeTabs[1]).setIndicator(getTabItemView(1))
				.setContent(intent2);
		tabHost.addTab(spec);

		// 我的
		Intent intent3 = new Intent(this, MineActivity.class);
		spec = tabHost.newTabSpec(FunctionType.typeTabs[2]).setIndicator(getTabItemView(2))
				.setContent(intent3);
		tabHost.addTab(spec);

		/*
		 * Intent intent3 = new Intent(this, WsqActivity.class); spec =
		 * tabHost.newTabSpec(mTitle[2]).setIndicator( getTabItemView(2)
		 * ).setContent(intent3); tabHost.addTab(spec);
		 * 
		 * Intent intent4 = new Intent(this, OldMineActivity.class); spec =
		 * tabHost.newTabSpec(mTitle[3]).setIndicator( getTabItemView(3)
		 * ).setContent(intent4); tabHost.addTab(spec);
		 */
		tabHost.setOnTabChangedListener(new OnTabChangedListener());
		tabHost.setCurrentTab(0);
        updateTab();
	}

	/**
	 * 更新Tab标签的颜色，和字体的颜色
	 * 
	 */
	private void updateTab() {
		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			View view = tabHost.getTabWidget().getChildAt(i);
			TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i)
					.findViewById(R.id.tab_title);
			if (tabHost.getCurrentTab() == i) {// 选中
                view.setBackgroundColor(this.getResources().getColor(R.color.bg_color_first));
				tv.setTextColor(this.getResources().getColor(R.color.text_color_white));
			} else {// 不选中
				view.setBackgroundColor(this.getResources().getColor(R.color.bg_color_second));// 非选择的背景
				tv.setTextColor(this.getResources().getColor(R.color.bg_color_first));
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			System.exit(0);
			return false;
		} else if (keyCode == KeyEvent.KEYCODE_MENU
				&& event.getRepeatCount() == 0) {
			return true; // 返回true就不会弹出默认的setting菜单
		}
		return false;
	}

	class OnTabChangedListener implements OnTabChangeListener {

		@Override
		public void onTabChanged(String tabId) {
			tabHost.setCurrentTabByTag(tabId);
			//System.out.println("tabid " + tabId);
			//System.out.println("curreny after: " + tabHost.getCurrentTabTag());
			updateTab();
		}
	}
}
