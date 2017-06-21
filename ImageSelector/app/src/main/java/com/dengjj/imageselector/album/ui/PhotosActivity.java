package com.dengjj.imageselector.album.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import com.dengjj.imageselector.R;
import com.dengjj.imageselector.album.adapter.PhotosAdapter;
import com.dengjj.imageselector.album.bean.ImageFloder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 邓俊杰 on 2017/2/27.
 */

public class PhotosActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener{
    @Bind(R.id.tv_head_left)
    TextView tvLeft;
    @Bind(R.id.tv_head_title)
    TextView tvTitle;
    @Bind(R.id.tv_head_right)
    TextView tvRight;
    @Bind(R.id.lv_photos)
    ListView lvPhotos;

    /**
     * 扫描拿到所有的图片文件夹
     */
    private List<ImageFloder> mImageFloders;
    private PhotosAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_photos);
        ButterKnife.bind(this);
        init();
        addListener();
    }

    private void addListener() {
        tvRight.setOnClickListener(this);
        lvPhotos.setOnItemClickListener(this);
    }

    private void init() {
        tvLeft.setVisibility(View.INVISIBLE);
        tvTitle.setText("照片");
        tvRight.setText("取消");
        Intent intent = getIntent();
        mImageFloders = (ArrayList<ImageFloder>) intent.getSerializableExtra("mImageFloders");

        adapter = new PhotosAdapter(this,mImageFloders);
        lvPhotos.setAdapter(adapter);




    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_head_right:
                finish();
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = new Intent();
        intent.putExtra("position",position);
        setResult(RESULT_OK,intent);
        finish();

    }
}
