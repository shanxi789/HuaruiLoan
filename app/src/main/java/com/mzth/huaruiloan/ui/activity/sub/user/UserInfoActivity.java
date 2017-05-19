package com.mzth.huaruiloan.ui.activity.sub.user;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mzth.huaruiloan.R;
import com.mzth.huaruiloan.common.Constans;
import com.mzth.huaruiloan.ui.activity.base.BaseBussActivity;
import com.mzth.huaruiloan.util.CustomUtil;
import com.mzth.huaruiloan.util.FileStorage;
import com.mzth.huaruiloan.util.ToastUtil;
import com.mzth.huaruiloan.widget.CircleImageView;
import com.yanzhenjie.permission.AndPermission;

import java.io.File;

/**
 * Created by Administrator on 2017/5/11.
 * 用户资料页面
 */

public class UserInfoActivity extends BaseBussActivity {
    private RelativeLayout rl_user_head,rl_reset_pwd,rl_user_aboutus;
    private TextView tv_user_phone,tv_user_name,tv_title;
    private CircleImageView iv_info_head;
    private ImageView iv_back;
    private String imagepath,cachPath,HeadUrl;
    private File cacheFile;
    private Uri imageUri;
    @Override
    protected void setCustomLayout(Bundle savedInstanceState) {
        super.setCustomLayout(savedInstanceState);
        _context = UserInfoActivity.this;
        setContentView(R.layout.activity_user_info);
    }

    @Override
    protected void initView() {
        super.initView();
        //返回键
        iv_back = (ImageView) findViewById(R.id.iv_back);
        //标题
        tv_title = (TextView) findViewById(R.id.tv_big_title);
        //头像布局
        rl_user_head = (RelativeLayout) findViewById(R.id.rl_user_head);
        //修改密码
        rl_reset_pwd = (RelativeLayout) findViewById(R.id.rl_reset_pwd);
        //关于我们
        rl_user_aboutus = (RelativeLayout) findViewById(R.id.rl_user_aboutus);
        //用户昵称
        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        //用户手机号
        tv_user_phone = (TextView) findViewById(R.id.tv_user_phone);
        //头像
        iv_info_head = (CircleImageView) findViewById(R.id.iv_info_head);
    }

    @Override
    protected void initData() {
        super.initData();
        tv_title.setText("我的资料");
    }

    @Override
    protected void BindComponentEvent() {
        super.BindComponentEvent();
        rl_user_head.setOnClickListener(myonclick);
        rl_reset_pwd.setOnClickListener(myonclick);
        rl_user_aboutus.setOnClickListener(myonclick);
        //iv_info_head.setOnClickListener(myonclick);
        iv_back.setOnClickListener(myonclick);
    }

