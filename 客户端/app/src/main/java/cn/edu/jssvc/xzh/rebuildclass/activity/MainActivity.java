package cn.edu.jssvc.xzh.rebuildclass.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;

import cn.edu.jssvc.xzh.rebuildclass.R;
import cn.edu.jssvc.xzh.rebuildclass.fragment.ExerciseFragment;
import cn.edu.jssvc.xzh.rebuildclass.fragment.ForumFragment;
import cn.edu.jssvc.xzh.rebuildclass.fragment.ProjectFragment;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by xzh on 2017/1/16.
 * 使用FragmentTabHost进行页面加载
 * 导航图片、文字以及Fragment全部存放到数组中，通过判断点击，加载对应的图片文字和Fragment
 * 使用ToolBar设置标题栏
 * 使用DrawerLayout添加侧边栏
 * 使用NavigationView设置侧边栏样式
 * <p>
 * 注：原始让MainActivity 继承与FragmentActivity，现由于使用ToolBar，需要继承自AppCompatActivity
 * 由于AppCompatActivity里已包含FragmentActivity，所以完全不需要改动
 */
public class MainActivity extends BaseActivity {
    private static final int TAKE_PHOTO = 0x00;
    private static final int CROP_PHOTO = 0x01;
    private static final int CHOOSE_PHOTO = 0x02;
    private static final int UPLOAD_SUCCEED = 0x03;
    private static final int UPLOAD_FAILED = 0x04;
    private String[] mTagStrings;
    private int[] images;
    private Class<?>[] fragments;
    private FragmentTabHost mTabHost;
    private int[] imagesLight;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private String idFromLog;
    private String nameFromLog;
    private String imageFromLog;
    private ImageView main_user_image;
    private TextView main_username;
    private Uri imageUri;
    private SharedPreferences mSharedPreferences;
    private File outputImage;
    private File file;
    private Handler mHandler;
    private Thread mThread;
    private static final String URL = "http://118.190.10.181:8080/YDKT/uploadUserImg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniData();
        getImageAndName();
        initView();

