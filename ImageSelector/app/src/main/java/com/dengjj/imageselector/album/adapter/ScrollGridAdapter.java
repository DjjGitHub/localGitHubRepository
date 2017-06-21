package com.dengjj.imageselector.album.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dengjj.imageselector.R;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 邓俊杰 on 2017/2/28.
 */

public class ScrollGridAdapter extends BaseAdapter{
    private Context context;
    private List<String> datas;
    private LayoutInflater inflater;

    private HashMap<Integer, View> viewMap = new HashMap<>();

    public ScrollGridAdapter(Context context, List<String> datas) {
        this.context = context;
        this.datas = datas;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if(datas.size() == 8){
            return 8;
        }else {
            return datas.size()+1;
        }
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        Holder holder;
        if(!viewMap.containsKey(position) || viewMap.get(position) == null){
            holder = new Holder();
            convertView = inflater.inflate(R.layout.item_published_grida, null);
            holder.imageview = (ImageView) convertView.findViewById(R.id.item_grida_image);
            convertView.setTag(holder);
            viewMap.put(position, convertView);
        }else{
            convertView = viewMap.get(position);
            holder = (Holder) convertView.getTag();
        }



        if(datas.size()>0 && position != datas.size()){
                File file = new File(datas.get(position));
                Glide.with(context).load(file).into(holder.imageview);
            }else {
                holder.imageview.setImageResource(R.mipmap.icon_addpic_unfocused);
            }
        return convertView;
    }

    class Holder{
         ImageView imageview;
    }
}
