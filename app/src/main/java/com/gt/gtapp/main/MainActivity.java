package com.gt.gtapp.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gt.gtapp.R;
import com.gt.gtapp.base.BaseActivity;
import com.gt.gtapp.base.BaseConstant;
import com.gt.gtapp.base.MyApplication;
import com.gt.gtapp.bean.LoginFinishMsg;
import com.gt.gtapp.http.rxjava.RxBus;
import com.gt.gtapp.update.UpdateManager;
import com.gt.gtapp.utils.commonutil.BarUtils;
import com.gt.gtapp.utils.commonutil.ToastUtil;
import com.gt.gtapp.web.GtBridge;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

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
    @BindView(R.id.bottomLayout)
    LinearLayout bottomLayout;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private DuofriendFragment duofriendFragment;
    private PersonFragment personFragment;
    GtBridge gtBridge;
    /**
     * 方便操作
     */
    private List<Fragment> fragments;
    /**
     * 不用fragment api 异步切换 不靠谱
     */
    private int currentFragment = 0;

    private String url;

    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
        if (MyApplication.isNeedUpdate()) {
            UpdateManager updateManager = new UpdateManager(this,  BaseConstant.UPDATE_NAME, UpdateManager.UPDATE_BADGE_AND_DIALOG);
            updateManager.requestUpdate();
        }
    }

    private void init() {
        gtBridge = new GtBridge();
        url = getIntent().getStringExtra("url");
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        duofriendFragment = new DuofriendFragment(url);
        personFragment = new PersonFragment();


        fragments = new ArrayList<>();

        mFragmentTransaction.add(R.id.main_content, duofriendFragment);
        fragments.add(duofriendFragment);
        mFragmentTransaction.add(R.id.main_content, personFragment);
        fragments.add(personFragment);

        mFragmentTransaction.hide(personFragment);
        mFragmentTransaction.commit();

        RxBus.get().post(new LoginFinishMsg());
    }

    @Override
    public int getToolBarType() {
        return TOOLBAR_RED_STYLE;
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
                    mainBottomPersonTv.setTextColor(getResources().getColor(R.color.launch_gray));

                    mFragmentTransaction = mFragmentManager.beginTransaction();
                    mFragmentTransaction.show(duofriendFragment);
                    mFragmentTransaction.hide(fragments.get(currentFragment));

                    mFragmentTransaction.commit();
                    currentFragment = 0;
                    changeStyle(TOOLBAR_RED_STYLE);
                    boolean isLeftHeaderShow = Hawk.get(BaseConstant.HAWK_LEFT_IS_SHOW_HEADER, true);

                }

                break;
            case R.id.main_bottom_person:
                if (currentFragment != 1) {
                    mainBottomPersonIv.setImageResource(R.drawable.main_person_p);
                    mainBottomPersonTv.setTextColor(getResources().getColor(R.color.login_enable));

                    mainBottomDuofriendIv.setImageResource(R.drawable.main_duofriend_icon);
                    mainBottomDuofriendTv.setVisibility(View.VISIBLE);

                    mFragmentTransaction = mFragmentManager.beginTransaction();
                    mFragmentTransaction.show(personFragment);
                    mFragmentTransaction.hide(fragments.get(currentFragment));

                    mFragmentTransaction.commit();
                    currentFragment = 1;
                    changeStyle(TOOLBAR_RED_WHITE_TITLE_STYLE, "个人中心");
                }
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (duofriendFragment.onBackKeyDown()) {
                return true;
            } else {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    ToastUtil.getInstance().showToast("再按一次退出");
                    exitTime = System.currentTimeMillis();
                } else {
                    MyApplication.appExit();
                }
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    public void showBottom(boolean isShow) {
        bottomLayout.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public DuofriendFragment getDuoFriendFragment() {
        return duofriendFragment;
    }
}
