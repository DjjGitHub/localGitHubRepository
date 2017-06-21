package com.dengjj.imageselector.album.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dengjj.imageselector.R;
import com.dengjj.imageselector.album.bean.ImageFloder;
import com.dengjj.imageselector.album.util.ViewHolder;

import java.io.File;
import java.util.List;

/**
 * Created by 邓俊杰 on 2017/2/27.
 */

public class PhotosAdapter extends BaseAdapter {
    private Context context;
    private List<ImageFloder> mImageFloders;
    private LayoutInflater inflater;

    public PhotosAdapter(Context context, List<ImageFloder> mImageFloders) {
        this.context = context;
        this.mImageFloders = mImageFloders;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mImageFloders.size();
    }

    @Override
    public Object getItem(int i) {
        return mImageFloders.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_my_photo,null);
        }

        ImageView ivFirstImage = (ImageView) ViewHolder.get(convertView,R.id.iv_first_img);
        TextView tvName = (TextView) ViewHolder.get(convertView,R.id.tv_photo_name);
        TextView tvNum = (TextView) ViewHolder.get(convertView,R.id.tv_photo_num);

        ImageFloder bean = mImageFloders.get(position);

        File file = new File(bean.getFirstImagePath());
        //通过Glide加载图片
        Glide.with(context).load(file).into(ivFirstImage);
        int index = bean.getName().lastIndexOf("/")+1;
        tvName.setText(bean.getName().substring(index));
        tvNum.setText("("+bean.getCount()+")");


        return convertView;
    }
}
