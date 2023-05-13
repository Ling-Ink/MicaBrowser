package com.moling.micabrowser.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
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
    Object[] param;

    LayoutInflater inflater;
    TextView mTextTitle;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch mSwitch;
    RadioButton mRadio;
    ImageView mNext;
    TextView mText;

    public SettingAdapter(LayoutInflater inflater, String[] title, String[] type, String[] key, Object[] param) {
        this.inflater = inflater;
        this.title = title;
        this.type = type;
        this.key = key;
        this.param = param;
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
        this.mRadio = layoutView.findViewById(R.id.setting_radio);
        this.mNext = layoutView.findViewById(R.id.setting_next);
        this.mText = layoutView.findViewById(R.id.setting_text);

        mSwitch.setVisibility(View.GONE);
        mRadio.setVisibility(View.GONE);
        mNext.setVisibility(View.GONE);
        mText.setVisibility(View.GONE);

        switch (type[position]) {
            case Constants.SETTING_TYPE_SWITCH:
                mTextTitle.setText(title[position]);
                mSwitch.setVisibility(View.VISIBLE);
                mSwitch.setChecked((boolean) param[position]);
                break;
            case Constants.SETTING_TYPE_RADIO:
                mRadio.setVisibility(View.VISIBLE);
                mRadio.setText(title[position]);
                mRadio.setChecked((boolean) param[position]);
                break;
            case Constants.SETTING_TYPE_NEXT:
                mTextTitle.setText(title[position]);
                mNext.setVisibility(View.VISIBLE);
                break;
            case Constants.SETTING_TYPE_TEXT:
                mTextTitle.setText(title[position]);
                mText.setVisibility(View.VISIBLE);
                mText.setText((String) param[position]);
                break;
        }
        return layoutView;
    }
}
