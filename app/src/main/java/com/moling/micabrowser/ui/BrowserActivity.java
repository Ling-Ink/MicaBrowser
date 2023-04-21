package com.moling.micabrowser.ui;

import android.os.Bundle;
import android.util.Log;

import com.moling.micabrowser.databinding.ActivityBrowserBinding;

import org.xwalk.core.XWalkActivity;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkView;

public class BrowserActivity extends XWalkActivity {
    private ActivityBrowserBinding binding;
    private XWalkView mXWalkView;
    @Override
    protected void onXWalkReady() {
        mXWalkView.loadUrl("https://cn.bing.com");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBrowserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mXWalkView = binding.xwalkview;

        mXWalkView.setResourceClient(new XWalkResourceClient(this.mXWalkView) {
            @Override
            public void onProgressChanged(XWalkView xWalkView, int i) {
                Log.d("Progress", String.valueOf(i));
            }
        });
    }
}
