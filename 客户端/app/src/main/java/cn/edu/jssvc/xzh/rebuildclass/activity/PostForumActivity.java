package cn.edu.jssvc.xzh.rebuildclass.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.edu.jssvc.xzh.rebuildclass.R;
import cn.edu.jssvc.xzh.rebuildclass.util.CompressionUtil;
import cn.edu.jssvc.xzh.rebuildclass.util.HttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by xzh on 2017/2/12.
 * <p>
 * 发帖界面
 * 获取到当前用户的用户名以及输入的帖子标题和内容
 * 发送网络请求，将数据保存到数据库中
 */

public class PostForumActivity extends BaseActivity {
    private static final int CHOOSE_PHOTO = 0x00;
    private String idFromLog;
    private String nameFromLog;
    private String imageFromLog;
    private ImageView submit;
    // 发送帖子的网络请求地址
    private String URL = "http://118.190.10.181:8080/YDKT/insertForumList";
    private String URL_PIC = "http://118.190.10.181:8080/YDKT/insertForumListpic";
    private EditText f_title;
    private EditText f_content;
    private ImageView forum_image;
    private static final int SUCCESS_TEXT = 200;
    private static final int FAILED_TEXT = 500;

    // 使用异步消息处理机制，解决Android中不允许在子线程中进行UI操作的问题
    // Handler接收到发送的数据并交由handlermessage()方法进行处理
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS_TEXT:
                    closeProgressDialog();
                    showDialog2(getResources().getString(R.string.post_success));
                    break;
                case FAILED_TEXT:
                    closeProgressDialog();
                    showDialog2(getResources().getString(R.string.post_failed));
                    break;
                default:
                    break;
            }
        }
    };
    private File file;
    private String imagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_post_forum);
        initMenu();
        getDataFromShared();
        initView();
    }

    /**
     * 初始化控件、提交按钮
     */
    private void initView() {
        f_title = (EditText) findViewById(R.id.edit_title);
        f_content = (EditText) findViewById(R.id.edit_content);
        forum_image = (ImageView) findViewById(R.id.forum_image);
        forum_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(PostForumActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PostForumActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
                openAlbum();
            }
        });
        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = f_title.getText().toString();
                String content = f_content.getText().toString();
                if (title.equals("") || title == null) {
                    Toast.makeText(PostForumActivity.this, "请输入标题", Toast.LENGTH_SHORT).show();
                } else if (content.equals("") || content == null) {
                    Toast.makeText(PostForumActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                } else if (!getFile()) {
                    sendRequestWithOkHttp();
                } else if (getFile()) {
                    sendRequestWithImage();
                }
            }
        });
    }

    /**
     * 带图片的网络请求
     */
    private void sendRequestWithImage() {
        showProgressDialog(getResources().getString(R.string.upload_wait));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("user_id", idFromLog)
                            .addFormDataPart("f_title", f_title.getText().toString())
                            .addFormDataPart("f_content", f_content.getText().toString())
                            .addFormDataPart("image", "forum.jpg", fileBody)
                            .build();
                    Request request = new Request.Builder()
                            .url(URL_PIC)
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    if (responseData.equals("200")) {
                        handler.sendEmptyMessage(SUCCESS_TEXT);
                    } else {
                        handler.sendEmptyMessage(FAILED_TEXT);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 打开相册，获取图片
     */
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    /**
     * 回调函数
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitKat(data);
                    } else {
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Anroid4.4以上版本调用
     *
     * @param data
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data) {

        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloadd.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }
        displayImage(imagePath);

    }

    /**
     * android4.4以下
     * 图片处理
     *
     * @param data
     */

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    /**
     * 获取到图片文件并上传
     */
    private boolean getFile() {
        boolean isExist = false;
        if (!imagePath.equals("")) {
            // 对图片进行压缩
            Bitmap bm = CompressionUtil.getSmallBitmap(imagePath);
            FileOutputStream out = null;
            file = new File(imagePath);
            try {
                out = new FileOutputStream(file);
                bm.compress(Bitmap.CompressFormat.JPEG, 100, out);

                if (!file.exists()) {
                    isExist = false;
                } else {
                    isExist = true;
                    file = new File(imagePath);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return isExist;
    }

    /**
     * 图片显示
     *
     * @param imagePath
     */
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            forum_image.setImageBitmap(bitmap);
        } else {
            Toast.makeText(PostForumActivity.this, "---", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取图片路径
     *
     * @param uri
     * @param selection
     * @return
     */
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 发布帖子的网络请求
     */
    private void sendRequestWithOkHttp() {
        showProgressDialog(getResources().getString(R.string.upload_wait));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody requestBody = new FormBody.Builder()
                            .add("user_id", idFromLog)
                            .add("f_title", f_title.getText().toString())
                            .add("f_content", f_content.getText().toString())
                            .build();
                    HttpUtil.sendOkHttpRequest(URL, requestBody, new Callback() {

                        /**
                         * 请求失败
                         */
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        /**
                         * 请求成功
                         */
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String resp = response.body().string();
                            Message message = new Message();
                            if (resp.equals("500")) {
                                message.what = FAILED_TEXT;
                            } else if (resp.equals("200")) {
                                message.what = SUCCESS_TEXT;
                            }
                            handler.sendMessage(message);// 将message发送到Handler
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 获取share存储的id等数据
     */
    private void getDataFromShared() {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        idFromLog = mSharedPreferences.getString("idFromLog", "");
        nameFromLog = mSharedPreferences.getString("nameFromLog", "");
        imageFromLog = mSharedPreferences.getString("imageFromLog", "");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "DENIED", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
