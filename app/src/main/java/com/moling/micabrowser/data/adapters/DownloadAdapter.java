package com.moling.micabrowser.data.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.moling.micabrowser.R;
import com.moling.micabrowser.data.models.DownloadModel;

import java.util.List;

public class DownloadAdapter extends BaseAdapter {
    private List<DownloadModel> mData;
    private LayoutInflater mInflater;
    public DownloadAdapter(LayoutInflater inflater, List<DownloadModel> data) {
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
        @SuppressLint({"ViewHolder", "InflateParams"}) View viewDownload = mInflater.inflate(R.layout.widget_download, null);
        // 获取 Download 对象
        DownloadModel download = mData.get(position);
        // 获得自定义布局中每一个控件的对象
        TextView text_title = viewDownload.findViewById(R.id.text_title);
        ProgressBar progress_down = viewDownload.findViewById(R.id.progress_down);
        // 将数据添加到布局
        text_title.setText(download.getName());
        progress_down.setProgress((int) download.getProgress());
        return viewDownload;
    }

    public void update(List<DownloadModel> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        // TODO Auto-generated method stub
        super.notifyDataSetChanged();
    }
}
