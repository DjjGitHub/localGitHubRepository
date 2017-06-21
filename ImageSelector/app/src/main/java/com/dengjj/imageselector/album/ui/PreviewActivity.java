package com.dengjj.imageselector.album.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dengjj.imageselector.R;
import com.dengjj.imageselector.album.adapter.AlbumAdapter;
import com.dengjj.imageselector.album.adapter.PreviewAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 邓俊杰 on 2017/2/28.
 */

public class PreviewActivity extends AppCompatActivity implements View.OnClickListener{
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.iv_delete)
    ImageView ivDelete;
    @Bind(R.id.tv_choosed_num)
    TextView tvChoosedNum;
    @Bind(R.id.tv_complete)
    TextView tvComplete;



    private PreviewAdapter adapter;

    /**
     * 装ImageView数组
     */
    private List<ImageView> mImageViews;

    private String from;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        ButterKnife.bind(this);
        init();
        addListener();

    }

    private void addListener() {
        ivBack.setOnClickListener(this);
        ivDelete.setOnClickListener(this);
        tvComplete.setOnClickListener(this);

    }

    private void init() {
        Intent intent = getIntent();
        int position = intent.getIntExtra("position",0);
        from = intent.getStringExtra("from");
        if("WorkReportDetailActivity".equals(from)){
            ivDelete.setVisibility(View.GONE);
        }
        mImageViews = new ArrayList<>();
        makeDatas();
        tvChoosedNum.setText("("+ AlbumAdapter.selectedImgs.size()+")");

        //设置Adapter
        adapter = new PreviewAdapter(mImageViews);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);



    }

    /**
     * 组装数据
     */
    private void makeDatas(){

        //将图片装载到数组中
        mImageViews.clear();
        for(int i=0; i<AlbumAdapter.selectedImgs.size(); i++){
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mImageViews.add(imageView);
            //Glide加载图片
            File file = new File(AlbumAdapter.selectedImgs.get(i));
            Glide.with(this).load(file).into(imageView);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_delete:
                int position =  viewPager.getCurrentItem();
                AlbumAdapter.selectedImgs.remove(position);
                if(AlbumAdapter.selectedImgs.size() > 0){
                    makeDatas();
                    //设置Adapter
                    adapter = new PreviewAdapter(mImageViews);
                    viewPager.setAdapter(adapter);
                    tvChoosedNum.setText("("+AlbumAdapter.selectedImgs.size()+")");
                }else{
                    //返回写日报界面
                    Intent intent1 = new Intent(PreviewActivity.this, MainActivity.class);
                    startActivity(intent1);
                }

                break;
            case R.id.tv_complete:
                if("WorkReportDetailActivity".equals(from)){
                    finish();
                }else {
                Intent intent = new Intent(PreviewActivity.this, MainActivity.class);
                startActivity(intent);
                }
                break;
        }

    }
}
