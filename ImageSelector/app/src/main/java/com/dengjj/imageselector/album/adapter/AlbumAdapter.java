package com.dengjj.imageselector.album.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.dengjj.imageselector.R;
import com.dengjj.imageselector.album.util.ToastUtil;
import com.dengjj.imageselector.album.util.ViewHolder;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 邓俊杰 on 2017/2/27.
 */

public class AlbumAdapter extends BaseAdapter {
    private Context context;
    //图片路径
    private List<String> imagPaths;
    //文件夹路径
    public static String dirPath;
    private LayoutInflater inflater;

    /**
     * 用户选择的图片，存储为图片的完整路径
     */
    public static List<String> selectedImgs = new LinkedList<String>();

    public AlbumAdapter(Context context, List<String> imagPaths) {
        this.context = context;
        this.imagPaths = imagPaths;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return imagPaths.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_gv_album_display,null);
        }

        final ImageView iv = (ImageView) ViewHolder.get(convertView, R.id.iv_item_image);
        final ImageButton ibSelect = (ImageButton) ViewHolder.get(convertView,R.id.ib_item_select);

        iv.setImageResource(R.mipmap.pictures_no);
        ibSelect.setImageResource(R.mipmap.picture_unselected);
        //通过Glide加载图片
        final String imageName = imagPaths.get(position);
        //本地文件
        File file = new File(dirPath, imageName);
        //通过Glide加载图片
        Glide.with(context).load(file).into(iv);

        iv.setColorFilter(null);
        //设置ImageView的点击事件
        iv.setOnClickListener(new View.OnClickListener()
        {
            //选择，则将图片变暗，反之则反之
            @Override
            public void onClick(View v)
            {
                //已超过最大张数，并且已选则照片中不包括点击的这张照片
                if(selectedImgs.size()==8 && !selectedImgs.contains(dirPath + "/" + imageName)){
                    ToastUtil.showToast(context,"已超过最大张数");
                    return;
                }

                // 已经选择过该图片
                if (selectedImgs.contains(dirPath + "/" + imageName))
                {
                    selectedImgs.remove(dirPath + "/" + imageName);
                    ibSelect.setImageResource(R.mipmap.picture_unselected);
                    iv.setColorFilter(null);
                } else
                // 未选择该图片
                {
                    selectedImgs.add(dirPath + "/" + imageName);
                    ibSelect.setImageResource(R.mipmap.pictures_selected);
                    iv.setColorFilter(Color.parseColor("#77000000"));
                }

            }
        });

        /**
         * 已经选择过的图片，显示出选择过的效果
         */
        if (selectedImgs.contains(dirPath + "/" + imageName))
        {
            ibSelect.setImageResource(R.mipmap.pictures_selected);
            iv.setColorFilter(Color.parseColor("#77000000"));
        }else {
            ibSelect.setImageResource(R.mipmap.picture_unselected);
            iv.setColorFilter(null);
        }

        return convertView;
    }
}
