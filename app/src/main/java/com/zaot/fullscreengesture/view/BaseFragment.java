package com.zaot.fullscreengesture.view;

import android.support.v4.app.Fragment;
import com.umeng.analytics.MobclickAgent;

public abstract class BaseFragment extends Fragment{
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getPageKey());
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getPageKey());
    }

    protected abstract String getPageKey();
}
