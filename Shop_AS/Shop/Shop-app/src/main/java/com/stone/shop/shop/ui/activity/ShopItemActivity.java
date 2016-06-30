package com.stone.shop.shop.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.stone.shop.R;
import com.stone.shop.base.ui.BaseActivity;
import com.stone.shop.shop.ui.adapter.CommentsListAdapter;
import com.stone.shop.shop.ui.adapter.GoodsListAdapter;
import com.stone.shop.shop.ui.adapter.ViewPagerAdapter;
import com.stone.shop.main.manager.ACLManager;
import com.stone.shop.shop.ShopManager;
import com.stone.shop.shop.model.Comment;
import com.stone.shop.shop.model.Good;
import com.stone.shop.shop.model.Shop;
import com.stone.shop.user.model.User;
import com.stone.shop.base.util.ToastUtils;
import com.stone.shop.base.widget.ShopCartView;
import com.stone.shop.base.widget.TitleBarView;
import com.stone.shop.base.widget.ViewPagerCompat;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class ShopItemActivity extends BaseActivity implements OnClickListener,
        OnItemClickListener {

    private static final String TAG = "ShopItemActivity";

    private TitleBarView titleBar;

    // 购物车
    private ShopCartView shopCartView;

    // ViewPager页
    private View view1, view2, view3; // 需要滑动的页卡
    private ViewPagerCompat viewPager; // viewpager
    private ViewPagerAdapter shopViewPagerAdapter;

    @SuppressWarnings("unused")
    private PagerTitleStrip pagerTitleStrip; // viewpager的标题
    private PagerTabStrip pagerTabStrip; // 一个viewpager的指示器，效果就是一个横的粗的下划线

    private List<View> viewList; // 把需要滑动的页卡添加到这个list中
    private List<String> titleList; // viewpager的标题

    // 店铺商品列表
    private ListView lvGoodsList;
    private GoodsListAdapter goodsListAdapter;


    // 店铺简介页中的控件
    private RatingBar rbShopRate; // 评分
    private TextView tvShopScrope; // 经营范围
    private TextView tvShopLoc; // 店铺地理位置
    private TextView tvShopPhone; // 店铺电话
    private TextView tvShopInfo; // 店铺简介
    private TextView tvShopSale; // 店铺促销信息
    private ImageView imgCall; // 拨打电话


    // 店铺评论
    private TextView tvShopCommentsCount; // 店铺评价条数
    private Button btnCommit;
    private ListView lvShopComments;
    private CommentsListAdapter mCommentsAdapter;
    private List<Comment> mCommentList;


    // UI测试数据
    private static List<Good> goodsList;

    private Good selectGood;

    // 当前显示店铺
    private Shop shop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_item);

        shop = ShopManager.getInstance().getSelectedShop();

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // 初始化商品页面以及适配数据
        initView();
        initGoodsDate();
        initCommentsData();
    }

    /**
     * 初始化ViewPager
     */
    private void initView() {

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        viewPager = (ViewPagerCompat) findViewById(R.id.viewpager);
        pagerTabStrip = (PagerTabStrip) findViewById(R.id.pagertab);
        pagerTabStrip.setTabIndicatorColor(this.getResources().getColor(R.color.text_color_white));
        pagerTabStrip.setDrawFullUnderline(false);
        pagerTabStrip.setTextSpacing(50);
        pagerTabStrip.setBackgroundColor(this.getResources().getColor(R.color.bg_color_first));
        pagerTabStrip.setTextColor(this.getResources().getColor(R.color.text_color_white));

        view1 = LayoutInflater.from(this)
                .inflate(R.layout.viewpager_menu, null);
        view2 = LayoutInflater.from(this).inflate(R.layout.viewpager_shopinfo,
                null);
        view3 = LayoutInflater.from(this).inflate(R.layout.viewpager_comments, null);

        initContentView();

        viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);

        titleList = new ArrayList<String>();// 每个页面的Title数据
        titleList.add("商品");
        titleList.add("店铺简介");
        titleList.add("店铺树洞");

        shopViewPagerAdapter = new ViewPagerAdapter(viewList, titleList);
        viewPager.setAdapter(shopViewPagerAdapter);
        viewPager.setCurrentItem(0);
    }


    /**
     * 初始化ViewPager内容布局
     */
    private void initContentView() {

        shopCartView = (ShopCartView) view1.findViewById(R.id.shop_cart);
        shopCartView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 进入购物车界面
                Intent i = new Intent(ShopItemActivity.this, ShopCartActivity.class);
                startActivity(i);
            }
        });

        // 商品列表页
        lvGoodsList = (ListView) view1.findViewById(R.id.lv_goods_list);
        lvGoodsList.setOnItemClickListener(this);

        // 店铺简介页
        rbShopRate = (RatingBar) view2.findViewById(R.id.rb_shop_rate);
        tvShopScrope = (TextView) view2.findViewById(R.id.tv_shop_scrope);
        tvShopInfo = (TextView) view2.findViewById(R.id.tv_shop_introduce);
        tvShopSale = (TextView) view2.findViewById(R.id.tv_shop_promotion);
        tvShopLoc = (TextView) view2.findViewById(R.id.tv_shop_location);
        tvShopPhone = (TextView) view2.findViewById(R.id.tv_shop_phone);

        rbShopRate.setRating(shop.getRates());
        tvShopScrope.setText("主营：" + shop.getScrope()); // 设置经营范围
        tvShopInfo.setText(shop.getInfo()); // 设置店铺简介
        tvShopSale.setText(shop.getSale()); // 设置店铺公告
        tvShopLoc.setText("位置：" + shop.getLocation()); // 设置店铺位置
        tvShopPhone.setText("电话：" + shop.getPhone()); // 设置店铺联系电话

        imgCall = (ImageView) view2.findViewById(R.id.img_shop_call);
        imgCall.setOnClickListener(this);

        //店铺树洞页
        initCommentsView();
    }

    private void initCommentsView() {

        mCommentList = new ArrayList<Comment>();

        tvShopCommentsCount = (TextView) view3
                .findViewById(R.id.tv_commit_count);

        btnCommit = (Button) view3.findViewById(R.id.btn_commit);
        btnCommit.setOnClickListener(this);

        lvShopComments = (ListView) view3.findViewById(R.id.lv_comments_shop);
        mCommentsAdapter = new CommentsListAdapter(this, mCommentList);
        lvShopComments.setAdapter(mCommentsAdapter);
    }


    /**
     * 获取某一商店的所有商品
     *
     * @date 2014-5-1
     * @autor Stone
     */
    private void initGoodsDate() {

        goodsList = new ArrayList<Good>();
        goodsListAdapter = new GoodsListAdapter(this, goodsList);
        lvGoodsList.setAdapter(goodsListAdapter);

        if (shop == null) {
            ToastUtils.showToast("获取当前店铺实例失败");
            return;
        }

        showProgressDialog();
        BmobQuery<Good> query = new BmobQuery<Good>();
        //判断是否有缓存
        boolean isCache = query.hasCachedResult(this, Good.class);
        if(isCache){
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
        }else{
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
        }
        query.setMaxCacheAge(360000); //缓存有效时间5分钟
        query.addWhereEqualTo("shop", shop);
        query.findObjects(this, new FindListener<Good>() {

            @Override
            public void onSuccess(List<Good> goods) {

                dismissProgressDialog();
                if (goods.size() == 0) {
                    ToastUtils.showToast("亲, 该店还没有添加商品哦");
                    return;
                }
                ToastUtils.showToast("查到" + goods.size() + "条商品");
                goodsList = goods;
                goodsListAdapter.refresh(goodsList);
            }

            @Override
            public void onError(int arg0, String arg1) {
                dismissProgressDialog();
                ToastUtils.showToast("查询失败");
            }
        });

    }


    /**
     * 加载店铺评论
     */
    private void initCommentsData() {
        showProgressDialog();
        BmobQuery<Comment> query = new BmobQuery<Comment>();
        query.addWhereEqualTo("shop", shop);
        query.addWhereEqualTo("state", Comment.COMMIT_STATE_ENABLE);  //只显示合法的评论数据
        query.include("user");
        query.findObjects(this, new FindListener<Comment>() {

            @Override
            public void onSuccess(List<Comment> list) {
                dismissProgressDialog();

                ToastUtils.showToast(String.format("查询到%d条评论", list.size()));
                updateCommentsList(list);
            }

            @Override
            public void onError(int code, String msg) {
                dismissProgressDialog();
                ToastUtils.showToast("查询店铺评论失败" + msg);
            }
        });
    }


    /**
     * 添加一条评论到评论表， 更新Comment表
     *
     * @param content
     */
    private void saveCommment(String content, boolean isShowName) {

        if (!ACLManager.checkACL(ACLManager.ACL_SHOP_COMMENT)) {
            return;
        }

        showProgressDialog();
        final Comment comment = new Comment();
        comment.setType(Comment.COMMIT_TO_SHOP);
        comment.setState(Comment.COMMIT_STATE_ENABLE);
        comment.setUser(BmobUser.getCurrentUser(this, User.class));
        comment.setContent(content);
        comment.setShowname(isShowName);
        comment.setShop(shop);
        comment.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                dismissProgressDialog();
                addCommentToShop(comment);
            }

            @Override
            public void onFailure(int code, String msg) {
                dismissProgressDialog();
                ToastUtils.showToast("很遗憾，评论添加失败 " + msg);
            }
        });
    }


    /**
     * 为添加的评论关联到店铺表中，更新Shop表
     */
    private void addCommentToShop(final Comment comment) {
        if (comment == null)
            return;

        showProgressDialog();
        BmobRelation comments = new BmobRelation();
        comments.add(comment);
        shop.setComments(comments);
        shop.update(this, new UpdateListener() {

            @Override
            public void onSuccess() {
                dismissProgressDialog();

                initCommentsData();
                ToastUtils.showToast("评论成功");
            }

            @Override
            public void onFailure(int code, String msg) {
                dismissProgressDialog();
                ToastUtils.showToast("很遗憾，评论失败 " + msg);
            }
        });
    }

    /**
     * 更新评论列表和计数
     *
     * @param list 评论列表数据
     */
    private void updateCommentsList(List<Comment> list) {
        if (list != null) {
            //评论计数器
            tvShopCommentsCount.setText("历史评价("
                    + list.size() + ")");
            if (mCommentsAdapter != null) {
                mCommentsAdapter.refresh(list);
            }
        }

    }

    /**
     * 拨打店铺服务电话
     */
    private void makePhoneCall() {
        if (!ACLManager.checkACL(ACLManager.ACL_SHOP_CALL)) {
            return;
        }

        //TODO 显示确认框， 拨打电话
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Call： (＋86) " + shop.getPhone())
                .setMessage("      确认拨打 ？     ")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToastUtils.showToast("取消拨打");
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + shop.getPhone()));
                        startActivity(intent);
                    }
                });

        //builder.create().show();
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();
        AlertDialog dialog = builder.create();
        dialog.show();
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        Point outSize = new Point();
        d.getSize(outSize);
        p.height = (int) (outSize.y * 0.6); // 高度设置为屏幕的0.6
        //p.width = (int) (d.getWidth() * 0.8);
        dialog.getWindow().setAttributes(p);
        dialogTitleLineColor(dialog);
    }


    public ShopCartView getShopCartView() {
        if (null != shopCartView) {
            return shopCartView;
        }

        return null;
    }

    public List<View> getViewList() {
        if (null != viewList) {
            return viewList;
        }

        return null;
    }


    // public void clickBuyGood(View v) {
    // Intent toOrderActivity = new Intent(ShopItemActivity.this,
    // OrderActivity.class);
    // Bundle bundle = new Bundle();
    // bundle.putSerializable("shop", shop );
    // bundle.putString("shopID", shopID); //商铺的ID需要单独传递,否则获取到的是null
    // toOrderActivity.putExtras(bundle);
    // startActivity(toOrderActivity);
    // //-------------------------------------------------------------------
    // toast("亲， 记得在弹出的对话框中选择数量哦");
    // Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
    // lvGoodsList.startAnimation(shake);
    //
    // // 显示订单对话框
    // orderDlg = new DialogOrder(this, R.style.MyDialog);
    // orderDlg.show();
    // // 得到订单对话框的View
    // LayoutInflater factory = LayoutInflater.from(this);
    // dlgOrderView = factory.inflate(R.layout.dlg_order, null);
    // tvOrderCount = (TextView) dlgOrderView
    // .findViewById(R.id.tv_order_count);
    // etOrderPhone = (EditText) dlgOrderView
    // .findViewById(R.id.et_order_phone);
    // etOrderWords = (EditText) dlgOrderView
    // .findViewById(R.id.et_order_phone);
    // ;
    // btnOrderCount = (Button) dlgOrderView
    // .findViewById(R.id.btn_order_count);
    // btnOrderSubmit = (Button) dlgOrderView
    // .findViewById(R.id.btn_order_submit);
    // btnOrderCount.setOnClickListener(this);
    // btnOrderSubmit.setOnClickListener(this);
    // //-------------------------------------------------------------------
    // }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit:
                Intent intent = new Intent(ShopItemActivity.this, CommentActivity.class);
                startActivityForResult(intent, 100);
                break;

            case R.id.img_shop_call:
                if (shop.getPhone().equals("")) {
                    ToastUtils.showToast("亲，店主好懒，木有留下电话，求别戳");
                } else {
                    makePhoneCall();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        switch (viewPager.getCurrentItem()) {
            case 0:
                break;
            case 1:
                break;
            default:
                break;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            Log.d(TAG, "开始保存评论数据");
            String content = data.getStringExtra(CommentActivity.EXTRA_KAY_CONTENT);
            boolean isShowName = data.getBooleanExtra(CommentActivity.EXTRA_KEY_IS_SHOW_NAME, false);
            saveCommment(content, isShowName);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_shop_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_star:
                //TODO 店铺收藏
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("ShopItemActivity"); //统计页面
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("ShopItemActivity"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
        MobclickAgent.onPause(this);
    }
}
