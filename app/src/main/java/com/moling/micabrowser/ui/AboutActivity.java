package com.moling.micabrowser.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.widget.TextView;

import com.moling.micabrowser.R;
import com.moling.micabrowser.databinding.ActivityAboutBinding;

public class AboutActivity extends Activity {
    private ActivityAboutBinding binding;
    private TextView mTextVersion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 控件绑定
        mTextVersion = binding.textVersion;

        // 获取程序版本
        try {
            PackageInfo info = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
            mTextVersion.setText(info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // 从 assets 中读取更新日志
        findViewById(R.id.about_note).setOnClickListener(view -> {
            Intent noteIntent = new Intent(this, NoteActivity.class);
            startActivity(noteIntent);
        });

        // Github 地址
        findViewById(R.id.about_github).setOnClickListener(view -> {
            Message githubMsg = new Message();
            githubMsg.obj = "https://github.com/MicaProject/MicaBrowser";
            MainActivity.search.sendMessage(githubMsg);
        });

        // MicaProject 地址
        findViewById(R.id.about_project).setOnClickListener(view -> {
            Message projectMsg = new Message();
            projectMsg.obj = "https://github.com/MicaProject";
            MainActivity.search.sendMessage(projectMsg);
        });
    }
}
