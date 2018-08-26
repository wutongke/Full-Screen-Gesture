package com.zaot.fullscreengesture.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.zaot.fullscreengesture.BuildConfig;
import com.zaot.fullscreengesture.R;
import com.zaot.fullscreengesture.databinding.AboutFragmentBinding;

public class AboutFragment extends Fragment {

    private AboutFragmentBinding aboutFragmentBinding;
    public String version;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        aboutFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.about_fragment, container, false);
        return aboutFragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        version = getResources().getString(R.string.gesture_control_version, BuildConfig.VERSION_NAME);
        aboutFragmentBinding.setModel(this);
    }
}
