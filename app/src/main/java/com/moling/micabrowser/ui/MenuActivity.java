package com.moling.micabrowser.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.moling.micabrowser.adapters.HistoryAdapter;
import com.moling.micabrowser.browser.menu.Menu_Adapter;
import com.moling.micabrowser.browser.menu.Menu_Listener;
import com.moling.micabrowser.databinding.ActivityMenuBinding;
import com.moling.micabrowser.models.HistoryModel;
import com.moling.micabrowser.utils.Constants;

public class MenuActivity extends Activity {
    public static MenuActivity menuActivity;

    private ActivityMenuBinding binding;
    private ListView mListMenu;
    private String menuType;

    private AdapterView.OnItemClickListener listener;
    private HistoryAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        menuActivity = this;

        // 控件绑定
        mListMenu = binding.listMenu;
        // 菜单类型
        menuType = getIntent().getData().toString();

        switch (menuType) {
            case Constants.MENU_TYPE_HISTORY:
                adapter = new HistoryAdapter(getLayoutInflater(), Menu_Adapter.HistoryAdapter());
                listener = Menu_Listener.HistoryListener(adapter);
        }

        mListMenu.setAdapter(adapter);
        mListMenu.setOnItemClickListener((adapterView, view, i, l) -> listener.onItemClick(adapterView, view, i, l));
    }
}
