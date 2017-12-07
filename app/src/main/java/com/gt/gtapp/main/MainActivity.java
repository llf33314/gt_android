package com.gt.gtapp.main;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gt.gtapp.R;
import com.gt.gtapp.util.statusbar.StatusBarFontHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_bottom_duofriend)
    LinearLayout mainBottomDuofriend;
    @BindView(R.id.main_bottom_person)
    LinearLayout mainBottomPerson;
    @BindView(R.id.main_bottom_duofriend_iv)
    ImageView mainBottomDuofriendIv;
    @BindView(R.id.main_bottom_duofriend_tv)
    TextView mainBottomDuofriendTv;
    @BindView(R.id.main_bottom_person_iv)
    ImageView mainBottomPersonIv;
    @BindView(R.id.main_bottom_person_tv)
    TextView mainBottomPersonTv;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private DuofriendFragment duofriendFragment;
    private PersonFragment personFragment;
    /**
     * 方便操作
     */
    private List<Fragment> fragments;
    /**
     * 不用fragment api 异步切换 不靠谱
     */
    private int currentFragment = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        StatusBarFontHelper.setStatusBarTransparent(this);

        init();
    }

    private void init() {
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        duofriendFragment = new DuofriendFragment();
        personFragment = new PersonFragment();

        fragments = new ArrayList<>();

        mFragmentTransaction.add(R.id.main_content, duofriendFragment);
        fragments.add(duofriendFragment);
        mFragmentTransaction.add(R.id.main_content, personFragment);
        fragments.add(personFragment);

        mFragmentTransaction.hide(personFragment);
        mFragmentTransaction.commit();

    }


    @OnClick({R.id.main_bottom_duofriend, R.id.main_bottom_person})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.main_bottom_duofriend:
                if (currentFragment != 0) {
                    //底部UI更新 后续更加动画
                    mainBottomDuofriendIv.setImageResource(R.drawable.main_duofriend_icon_p);
                    mainBottomDuofriendTv.setVisibility(View.GONE);

                    mainBottomPersonIv.setImageResource(R.drawable.main_person);
                    mainBottomPersonTv.setVisibility(View.VISIBLE);

                    mFragmentTransaction = mFragmentManager.beginTransaction();
                    mFragmentTransaction.show(duofriendFragment);
                    mFragmentTransaction.hide(fragments.get(currentFragment));

                    mFragmentTransaction.commit();
                    currentFragment = 0;
                }

                break;
            case R.id.main_bottom_person:
                if (currentFragment != 1) {
                    mainBottomPersonIv.setImageResource(R.drawable.main_duofriend_icon_p);
                    mainBottomPersonTv.setVisibility(View.GONE);

                    mainBottomDuofriendIv.setImageResource(R.drawable.main_duofriend_icon);
                    mainBottomDuofriendTv.setVisibility(View.VISIBLE);

                    mFragmentTransaction = mFragmentManager.beginTransaction();
                    mFragmentTransaction.show(personFragment);
                    mFragmentTransaction.hide(fragments.get(currentFragment));

                    mFragmentTransaction.commit();
                    currentFragment = 1;
                }
                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (duofriendFragment.onBackKeyDown()){
               return true;
            }else{
                return super.onKeyDown(keyCode, event);
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}
