package com.moling.micabrowser.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.moling.micabrowser.databinding.ActivityMainBinding;
import com.moling.micabrowser.databinding.ActivityMenuBinding;

public class MenuActivity extends Activity {

    private ActivityMenuBinding binding;
    private ListView mListMenu;
    private String menuType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 控件绑定
        mListMenu = binding.listMenu;
        // 菜单类型
        menuType = getIntent().getData().toString();

        mListMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }
}
