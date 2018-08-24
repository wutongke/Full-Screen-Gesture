package com.zaot.fullscreengesture;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;
import com.zaot.fullscreengesture.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding activityMainBinding;
    public View.OnClickListener startService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_main, null, false);
        setContentView(activityMainBinding.getRoot());
        setupClick();
        activityMainBinding.setModel(this);
    }

    private void setupClick() {
        startService = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!OpenAccessibilitySettingHelper.isAccessibilitySettingsOn(MainActivity.this,
                                                                              FullScreenAccessibilityService.class.getName())){// 判断服务是否开启
                    OpenAccessibilitySettingHelper.jumpToSettingPage(MainActivity.this);// 跳转到开启页面
                }else {
                    Toast.makeText(MainActivity.this, "服务已开启", Toast.LENGTH_SHORT).show();
                    MainActivity.this.startService(new Intent(MainActivity.this, FullScreenAccessibilityService.class));

                    Intent floatIntent = new Intent(MainActivity.this, FullScreenService.class);
                    floatIntent.putExtra(FullScreenService.ACTION, FullScreenService.SHOW);
                    MainActivity.this.startService(floatIntent);
                }
            }
        };
    }
}
