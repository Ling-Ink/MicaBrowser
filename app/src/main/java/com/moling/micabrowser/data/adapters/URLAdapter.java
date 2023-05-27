package com.moling.micabrowser.data.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.moling.micabrowser.R;
import com.moling.micabrowser.data.models.DownloadModel;
import com.moling.micabrowser.data.models.URLModel;

import java.util.List;

public class URLAdapter extends BaseAdapter {
    private List<URLModel> mData;
    private LayoutInflater mInflater;
    public URLAdapter(LayoutInflater inflater, List<URLModel> data) {
        this.mInflater = inflater;
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint({"ViewHolder", "InflateParams"}) View viewHistory = mInflater.inflate(R.layout.widget_url, null);
        // 获取 history 对象
        URLModel history = mData.get(position);
        // 获得自定义布局中每一个控件的对象
        TextView text_title = viewHistory.findViewById(R.id.text_title);
        TextView text_url = viewHistory.findViewById(R.id.text_url);
        // 将数据添加到布局
        text_title.setText(history.getTitle());
        text_url.setText(history.getUrl());
        return viewHistory;
    }

    public void update(List<URLModel> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        // TODO Auto-generated method stub
        super.notifyDataSetChanged();
    }
}
