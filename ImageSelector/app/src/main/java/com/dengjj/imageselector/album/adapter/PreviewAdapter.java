package com.dengjj.imageselector.album.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by 邓俊杰 on 2017/2/28.
 */

public class PreviewAdapter extends PagerAdapter {

    /**
     * 装ImageView数组
     */
    private List<ImageView> mImageViews;


    public PreviewAdapter(List<ImageView> mImageViews) {
        this.mImageViews = mImageViews;

    }





    @Override
    public int getItemPosition(Object object)   {
            return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mImageViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    /**
     * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
     */

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        try {
            ((ViewPager)container).addView(mImageViews.get(position % getCount()), 0);
        }catch(Exception e){
            //handler something
        }
        return mImageViews.get(position);
    }


    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView(mImageViews.get(position % getCount()));

    }





}