        setMenuBtn();
        setAside();
        addTabs();
        setListener();
        handleMessage();
    }

    /**
     * 消息处理
     */
    private void handleMessage() {
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case UPLOAD_SUCCEED:
                        closeProgressDialog();
                        showDialog(getResources().getString(R.string.update_succeed));
                        break;
                    case UPLOAD_FAILED:
                        closeProgressDialog();
                        showDialog(getResources().getString(R.string.update_failed));
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 获取用户名和头像到侧边栏顶部
     */
    private void getImageAndName() {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        idFromLog = mSharedPreferences.getString("idFromLog", "");
        nameFromLog = mSharedPreferences.getString("nameFromLog", "");
        imageFromLog = mSharedPreferences.getString("imageFromLog", "");
        Log.e("imageFromLog", imageFromLog);
    }

    /**
     * 设置侧边栏
     */
    private void setAside() {
        navigationView.setCheckedItem(R.id.nav_editpass);   //设置默认选中
        //设置点击事件
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (String.valueOf(item)) {
                   /* case "个人信息":
                        Intent myInfoIntent = new Intent(MainActivity.this, MyInfoActivity.class);
                        startActivity(myInfoIntent);
                        break;*/
                    case "修改密码":
                        Intent resetPassIntent = new Intent(MainActivity.this, ResetPassActivity.class);
                        startActivity(resetPassIntent);
                        break;
                    case "我的课程":
                        Intent myClassIntent = new Intent(MainActivity.this, MyClassActivity.class);
                        startActivity(myClassIntent);
                        break;
                    case "与我相关":
                        Intent myForumIntent = new Intent(MainActivity.this, MyForumActivity.class);
                        startActivity(myForumIntent);
                        break;
                    case "设置":
                        Intent settingIntent = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(settingIntent);
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "" + item, Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 设置菜单按钮
     */
    private void setMenuBtn() {
        ActionBar actionBar = getSupportActionBar();//得到ActionBar实例
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//设置导航按钮显示
            actionBar.setHomeAsUpIndicator(R.mipmap.menu);//默认为返回按钮，修改为菜单按钮
        }
    }

    /**
     * 初始化数据
     */
    private void iniData() {
        // 获取Tag数组
        mTagStrings = getResources().getStringArray(R.array.tag);
        // 创建Tag图片数组1（未点击）
        imagesLight = new int[]{R.mipmap.tab_1_pressed, R.mipmap.tab_2_pressed,
                R.mipmap.tab_3_pressed};
        // 创建Tag图片数组2（点击）
        images = new int[]{R.mipmap.tab_1_normal, R.mipmap.tab_2_normal,
                R.mipmap.tab_3_normal};
        // 创建Fragment数组
        fragments = new Class<?>[]{ProjectFragment.class,
                ExerciseFragment.class, ForumFragment.class};
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        // 设置标题栏样式
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //侧边栏
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        // 去掉底部导航栏分割线
        mTabHost.getTabWidget().setDividerDrawable(null);

        // 将用户名和头像加载到控件中
        View headerView = navigationView.getHeaderView(0);
        main_user_image = (ImageView) headerView.findViewById(R.id.main_user_image);
        main_username = (TextView) headerView.findViewById(R.id.main_username);

        main_username.setText(nameFromLog);
        Glide.with(this).load(imageFromLog).into(main_user_image);

        // 更换头像，从相册或者拍照中获取
        resetPicture();

    }

    /**
     * 更换头像
     */
    private void resetPicture() {
        main_user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }

    /**
     * 添加视图和指示器
     */
    private void addTabs() {
        for (int i = 0; i < mTagStrings.length; i++) {
            TabSpec tag = mTabHost.newTabSpec(mTagStrings[i]).setIndicator(
                    getTabItemView(i));
            mTabHost.addTab(tag, fragments[i], null);
        }
    }

    /**
     * 给Tab按钮设置图标和文字
     * 加载底部导航栏
     * 根据当前的界面显示导航图片
     */
    private View getTabItemView(int index) {
        View indicatorView = LayoutInflater.from(this).inflate(
                R.layout.tab_layout, mTabHost.getTabWidget(), false);
        ImageView imageView = (ImageView) indicatorView.findViewById(R.id.tab_icon);
        TextView textView = (TextView) indicatorView.findViewById(R.id.tab_text);
        textView.setText(mTagStrings[index]);

        if (index == 0) {
            imageView.setImageResource(imagesLight[index]);
        } else {
            imageView.setImageResource(images[index]);
        }
        return indicatorView;
    }

    /**
     * 添加监听器
     * 根据用户的点击判断导航按钮，并切换导航按钮图片以及标题
     */
    private void setListener() {
        TabHost.OnTabChangeListener mListener = new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {
                TabWidget tabw = mTabHost.getTabWidget();
                for (int i = 0; i < tabw.getChildCount(); i++) {
                    View view = tabw.getChildAt(i);
                    ImageView iv = (ImageView) view.findViewById(R.id.tab_icon);

                    if (i == mTabHost.getCurrentTab()) {
                        iv.setImageResource(imagesLight[i]);
                    } else {
                        iv.setImageResource(images[i]);
                    }
                }
            }
        };
        mTabHost.setOnTabChangedListener(mListener);
    }

    /**
     * 选择对话框
     */
    private void showDialog() {
        final String[] items = getResources().getStringArray(R.array.picture_item);
        new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.select))
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String image = items[i];
                        if (image.equals("用相机拍摄照片")) {
                            createFile();
                            if (Build.VERSION.SDK_INT >= 24) {
                                imageUri = FileProvider.getUriForFile(MainActivity.this, "cn.edu.jssvc.xzh.rebuildclass.fileprovider", outputImage);
                            } else {
                                imageUri = Uri.fromFile(outputImage);
                            }
                            openCamera();
                        } else {
                            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                            }
                            openAlbum();
                        }
                    }
                }).show();
    }

    /**
     * 创建File对象，存储拍照后的图片
     */
    private void createFile() {
        outputImage = new File(Environment.getExternalStorageDirectory(), "output_image.jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 打开相机
     */
    private void openCamera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    // 打开相册
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
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        String imagePath = Environment.getExternalStorageDirectory() + "/output_image.jpg";
                        Log.e("----->", imagePath);
                        clearGlide();
                        Glide.with(MainActivity.this).load(imagePath).into(main_user_image);
                        getFile(imagePath);
                    } catch (Exception e) {
                        e.printStackTrace();
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
        String imagePath = null;
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
        getFile(imagePath);
    }

    /**
     * 获取到图片文件并上传
     */
    private void getFile(String imagePath) {
        // 对图片进行压缩
        file = new File(imagePath);
        if (!file.exists()) {
            Toast.makeText(this, "文件不存在", Toast.LENGTH_SHORT).show();
        } else {
            showProgressDialog(getResources().getString(R.string.upload_wait));
            new Thread(runnable).start();
        }
    }


    Runnable runnable=new Runnable() {
        @Override
        public void run () {
            try {
                OkHttpClient client = new OkHttpClient();
                RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("id", idFromLog)
                        .addFormDataPart("image", "test.jpg", fileBody)
                        .build();
                Request request = new Request.Builder()
                        .url(URL)
                        .post(requestBody)
                        .build();
                Response response = client.newCall(request).execute();
                String responseData = response.body().string();
                if (responseData.equals("200")) {
                    mHandler.sendEmptyMessage(UPLOAD_SUCCEED);
                } else {
                    Log.e("----->", responseData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * android4.4以下
     * 图片处理
     *
     * @param data
     */

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
        getFile(imagePath);
    }

    /**
     * 图片显示
     *
     * @param imagePath
     */
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            main_user_image.setImageBitmap(bitmap);
        } else {
            Toast.makeText(MainActivity.this, "---", Toast.LENGTH_SHORT).show();
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

    /**
     * 点击导航栏菜单按钮弹出侧边栏
     * 默认按钮的id为android.R.id.home
     * 调用DrawerLayout的openDrawer()方法将滑动菜单展示出来
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

    //双击退出--------------------------------------------------------------------------------------
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
            exit();
        } else {
            return super.dispatchKeyEvent(event);
        }
        return false;
    }

    private boolean isExit;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(MainActivity.this, "再按一次退出我的应用", Toast.LENGTH_SHORT).show();
            handler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
        }
    }
}
