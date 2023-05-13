package com.moling.micabrowser.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.moling.micabrowser.R;
import com.moling.micabrowser.utils.Constants;

import java.util.HashMap;
import java.util.Map;

public class SettingAdapter extends BaseAdapter {
    String[] title;
    String[] type;
    String[] key;

    LayoutInflater inflater;
    TextView mTextTitle;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch mSwitch;
    ImageView mNext;
    TextView mText;

    public SettingAdapter(LayoutInflater inflater, String[] title, String[] type, String[] key) {
        this.inflater = inflater;
        this.title = title;
        this.type = type;
        this.key = key;
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public Map<String, String> getItem(int position) {
        Map<String, String> map = new HashMap<>();
        map.put("title", title[position]);
        map.put("type", type[position]);
        map.put("key", key[position]);
        return map;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder") View layoutView = inflater.inflate(R.layout.widget_setting, (ViewGroup) null);

        this.mTextTitle = layoutView.findViewById(R.id.setting_title);
        this.mSwitch = layoutView.findViewById(R.id.setting_switch);
        this.mNext = layoutView.findViewById(R.id.setting_next);
        this.mText = layoutView.findViewById(R.id.setting_text);

        mSwitch.setVisibility(View.GONE);
        mNext.setVisibility(View.GONE);
        mText.setVisibility(View.GONE);

        mTextTitle.setText(title[position]);
        switch (type[position]) {
            case Constants.SETTING_TYPE_SWITCH:
                mSwitch.setVisibility(View.VISIBLE);
                break;
            case Constants.SETTING_TYPE_NEXT:
                mNext.setVisibility(View.VISIBLE);
                break;
            case Constants.SETTING_TYPE_TEXT:
                mText.setVisibility(View.VISIBLE);
                break;
        }
        return layoutView;
    }
}