    private View.OnClickListener myonclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                switch (v.getId()){
                    case R.id.iv_back://返回键
                        onBackPressed();
                        break;
                    case R.id.rl_user_head://头像布局
                        if(CustomUtil.sdCardExist()){//判断sd卡是否存在
                            //showHeadDialog();
                            showDialog();
                            cachPath = Constans.PATH_HEAD + System.currentTimeMillis()+ ".jpg";
                            cacheFile = new File(cachPath);
                        }else {
                            ToastUtil.showShort(_context, "SD卡没有检测到，不能操作哦~");
                        }
                        break;
                    case R.id.rl_reset_pwd://修改密码
                        startActivity(UpdatePwdActivity.class,null);
                        break;
                    case R.id.rl_user_aboutus://关于我们

                        break;
//                    case R.id.iv_info_head://修改头像
//
//                        break;
                }
        }
    };
    //仿QQ点击头像弹出的对话框
    private void showDialog() {
        View view = getLayoutInflater().inflate(R.layout.photo_choose_dialog, null);
        final Dialog dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        //图库
        Button imgcatch = (Button) view.findViewById(R.id.btn__userhead_catch);
        //拍照
        Button imgPhoto = (Button) view.findViewById(R.id.btn_userhead_photo);
        //取消
        Button cancle = (Button) view.findViewById(R.id.btn_cancle);
        //拍照的点击事件
        imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isGetPermission = AndPermission.hasPermission(_context, Manifest.permission.CAMERA);
                if (!isGetPermission) {
                    AndPermission.defaultSettingDialog(_context, 1).show();
                }else{
                    openCamera();
                }
                dialog.dismiss();
            }
        });
        //相册的点击事件
        imgcatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到本地相册
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
                dialog.dismiss();
            }
        });
        //取消的点击事件
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    //拍照
    private void openCamera(){
        Intent intent = new Intent();
        File file = new FileStorage().createIconFile();
        imagepath = Constans.PATH_HEAD + System.currentTimeMillis() + ".jpg";
        if (Build.VERSION.SDK_INT >= 24) {  //针对Android7.0，需要通过FileProvider封装过的路径，提供给外部调用
            imageUri =  FileProvider.getUriForFile(_context, "com.sd.parentsofnetwork.fileprovider", new File(imagepath));//通过FileProvider创建一个content类型的Uri，进行封装
        } else { //7.0以下，如果直接拿到相机返回的intent值，拿到的则是拍照的原图大小，很容易发生OOM，所以我们同样将返回的地址，保存到指定路径，返回到Activity时，去指定路径获取，压缩图片
            imageUri = Uri.fromFile(new File(imagepath));
        }
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
        startActivityForResult(intent, 1);//启动拍照
    };

    @Override
    protected void doActivityResult(int requestCode, Intent intent) {
        super.doActivityResult(requestCode, intent);
        switch (requestCode){
            case 1 ://拍照
                startPhotoZoom(new File(imagepath));
                break;
            case 2 ://本地相册返回
                // 判断手机系统版本号
                if (Build.VERSION.SDK_INT >= 19) {
                    // 4.4及以上系统使用这个方法处理图片
                    handleImageOnKitKat(intent);
                } else {
                    // 4.4以下系统使用这个方法处理图片
                    handleImageBeforeKitKat(intent);
                }
                break;
            case 3 ://裁剪
                if(new File(cachPath).exists()){
                    HeadUrl = cachPath;
                    Glide.with(_context).load(HeadUrl).crossFade().centerCrop().into(iv_info_head);
                }
                break;
        }
    }

    /**
     * 相册图片回调
     * android 4.4之后调用
     * @param data
     * @return
     */
    private void handleImageOnKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath= uriToPath(uri);
        startPhotoZoom(new File(imagePath));
    }
    /**
     * 相册图片回调
     * android 4.4之前调用
     * @param data
     */
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        startPhotoZoom(new File(imagePath));
    }
    /**
     * 适配 android 7.0 的裁剪图片方法实现
     * @param file
     */
    public void startPhotoZoom(File file) {
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            if (Build.VERSION.SDK_INT >= 24){
                intent.setDataAndType(getImageContentUri(this,file), "image/*");//自己使用Content Uri替换File Uri
            }else{
                intent.setDataAndType(Uri.fromFile(file), "image/*");//自己使用Content Uri替换File Uri
            }
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 150);
            intent.putExtra("outputY", 150);
            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(cacheFile));//定义输出的File Uri
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            startActivityForResult(intent, 3);
        } catch (ActivityNotFoundException e) {
            String errorMessage = "Your device doesn't support the crop action!";
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取符合 android 7.0 的 Content Uri
     * @param context
     * @param imageFile
     * @return
     */
    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * 将Uri转换成Path
     * android 4.4之后调用
     * @param uri
     * @return
     */
    private String uriToPath(Uri uri) {
        String path=null;
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                path = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                path = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            path = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            path = uri.getPath();
        }
        return  path;
    }
    /**
     * 将Uri转换成Path
     * android 4.4之前调用
     * @param uri
     * @param selection
     * @return
     */
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("cachPath",cachPath);
        setResult(RESULT_OK);
        super.onBackPressed();
    }
}
