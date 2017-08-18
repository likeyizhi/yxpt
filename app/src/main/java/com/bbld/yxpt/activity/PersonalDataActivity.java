package com.bbld.yxpt.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bbld.yxpt.R;
import com.bbld.yxpt.base.BaseActivity;
import com.bbld.yxpt.base.Constants;
import com.bbld.yxpt.bean.NickName;
import com.bbld.yxpt.bean.UserInfo;
import com.bbld.yxpt.loadingdialog.WeiboDialogUtils;
import com.bbld.yxpt.network.RetrofitService;
import com.bbld.yxpt.utils.MyToken;
import com.bbld.yxpt.utils.SettingImage;
import com.bbld.yxpt.utils.UploadUtil;
import com.bumptech.glide.Glide;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 个人信息
 * Created by likey on 2017/7/1.
 */

public class PersonalDataActivity extends BaseActivity{
    @BindView(R.id.ivHead)
    ImageView ivHead;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvSex)
    TextView tvSex;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.llChangePwd)
    LinearLayout llChangePwd;
    @BindView(R.id.btnOut)
    Button btnOut;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.llNickName)
    LinearLayout llNickName;
    @BindView(R.id.llSex)
    LinearLayout llSex;

    private UserInfo.UserInfoUserInfo userInfo;
    private Dialog loadDialog;
    private String[] items = new String[]{"选择本地图片", "拍照"};
    private static final int SELECT_PIC_KITKAT = 49;
    private static final int IMAGE_REQUEST_CODE = 50;
    private static final int CAMERA_REQUEST_CODE = 51;
    private static final int RESULT_REQUEST_CODE = 52;
    private static final String IMAGE_FILE_NAME = "file_img.jpg";
    private String file_imgPath;
    private String request;
    private String token;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 111:
                    try {
                        showToast("修改头像成功");
                        loadData();
                    }catch (Exception e){
                        showToast(someException());
                    }
                    break;
                case 222:
                    showToast("修改头像失败,错误信息:"+request);
                    break;
            }
        }
    };

    @Override
    protected void initViewsAndEvents() {
        token=new MyToken(PersonalDataActivity.this).getToken();
        loadData();
        setListeners();
    }

    private void setListeners() {
        btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    new MyToken(PersonalDataActivity.this).delToken();
                    new MyToken(PersonalDataActivity.this).delSPHeadPortrait();
                    SharedPreferences sharedAP=getSharedPreferences("YXAP",MODE_PRIVATE);
                    SharedPreferences.Editor editorAP = sharedAP.edit();
                    editorAP.putString("YXACC","");
                    editorAP.putString("YXPWD","");
                    editorAP.commit();
                    ActivityManagerUtil.getInstance().finishActivity(PersonalDataActivity.this);
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ActivityManagerUtil.getInstance().finishActivity(PersonalDataActivity.this);
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
        llChangePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Bundle bundle=new Bundle();
                    bundle.putString("bandPhone", userInfo.getMobile());
                    readyGo(UpdatePasswordActivity.class,bundle);
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
        ivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    showHeadDialog();
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
        llNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    showNickNamedialog();
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
        llSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    showSexDialog();
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
    }

    private void showSexDialog() {
        new AlertDialog.Builder(this)
                .setTitle("修改性别")
                .setPositiveButton("男", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            changeSex("男");
                            dialogInterface.dismiss();
                        }catch (Exception e){
                            showToast(someException());
                        }
                    }
                })
                .setNegativeButton("女", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        try {
                            changeSex("女");
                            dialogInterface.dismiss();
                        }catch (Exception e){
                            showToast(someException());
                        }
                    }
                }).show();
    }

    private void changeSex(String sex) {
        try {
            Call<NickName> call=RetrofitService.getInstance().updateUserSex(token, sex);
            call.enqueue(new Callback<NickName>() {
                @Override
                public void onResponse(Response<NickName> response, Retrofit retrofit) {
                    if (response==null){
                        showToast(responseFail());
                        return;
                    }
                    if (response.body().getStatus()==0){
                        try {
                            showToast("修改成功");
                            loadData();
                        }catch (Exception e){
                            showToast(someException());
                        }
                    }else{
                        showToast(response.body().getMes());
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {

                }
            });
        }catch (Exception e){
            showToast(someException());
        }
    }

    private void showNickNamedialog() {
        final EditText et = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("修改姓名")
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            String newNick = et.getText().toString().trim();
                            changeNick(newNick);
                        }catch (Exception e){
                            showToast(someException());
                        }
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void changeNick(String newNick) {
        Call<NickName> call=RetrofitService.getInstance().updateUserNickName(token, newNick);
        call.enqueue(new Callback<NickName>() {
            @Override
            public void onResponse(Response<NickName> response, Retrofit retrofit) {
                if (response==null){
                    showToast(responseFail());
                    return;
                }
                if (response.body().getStatus()==0){
                    showToast("修改成功");
                    loadData();
                }else{
                    showToast(response.body().getMes());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void loadData() {
        try {
            loadDialog= WeiboDialogUtils.createLoadingDialog(PersonalDataActivity.this, "加载中...");
            Call<UserInfo> call= RetrofitService.getInstance().getUserInfo(new MyToken(PersonalDataActivity.this).getToken());
            call.enqueue(new Callback<UserInfo>() {
                @Override
                public void onResponse(Response<UserInfo> response, Retrofit retrofit) {
                    if (response==null){
                        showToast(responseFail());
                        return;
                    }
                    if (response.body().getStatus()==0){
                        try {
                            userInfo=response.body().getUserInfo();
                            SharedPreferences shared=getSharedPreferences("YXToken",MODE_PRIVATE);
                            SharedPreferences.Editor editor=shared.edit();
                            editor.putString("HeadPortrait",response.body().getUserInfo().getHeadPortrait());
                            editor.commit();
                            setData();
                        }catch (Exception e){
                            showToast(someException());
                            WeiboDialogUtils.closeDialog(loadDialog);
                        }
                    }else{
                        WeiboDialogUtils.closeDialog(loadDialog);
                        showToast(response.body().getMes());
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {
                    WeiboDialogUtils.closeDialog(loadDialog);
                }
            });
        }catch (Exception e){
            showToast(someException());
        }
    }

    private void setData() {
        try {
            Glide.with(getApplicationContext()).load(userInfo.getHeadPortrait()).error(R.mipmap.head).into(ivHead);
            tvName.setText(userInfo.getNickName()+"");
            tvPhone.setText(userInfo.getMobile()+"");
            tvSex.setText(userInfo.getSex()+"");
            WeiboDialogUtils.closeDialog(loadDialog);
        }catch (Exception e){
            showToast(someException());
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 结果码不等于取消时候
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE :
                    startPhotoZoom(data.getData());
                    break;
                case SELECT_PIC_KITKAT :
                    startPhotoZoom(data.getData());
                    break;
                case CAMERA_REQUEST_CODE :
                    // 判断存储卡是否可以用，可用进行存储
                    String state = Environment.getExternalStorageState();
                    if (state.equals(Environment.MEDIA_MOUNTED)) {
                        File path = Environment
                                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                        File tempFile = new File(path, IMAGE_FILE_NAME);
                        startPhotoZoom(Uri.fromFile(tempFile));
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case RESULT_REQUEST_CODE : // 图片缩放完成后
                    if (data != null) {
                        getImageToView(data);
                    }
                    break;
            }
        }
    }
    private void getImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            SettingImage settingImage = new SettingImage(photo, "file_img");
            file_imgPath=settingImage.imagePath();
            final Map<String, String> params = new HashMap<String, String>();
            params.put("token", new MyToken(PersonalDataActivity.this).getToken()+"");
            final Map<String, File> files = new TreeMap<String, File>();
            final String requestURL = Constants.BASE_URL+"/api/User/UpdateUserFace?token="+new MyToken(PersonalDataActivity.this).getToken();
            if (!file_imgPath.equals("")) {
                files.put("file_img", new File(file_imgPath));
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        request = UploadUtil.post(requestURL, params, files);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if ((request+"").contains("成功")) { // 请求成功
                        Message message=new Message();
                        message.what=111;
                        handler.sendMessage(message);
                    } else { // 请求失败
                        Message message=new Message();
                        message.what=222;
                        handler.sendMessage(message);
                    }
                }
            }).start();
        }
    }


    private void startPhotoZoom(Uri uri) {
        if (uri == null) {
//            Log.i("tag", "The uri is not exist.");
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            String url=getPath(PersonalDataActivity.this,uri);
            intent.setDataAndType(Uri.fromFile(new File(url)), "image/jpeg");
        }else{
            intent.setDataAndType(uri, "image/jpeg");
        }
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }
    /**
     * 修改头像
     * 显示选择对话框
     */
    private void showHeadDialog() {
        new AlertDialog.Builder(this)
                .setTitle("修改方式")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0 :
                                Intent intentFromGallery = new Intent();
                                intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
                                intentFromGallery.addCategory(Intent.CATEGORY_OPENABLE);
                                intentFromGallery.setType("image/jpeg"); // 设置文件类型
                                if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.KITKAT){
                                    startActivityForResult(intentFromGallery, SELECT_PIC_KITKAT);
                                }else{
                                    startActivityForResult(intentFromGallery,IMAGE_REQUEST_CODE);
                                }
                                break;
                            case 1 :
                                Intent intentFromCapture = new Intent(
                                        MediaStore.ACTION_IMAGE_CAPTURE);
                                // 判断存储卡是否可以用，可用进行存储
                                String state = Environment
                                        .getExternalStorageState();
                                if (state.equals(Environment.MEDIA_MOUNTED)) {
                                    File path = Environment
                                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                                    File file = new File(path, IMAGE_FILE_NAME);
                                    intentFromCapture.putExtra(
                                            MediaStore.EXTRA_OUTPUT,
                                            Uri.fromFile(file));
                                }
                                startActivityForResult(intentFromCapture,
                                        CAMERA_REQUEST_CODE);
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
    /**
     * <br>功能简述:4.4及以上获取图片的方法
     * <br>功能详细描述:
     * <br>注意:
     * @param context
     * @param uri
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_personaldata;
    }
}
