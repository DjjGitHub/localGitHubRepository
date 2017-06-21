package com.dengjj.imageselector.album.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Toast;

import com.dengjj.imageselector.R;
import com.dengjj.imageselector.album.adapter.AlbumAdapter;
import com.dengjj.imageselector.album.adapter.ScrollGridAdapter;
import com.dengjj.imageselector.album.util.FileUtils;
import com.dengjj.imageselector.album.widget.ActionSheetDialog;
import com.dengjj.imageselector.album.widget.ScrollGridView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    @Bind(R.id.noScrollgridview)
    ScrollGridView noScrollgridview;



    /**
     * 显示照片的适配器
     */
    private ScrollGridAdapter mAdapter;
    /**
     * 调用相机请求码
     */
    private final static int TAKE_PICTURE = 0x12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mAdapter = new ScrollGridAdapter(MainActivity.this, AlbumAdapter.selectedImgs);
        noScrollgridview.setAdapter(mAdapter);
        noScrollgridview.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        if (position == AlbumAdapter.selectedImgs.size()) {
            choosePhotoType();
        } else {
            //跳转到预览界面
            Intent intent = new Intent(MainActivity.this, PreviewActivity.class);
            intent.putExtra("position",position);
            startActivity(intent);
        }

    }


    /**
     * 选择照片类型
     */
    private void choosePhotoType(){
        new ActionSheetDialog(MainActivity.this)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                //判断权限
                                checkPermission();
                            }
                        })
                .addSheetItem("从相册中选取", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener(){

                            @Override
                            public void onClick(int which) {
                                //判断相册的权限
                                checkAlbumPermission();

                            }
                        })
                .show();

    }


    /**
     * 检查相册权限
     */
    private void checkAlbumPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},333);
                return;
            }else{
                Intent intent = new Intent(MainActivity.this,
                        AlbumActivity.class);
                startActivity(intent);
            }
        } else {
            Intent intent = new Intent(MainActivity.this,
                    AlbumActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            //就像onActivityResult一样这个地方就是判断你是从哪来的。
            case 222:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    openCamera();
                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "请务必开启相机权限享受我们提供的服务吧。", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case 333:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Intent intent = new Intent(MainActivity.this,
                            AlbumActivity.class);
                    startActivity(intent);
                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "请开启访问内存信息权限。", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * 选择相机权限
     */
    private void checkPermission() {

        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.CAMERA);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CAMERA},222);
                return;
            }else{
                openCamera();//调用具体方法
            }
        } else {
            openCamera();//调用具体方法
        }
    }


    /**
     * 打开摄像头
     */
    public void openCamera() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mAdapter.notifyDataSetChanged();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    if (AlbumAdapter.selectedImgs.size() <8) {

                        String fileName = String.valueOf(System.currentTimeMillis());
                        Bitmap bm = (Bitmap) data.getExtras().get("data");
                        FileUtils.saveBitmap(bm, fileName);
                        String picPath = FileUtils.SDPATH + fileName + ".JPEG";
                        AlbumAdapter.selectedImgs.add(picPath);
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    }
}
