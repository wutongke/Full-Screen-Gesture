package com.zaot.fullscreengesture.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.zaot.fullscreengesture.R;
import com.zaot.fullscreengesture.databinding.OperationFragmentBinding;
import com.zaot.fullscreengesture.floatwidowpermission.FloatWindowPermissionHelper;
import com.zaot.fullscreengesture.service.FullScreenAccessibilityService;
import com.zaot.fullscreengesture.service.FullScreenGestureService;
import com.zaot.fullscreengesture.utils.OpenAccessibilitySettingHelper;

public class InstrumentFragment extends Fragment {

    private OperationFragmentBinding operationFragmentBinding;
    public View.OnClickListener requestFloatWindowPermission;
    public View.OnClickListener requestAccessibilityPermission;
    public View.OnClickListener startService;
    public View.OnClickListener stopService;

    public String fLoatWindowPermissionStatus;
    public String gestureAccessibilityAStatus;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        operationFragmentBinding =
            DataBindingUtil.inflate(getLayoutInflater(), R.layout.operation_fragment, null, false);
        return operationFragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setupClick();
        setupPermissionStatus();
        operationFragmentBinding.setModel(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        setupPermissionStatus();
        operationFragmentBinding.setModel(this);
    }

    private void setupClick() {
        startService = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!OpenAccessibilitySettingHelper.isAccessibilitySettingsOn(getActivity(),
                                                                              FullScreenAccessibilityService.class.getName())) {
                    Toast.makeText(getActivity(), R.string.need_accessibility, Toast.LENGTH_SHORT).show();
                } else if (!FloatWindowPermissionHelper.getInstance()
                                                       .checkPermission(getActivity().getApplicationContext())) {
                    Toast.makeText(getActivity(), R.string.need_float_window_permission, Toast.LENGTH_SHORT).show();
                } else {
                    getActivity().startService(new Intent(getActivity(), FullScreenAccessibilityService.class));
                    Intent floatIntent = new Intent(getActivity(), FullScreenGestureService.class);
                    floatIntent.putExtra(FullScreenGestureService.ACTION, FullScreenGestureService.SHOW);
                    getActivity().startService(floatIntent);
                    Toast.makeText(getActivity(), R.string.gesture_service_on, Toast.LENGTH_SHORT).show();
                }
            }
        };

        stopService = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent floatIntent = new Intent(getActivity(), FullScreenGestureService.class);
                floatIntent.putExtra(FullScreenGestureService.ACTION, FullScreenGestureService.SHOW);
                getActivity().stopService(floatIntent);
            }
        };

        requestFloatWindowPermission = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FloatWindowPermissionHelper floatWindowPermissionHelper = FloatWindowPermissionHelper.getInstance();
                floatWindowPermissionHelper.applyOrShowFloatWindow(v.getContext());
            }
        };

        requestAccessibilityPermission = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!OpenAccessibilitySettingHelper.isAccessibilitySettingsOn(getActivity(),
                                                                              FullScreenAccessibilityService.class.getName())) {
                    OpenAccessibilitySettingHelper.jumpToSettingPage(getActivity());
                } else {
                    Toast.makeText(getActivity().getApplicationContext(),
                                   R.string.request_accessibility_success,
                                   Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private void setupPermissionStatus() {
        gestureAccessibilityAStatus = OpenAccessibilitySettingHelper.isAccessibilitySettingsOn(getActivity(),
                                                                                               FullScreenAccessibilityService.class
                                                                                                   .getName())
                                      ?
                                      getResources().getString(R.string.statu_on)
                                      :
                                      getResources().getString(R.string.status_off);

        fLoatWindowPermissionStatus = FloatWindowPermissionHelper.getInstance().checkPermission(getContext())
                                      ?
                                      getResources().getString(R.string.statu_on)
                                      :
                                      getResources().getString(R.string.status_off);
    }
}
