package com.stone.shop.base.application;

import com.androidquery.AQuery;
import com.bmob.BmobConfiguration;
import com.bmob.BmobPro;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.bugly.crashreport.CrashReport;

public class ShopApplication extends BaseApplication {

    private static ImageLoader imageLoader;
    private static AQuery aq;

    @Override
    public void onCreate() {
        super.onCreate();
        //initImageLoader(getApplicationContext());

        initAQuery();
        initBmobProFile();
        initBugly();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        // 整体摧毁的时候调用这个方法
    }

    public static AQuery getAQuery() {
        if(aq == null) {
            aq = new AQuery(getAppContext());
        }
        return aq;
    }

    private void initAQuery() {
        aq = new AQuery(this);
    }

    private void initBmobProFile() {
        //Bmob 自定义文件保存路径
        BmobConfiguration config = new BmobConfiguration.Builder(this).customExternalCacheDir("Shop/bmob").build();
        BmobPro.getInstance(this).initConfig(config);
    }

    private void initBugly() {
        CrashReport.initCrashReport(this, "900011135", false);
    }

/*    *//**
     * 初始化ImageLoader
     *//*
    public static void initImageLoader(Context context) {
        File cacheDir = StorageUtils
                .getOwnCacheDirectory(context, "Shop/Cache");// 获取到缓存的目录地址
        Log.d("cacheDir", cacheDir.getPath());
        // 创建配置ImageLoader(所有的选项都是可选的,只使用那些你真的想定制)，这个可以设定在APPLACATION里面，设置为全局的配置参数
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                // .memoryCacheExtraOptions(480, 800) // max width, max
                // height，即保存的每个缓存文件的最大长宽
                // .discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75,
                // null) // Can slow ImageLoader, use it carefully (Better don't
                // use it)设置缓存的详细信息，最好不要设置这个
                .threadPoolSize(3)
                        // 线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                        // .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 *
                        // 1024)) // You can pass your own memory cache
                        // implementation你可以通过自己的内存缓存实现
                        // .memoryCacheSize(2 * 1024 * 1024)
                        // /.discCacheSize(50 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())// 将保存的时候的URI名称用MD5
                        // 加密
                        // .discCacheFileNameGenerator(new
                        // HashCodeFileNameGenerator())//将保存的时候的URI名称用HASHCODE加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                        // .discCacheFileCount(100) //缓存的File数量
                .discCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
                        // .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                        // .imageDownloader(new BaseImageDownloader(context, 5 * 1000,
                        // 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        imageLoader = ImageLoader.getInstance();// 全局初始化此配置
        imageLoader.init(config);
    }

    public ImageLoader getImageLoader() {
        if (imageLoader != null) {
            return imageLoader;
        }

        initImageLoader(getApplicationContext());

        if (imageLoader != null) {
            return imageLoader;
        } else {
            return null;
        }
    }*/

}
