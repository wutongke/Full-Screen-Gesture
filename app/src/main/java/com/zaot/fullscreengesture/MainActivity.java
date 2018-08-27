package com.zaot.fullscreengesture;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.zaot.fullscreengesture.databinding.MainActivityBinding;
import com.zaot.fullscreengesture.utils.NotificationBundle;
import com.zaot.fullscreengesture.view.AboutFragment;
import com.zaot.fullscreengesture.view.GuidelineFragment;
import com.zaot.fullscreengesture.view.InstrumentFragment;

public class MainActivity extends AppCompatActivity {

    private MainActivityBinding mainActivityBinding;
    public View.OnClickListener tabClick;

    public GuidelineFragment guidelineFragment;
    public InstrumentFragment instrumentFragment;
    public AboutFragment aboutFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivityBinding = DataBindingUtil.setContentView(this, R.layout.main_activity);
        setupFragments();
        setupTabCLick();
    }

    private void setupFragments() {
        guidelineFragment = new GuidelineFragment();
        instrumentFragment = new InstrumentFragment();
        aboutFragment = new AboutFragment();
        Fragment currentFragment = guidelineFragment;
        if (getIntent() != null) {
            switch (getIntent().getIntExtra(NotificationBundle.TAB_TO_OPEN, 0)) {
                case NotificationBundle.guideline_tab:
                    currentFragment = guidelineFragment;
                    selectIcon(R.id.guideline_layout);
                    break;
                case NotificationBundle.instrument_tab:
                    currentFragment = instrumentFragment;
                    selectIcon(R.id.instrument_layout);
                    break;
                case NotificationBundle.about_tab:
                    currentFragment = aboutFragment;
                    selectIcon(R.id.about_layout);
                    break;
            }
        }
        getSupportFragmentManager().beginTransaction()
                                   .replace(R.id.main_fragment, currentFragment)
                                   .commit();
    }

    private void selectIcon(int tabId) {
        switch (tabId) {
            case R.id.guideline_layout:
                mainActivityBinding.guidelineIcon.setSelected(true);
                mainActivityBinding.instrumentIcon.setSelected(false);
                mainActivityBinding.aboutIcon.setSelected(false);
                break;
            case R.id.instrument_layout:
                mainActivityBinding.guidelineIcon.setSelected(false);
                mainActivityBinding.instrumentIcon.setSelected(true);
                mainActivityBinding.aboutIcon.setSelected(false);
                break;
            case R.id.about_layout:
                mainActivityBinding.guidelineIcon.setSelected(false);
                mainActivityBinding.instrumentIcon.setSelected(false);
                mainActivityBinding.aboutIcon.setSelected(true);
                break;

        }
    }

    private void setupTabCLick() {
        tabClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = v.getId() == R.id.guideline_layout ? guidelineFragment :
                                    v.getId() == R.id.instrument_layout ? instrumentFragment : aboutFragment;
                selectIcon(v.getId());
                if (!getSupportFragmentManager().getFragments()
                                                .get(0)
                                                .equals(fragment)) {
                    getSupportFragmentManager().beginTransaction()
                                               .replace(R.id.main_fragment, fragment)
                                               .commit();
                }
            }
        };
        mainActivityBinding.setModel(this);
    }
}
