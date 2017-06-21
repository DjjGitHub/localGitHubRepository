package com.dengjj.imageselector.album.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;


import com.dengjj.imageselector.R;
import com.dengjj.imageselector.album.adapter.AlbumAdapter;
import com.dengjj.imageselector.album.bean.ImageFloder;
import com.dengjj.imageselector.album.task.ATask;
import com.dengjj.imageselector.album.task.ITaskCallBack;

import java.io.File;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 邓俊杰 on 2017/2/27.
 */

public class AlbumActivity extends AppCompatActivity implements View.OnClickListener,ITaskCallBack {
    @Bind(R.id.tv_head_left)
    TextView tvLeft;
    @Bind(R.id.tv_head_title)
    TextView tvTitle;
    @Bind(R.id.tv_head_right)
    TextView tvRight;
    @Bind(R.id.tv_comfirm)
    TextView tvComfirm;
    @Bind(R.id.tv_preview)
    TextView tvPreview;
    @Bind(R.id.gv_album_display)
    GridView gvAlbumDisplay;

    private ProgressDialog mProgressDialog;
    private AlbumAdapter adapter;
    //数据源
    //图片路径
    private List<String> imagPaths;
    //文件夹路径
    private String dirPath;
    /**
     * 所有的图片
     */
    private List<String> mImgs;

    /**
     * 扫描拿到所有的图片文件夹
     */
    private List<ImageFloder> mImageFloders;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_album);
        ButterKnife.bind(this);
        init();
        //检查权限
        checkAlbumPermission();
//        loadImags();
        addListener();

    }

    private void loadImags() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED))
        {
            Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }
        // 显示进度条
        mProgressDialog = ProgressDialog.show(this, null, "正在加载...");
        ATask loadImagsTask = new ATask(this,this);
        loadImagsTask.execute();

    }



    private void checkAlbumPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(AlbumActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(AlbumActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},111);
                return;
            }else{
                loadImags();
            }
        } else {
            loadImags();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            //就像onActivityResult一样这个地方就是判断你是从哪来的。
            case 333:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    loadImags();
                } else {
                    // Permission Denied
                    Toast.makeText(AlbumActivity.this, "请开启访问内存信息权限。", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void addListener() {
        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvComfirm.setOnClickListener(this);
        tvPreview.setOnClickListener(this);
    }

    private void init() {
        tvLeft.setText("相册");
        tvRight.setText("取消");
        tvTitle.setText("相机胶卷");
        imagPaths = new ArrayList<String>();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_head_left:
                //跳转到相册首页界面
                Intent intent = new Intent(AlbumActivity.this,PhotosActivity.class);
                intent.putExtra("mImageFloders",(Serializable) mImageFloders);
                startActivityForResult(intent,100);

                break;
            case R.id.tv_head_right:
                Intent intent3 = new Intent(AlbumActivity.this,MainActivity.class);
                AlbumAdapter.selectedImgs.clear();
                startActivity(intent3);
                break;
            case R.id.tv_preview:
                Intent intent1 = new Intent(AlbumActivity.this,PreviewActivity.class);
                startActivity(intent1);
                break;
            case R.id.tv_comfirm:

//                ToastUtil.showToast(this,AlbumAdapter.selectedImgs.size()+"");
                Intent intent2 = new Intent(AlbumActivity.this,MainActivity.class);
                startActivity(intent2);

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case 100:
                    int position = data.getIntExtra("position",0);
                    ImageFloder imageFloder = mImageFloders.get(position);
                    //更换相册
                    changeAlbum(imageFloder);
                    break;
            }
        }
    }

    /**
     * 更换相册
     * @param imageFloder
     */
    private void changeAlbum(ImageFloder imageFloder) {
        File mImgDir = new File(imageFloder.getDir());
        mImgs = Arrays.asList(mImgDir.list(new FilenameFilter()
        {
            @Override
            public boolean accept(File dir, String filename)
            {
                if (filename.endsWith(".jpg") || filename.endsWith(".png")
                        || filename.endsWith(".jpeg"))
                    return true;
                return false;
            }
        }));
        imagPaths.clear();
        imagPaths.addAll(mImgs);
        AlbumAdapter.dirPath = mImgDir.getAbsolutePath();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void getResult(int totalCount, List<ImageFloder> mImageFloders, int mPicsSize, File mImgDir) {
        mProgressDialog.dismiss();
        this.mImageFloders = mImageFloders;
        //展示数据
        if (mImgDir == null)
        {
            Toast.makeText(getApplicationContext(), "对不起，一张图片没扫描到",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        imagPaths.addAll(Arrays.asList(mImgDir.list()));
        AlbumAdapter.dirPath = mImgDir.getAbsolutePath();
        adapter = new AlbumAdapter(AlbumActivity.this, imagPaths);
        gvAlbumDisplay.setAdapter(adapter);


    }

}
