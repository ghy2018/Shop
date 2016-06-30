package com.stone.shop.user.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.androidquery.AQuery;
import com.bmob.BTPFileResponse;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;
import com.stone.shop.R;
import com.stone.shop.main.ui.activity.LoginActivity;
import com.stone.shop.base.ui.BaseActivity;
import com.stone.shop.hbut.ui.activity.AuthHBUTActivity;
import com.stone.shop.base.config.BmobConfig;
import com.stone.shop.base.config.MessageType;
import com.stone.shop.user.model.User;
import com.stone.shop.base.util.ToastUtils;
import com.stone.shop.base.util.CameraPhotoUtil;
import com.umeng.analytics.MobclickAgent;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 修改个人资料卡
 *
 * @author Stone
 * @date 2014-5-28
 */
public class MineInfoEditActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "MineInfoEditActivity";

    private final int RESULT_REQUEST_PHOTO = 1005;
    private final int RESULT_REQUEST_PHOTO_CROP = 1006;
    private final int RESULT_REQUEST_AUTH_HBUT = 1007;
    private final int RESULT_REQUEST_DOR_PART = 1008;
    private final int RESULT_REQUEST_DOR_NUM = 1009;
    private final int RESULT_REQUEST_PHONE = 1010;
    private final int RESULT_REQUEST_QQ = 1011;

    private Uri fileUri;
    private Uri fileCropUri;

    private User curUser;

    private AQuery aq;

    @SuppressWarnings("unused")
    private Bundle bundle;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MessageType.MINE_INFO_FINISH_FIND_USER:
                    initView();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_info_edit);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initView();
    }

    private void initView() {

//        aq = ShopApplication.getAQuery();
        aq = new AQuery(this);

        curUser = BmobUser.getCurrentUser(this, User.class);
        if (curUser == null)
            return;

        aq.id(R.id.et_mineinfo_username).text(curUser.getNickname());
        aq.id(R.id.et_mineinfo_sex).text(curUser.getSex());
        aq.id(R.id.et_mineinfo_stuid).text(curUser.getStuID());
        aq.id(R.id.et_mineinfo_school).text(curUser.getSchool());
        aq.id(R.id.et_mineinfo_cademy).text(curUser.getCademy());
        aq.id(R.id.et_mineinfo_class).text(curUser.getClassName());

        if(!curUser.getClassName().equals(""))
            aq.id(R.id.et_mineinfo_time).text("20"+curUser.getClassName().substring(0, 2));
        aq.id(R.id.et_mineinfo_dorpart).text(curUser.getDorPart());
        aq.id(R.id.et_mineinfo_dornum).text(curUser.getDorNum());
        aq.id(R.id.et_mineinfo_phone).text(curUser.getPhone());
        aq.id(R.id.et_mineinfo_qq).text(curUser.getQQ());

        if (null != curUser.getPicUser())
            aq.id(R.id.img_mine_info_icon).image(curUser.getPicUser().getFileUrl(this));
        else
            aq.id(R.id.img_mine_info_icon).image(R.drawable.ic_xiaocai_weixin);

        aq.id(R.id.rl_mine_info_icon).clicked(this);
        aq.id(R.id.rl_mine_info_username).clicked(this);
        aq.id(R.id.rl_mine_info_sex).clicked(this);
        aq.id(R.id.rl_mine_info_stuid).clicked(this);
        aq.id(R.id.rl_mine_info_school).clicked(this);
        aq.id(R.id.rl_mine_info_cademy).clicked(this);
        aq.id(R.id.rl_mine_info_dorpart).clicked(this);
        aq.id(R.id.rl_mine_info_dornum).clicked(this);
        aq.id(R.id.rl_mine_info_phone).clicked(this);
        aq.id(R.id.rl_mine_info_qq).clicked(this);
    }

    /**
     * 设置头像
     */
    private void setIcon() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("更换头像")
                .setItems(R.array.camera_gallery, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            camera();
                        } else {
                            photo();
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        dialogTitleLineColor(dialog);
    }


    private void refreshUserIcon() {
        aq.id(R.id.img_mine_info_icon).image(curUser.getPicUser().getUrl());
    }


    /**
     * 打开相机
     */
    private void camera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = CameraPhotoUtil.getOutputMediaFileUri();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, RESULT_REQUEST_PHOTO);
    }

    /**
     * 打开相册
     */
    private void photo() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_REQUEST_PHOTO);
    }


    /**
     * 打开图片裁剪
     *
     * @param uri
     * @param outputUri
     * @param outputX
     * @param outputY
     * @param requestCode
     */
    private void cropImageUri(Uri uri, Uri outputUri, int outputX, int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, requestCode);
    }


    /**
     * 性别
     */
    private void setSexs() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("性别")
                .setItems(R.array.sexs, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        curUser.setSex(getResources().getStringArray(R.array.sexs)[which]);
                        aq.id(R.id.et_mineinfo_sex).text(curUser.getSex());
                    }
                });
        //builder.create().show();
        AlertDialog dialog = builder.create();
        dialog.show();
        dialogTitleLineColor(dialog);
    }

    /**
     * 绑定学号
     */
    private void setStuID() {
        if (curUser.getStuID().equals("")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // 绑定学号
                    Intent toAuthHbut = new Intent(MineInfoEditActivity.this, AuthHBUTActivity.class);
                    startActivityForResult(toAuthHbut, RESULT_REQUEST_AUTH_HBUT);
                }
            }, 1000);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("警告").setMessage("当前小菜账号已经绑定学号 " + curUser.getStuID() + "是否重新绑定 ?")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ToastUtils.showToast("取消绑定");
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 绑定学号
                            Intent toAuthHbut = new Intent(MineInfoEditActivity.this, AuthHBUTActivity.class);
                            startActivityForResult(toAuthHbut, RESULT_REQUEST_AUTH_HBUT);
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
            dialogTitleLineColor(dialog);
        }
    }

    /**
     * 学校
     */
    private void showSchooolsDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("学校")
                .setItems(R.array.schools, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //user.job = which;
                        //action_done();
                        curUser.setSchool(getResources().getStringArray(R.array.schools)[which]);
                        aq.id(R.id.et_mineinfo_school).text(curUser.getSchool());
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

    /**
     * 学院
     */
    private void showCademyDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("学院")
                .setItems(R.array.cademys, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //user.job = which;
                        //action_done();
                        curUser.setCademy(getResources().getStringArray(R.array.cademys)[which]);
                        aq.id(R.id.et_mineinfo_cademy).text(curUser.getCademy());
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


    /**
     * 保存用户个人信息
     */
    private void saveUserInfo() {
        if (curUser == null) {
            ToastUtils.showToast("请先登录");
            Intent toLogin = new Intent(MineInfoEditActivity.this, LoginActivity.class);
            startActivity(toLogin);
            finish();
        } else {
            Log.i("当前用户的ID: ", curUser.getObjectId());
            curUser.update(this, curUser.getObjectId(), new UpdateListener() {

                @Override
                public void onSuccess() {
                    Intent back = new Intent(MineInfoEditActivity.this, MineInfoActivity.class);
                    setResult(RESULT_OK, back);  //返回成功码
                    finish();
                    ToastUtils.showToast("资料修改成功");
                }

                @Override
                public void onFailure(int arg0, String arg1) {
                    ToastUtils.showToast("资料修改失败: " + arg0 + "  " + arg1);
                }
            });
        }

    }


    /**
     * 上传用户头像
     *
     * @param filePath
     */
    private void uploadFile(String filePath) {

        showProgressDialog();
        BTPFileResponse response = BmobProFile.getInstance(this).upload(filePath, new UploadListener() {

            @Override
            public void onSuccess(String fileName, String url, BmobFile bmobFile) {
                dismissProgressDialog();
                String URL = BmobProFile.getInstance(MineInfoEditActivity.this).signURL(fileName, url, BmobConfig.BMOB_ACCESS_KEY, 0, null);
                bmobFile.setUrl(URL);
                curUser.setPicUser(bmobFile);
                refreshUserIcon();
                ToastUtils.showToast("文件已上传成功：" + fileName);
                Log.d(TAG, "filename: " + fileName + " url: " + url);
            }

            @Override
            public void onProgress(int ratio) {
                // TODO Auto-generated method stub
                Log.d(TAG, "MainActivity -onProgress :" + ratio);
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                // TODO Auto-generated method stub
                dismissProgressDialog();
                ToastUtils.showToast("上传出错：" + errormsg);
            }
        });

    }


    private void setDorPart() {
        Intent intent = new Intent(MineInfoEditActivity.this, InputRowActivity.class);
        intent.putExtra(InputRowActivity.KEY_INPUT_TYPE, InputRowActivity.TYPE_INPUT_DOR_PART);
        startActivityForResult(intent, RESULT_REQUEST_DOR_PART);
    }

    private void setDorNum() {
        Intent intent = new Intent(MineInfoEditActivity.this, InputRowActivity.class);
        intent.putExtra(InputRowActivity.KEY_INPUT_TYPE, InputRowActivity.TYPE_INPUT_DOR_NUM);
        startActivityForResult(intent, RESULT_REQUEST_DOR_NUM);
    }

    private void setPhone() {
        Intent intent = new Intent(MineInfoEditActivity.this, InputRowActivity.class);
        intent.putExtra(InputRowActivity.KEY_INPUT_TYPE, InputRowActivity.TYPE_INPUT_PHONE);
        startActivityForResult(intent, RESULT_REQUEST_PHONE);
    }

    private void setQQ() {
        Intent intent = new Intent(MineInfoEditActivity.this, InputRowActivity.class);
        intent.putExtra(InputRowActivity.KEY_INPUT_TYPE, InputRowActivity.TYPE_INPUT_QQ);
        startActivityForResult(intent, RESULT_REQUEST_QQ);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_mine_info_icon:
                setIcon();
                break;
            case R.id.rl_mine_info_sex:
                setSexs();
                break;
            case R.id.rl_mine_info_stuid:
                setStuID();
                break;
            case R.id.rl_mine_info_school:
                showSchooolsDialog();
                break;
            case R.id.rl_mine_info_cademy:
                showCademyDialog();
                break;
            case R.id.rl_mine_info_dorpart:
                setDorPart();
                break;
            case R.id.rl_mine_info_dornum:
                setDorNum();
                break;
            case R.id.rl_mine_info_phone:
                setPhone();
                break;
            case R.id.rl_mine_info_qq:
                setQQ();
                break;

            default:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_REQUEST_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    fileUri = data.getData();
                }

                fileCropUri = CameraPhotoUtil.getOutputMediaFileUri();
                cropImageUri(fileUri, fileCropUri, 640, 640, RESULT_REQUEST_PHOTO_CROP);
            }

        } else if (requestCode == RESULT_REQUEST_PHOTO_CROP) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    String filePath = CameraPhotoUtil.getPath(this, fileCropUri);
                    uploadFile(filePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == RESULT_REQUEST_AUTH_HBUT) {
            if (resultCode == Activity.RESULT_OK) {
                curUser = BmobUser.getCurrentUser(this, User.class);
                aq.id(R.id.et_mineinfo_stuid).text(curUser.getStuID());
            }
        } else if(requestCode == RESULT_REQUEST_DOR_PART) {
            if(resultCode == RESULT_OK) {
                String dorPart = data.getStringExtra(InputRowActivity.KEY_RESULT_DATA);
                curUser.setDorPart(dorPart);
                aq.id(R.id.et_mineinfo_dorpart).text(dorPart);
            }
        } else if(requestCode == RESULT_REQUEST_DOR_NUM) {
            if(resultCode == RESULT_OK) {
                String dorNum = data.getStringExtra(InputRowActivity.KEY_RESULT_DATA);
                curUser.setDorNum(dorNum);
                aq.id(R.id.et_mineinfo_dornum).text(dorNum);
            }
        } else if(requestCode == RESULT_REQUEST_PHONE) {
            if(resultCode == RESULT_OK) {
                String phone = data.getStringExtra(InputRowActivity.KEY_RESULT_DATA);
                curUser.setPhone(phone);
                aq.id(R.id.et_mineinfo_phone).text(phone);
            }
        } else if(requestCode == RESULT_REQUEST_QQ) {
            if(resultCode == RESULT_OK) {
                String qq = data.getStringExtra(InputRowActivity.KEY_RESULT_DATA);
                curUser.setQQ(qq);
                aq.id(R.id.et_mineinfo_qq).text(qq);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_mine_info_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveUserInfo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MineInfoEditActivity"); //统计页面
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MineInfoEditActivity"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
        MobclickAgent.onPause(this);
    }


}
