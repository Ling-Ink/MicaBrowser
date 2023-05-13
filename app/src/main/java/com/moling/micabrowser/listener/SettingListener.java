package com.moling.micabrowser.listener;

import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.util.Log;
import android.widget.AdapterView;

import com.moling.micabrowser.adapters.SettingAdapter;
import com.moling.micabrowser.ui.MainActivity;
import com.moling.micabrowser.ui.MenuActivity;
import com.moling.micabrowser.utils.Config;
import com.moling.micabrowser.utils.Constants;

import java.util.Objects;

public class SettingListener {

    public static AdapterView.OnItemClickListener SettingClickListener(SettingAdapter adapter) {
        AdapterView.OnItemClickListener listener = (parent, view, position, id) -> {
            Log.d("[Mica]", "<SettingClickListener> | Setting item clicked");
            // 子菜单
            if (Objects.requireNonNull(adapter.getItem(position).get("type")).equals(Constants.SETTING_TYPE_NEXT)) {
                Intent menuIntent = new Intent(MainActivity.mainActivity, MenuActivity.class);
                menuIntent.setData(Uri.parse(adapter.getItem(position).get("key")));
                MenuActivity.menuActivity.finish();
                MainActivity.mainActivity.startActivity(menuIntent);
            } else {
                // 操作项
                switch (Objects.requireNonNull(adapter.getItem(position).get("key"))) {
                    case "UsageReport": // 使用报告
                        Message adapterMsg = new Message();
                        String[] settingTitle = new String[] { "搜索引擎", "App Center 使用报告" };
                        String[] settingType = new String[] { Constants.SETTING_TYPE_NEXT, Constants.SETTING_TYPE_SWITCH};
                        String[] settingKey = new String[] { "SearchEngine", "UsageReport" };
                        Object[] settingParam = new Object[]{ null, !Config.getUsageReport() };

                        adapterMsg.obj = new SettingAdapter(MainActivity.mainActivity.getLayoutInflater(), settingTitle, settingType, settingKey, settingParam);
                        MenuActivity.setSettingAdapter.sendMessage(adapterMsg);

                        Config.setUsageReport(!Config.getUsageReport());
                        break;
                }
            }
        };
        return listener;
    }

    public static AdapterView.OnItemClickListener SearchEngineClickListener() {
        AdapterView.OnItemClickListener listener = (parent, view, position, id) -> {
            Log.d("[Mica]", "<SearchEngineClickListener> | SearchEngine item clicked");

            Message adapterMsg = new Message();
            String[] settingTitle = new String[] { "必应", "百度" };
            String[] settingType = new String[] { Constants.SETTING_TYPE_RADIO, Constants.SETTING_TYPE_RADIO};
            String[] settingKey = new String[] { "bing", "baidu" };
            Object[] settingParam = new Object[]{ false, false };
            settingParam[position] = true;

            adapterMsg.obj = new SettingAdapter(MainActivity.mainActivity.getLayoutInflater(), settingTitle, settingType, settingKey, settingParam);
            MenuActivity.setSettingAdapter.sendMessage(adapterMsg);

            Config.setSearchEngine(settingKey[position]);
        };
        return listener;
    }
}
