package com.stone.shop.hbut.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.stone.shop.R;
import com.stone.shop.base.ui.BaseActivity;
import com.stone.shop.hbut.ui.adapter.TradeItemListAdapter;
import com.stone.shop.hbut.ui.adapter.TradeListIndexAdapter;
import com.stone.shop.hbut.model.SecondTrade;
import com.stone.shop.base.util.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * 某一分类下的所有店铺页面
 * @author Stone
 * @date  2014-4-26 
 */
public class SecondTradeActivity extends BaseActivity implements OnItemClickListener{
	@SuppressWarnings("unused")
	private static final String TAG = "SecondTradeActivity" ; 

	//private TextView tvTitle;

    private ListView lvTradeListIndex;

	private ListView lvTradeItemList;
	private TradeItemListAdapter tradeItemListAdapter;
	private List<SecondTrade> tradeItemList = new ArrayList<>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second_trade_all);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

		//获取物品数据
		getTradeItemData();
		initView();

	}

	public void initView() {
		//设置标题
		//tvTitle = (TextView) findViewById(R.id.tv_title);
		//tvTitle.setText("二手交易");

        lvTradeListIndex = (ListView) findViewById(R.id.lv_second_trade_index);
        List<String> mIndexList = new ArrayList<>();
        mIndexList.add("学习图书");
        mIndexList.add("考试资料");
        mIndexList.add("电子数码");
        mIndexList.add("交通出行");
        mIndexList.add("生活用品");
        mIndexList.add("美食旅游");
        mIndexList.add("其他");
        TradeListIndexAdapter adapter = new TradeListIndexAdapter(this, mIndexList);
        lvTradeListIndex.setAdapter(adapter);

        lvTradeItemList = (ListView) findViewById(R.id.lv_second_trade_all);
		tradeItemListAdapter = new TradeItemListAdapter(this, tradeItemList);
		lvTradeItemList.setAdapter(tradeItemListAdapter);
		lvTradeItemList.setOnItemClickListener(this);
		lvTradeItemList.setSelector(R.drawable.selector_default);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		//toast("点击了： " + position);
		//将当前点击的Shop对象传递给下一个Activity
//		Intent toShopItem = new Intent(SecondTradeActivity.this, ShopItemActivity.class);
//		Bundle bundle = new Bundle();  
//        bundle.putSerializable("shop", shopList.get(position) );  
//        bundle.putString("shopID", shopList.get(position).getObjectId()); //商铺的ID需要单独传递,否则获取到的是null
//        Log.i(TAG, ">>发出>>" + "shopID: "+shopList.get(position).getObjectId()+" shopName: "+shopList.get(position).getName());
//        toShopItem.putExtras(bundle);
//		startActivity(toShopItem);
	}
	
	/**
	 * 加载二手交易中所有商品到ListView中
	 */
	private void getTradeItemData() {
        showProgressDialog();
		BmobQuery<SecondTrade> query = new BmobQuery<SecondTrade>();
		query.order("-updatedAt");
		query.findObjects(this, new FindListener<SecondTrade>() {
			
		    @Override
		    public void onSuccess(List<SecondTrade> object) {
                dismissProgressDialog();
		    	if(object.size()==0)
		    		toast("亲, 你来得太早了点哦");
		    	else {
		    		tradeItemList = object;
			        // 通知Adapter数据更新
			    	tradeItemListAdapter.refresh((ArrayList<SecondTrade>) tradeItemList);
				}
		    }
		    
			@Override
			public void onError(int arg0, String msg) {
                dismissProgressDialog();
				toast("查询失败:"+msg);
			}
		});
	}
	
	private void toast(String toast) {
        ToastUtils.showToast(toast);
	};
	
	@Override
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("SecondTradeActivity"); //统计页面
	    MobclickAgent.onResume(this);          //统计时长
	}
	
	@Override
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("SecondTradeActivity"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
	    MobclickAgent.onPause(this);
	}
	
}
