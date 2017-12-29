package com.gt.gtapp.base;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gt.gtapp.R;
import com.gt.gtapp.utils.commonutil.ToastUtil;
import com.gt.gtapp.utils.statusbar.StatusBarFontHelper;
import com.gt.gtapp.utils.commonutil.BarUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by wzb on 2017/12/7 0007.
 */

public abstract class BaseActivity extends RxAppCompatActivity {
    private RelativeLayout mToolbar;
    private TextView toolBarTitle;
    private ImageView toolBarBack;
    private RelativeLayout messageLayout;
    private RelativeLayout settingLayout;
    private View activityView;
    private BtnClickListener mBtnClickListener=new BtnClickListener();
    /**
     * 隐藏标题栏
     */
    public final int TOOLBAR_NOT = 0;
    /**
     * 红色无标题
     */
    public final int TOOLBAR_RED_STYLE = 1;
    /**
     * 白底返回键
     */
    public final int TOOLBAR_BACK = 2;
    /**
     * 红色白标题
     */
    public final int TOOLBAR_RED_WHITE_TITLE_STYLE = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.toolbar);
        init();
    }

    private void init() {
        mToolbar = (RelativeLayout) findViewById(R.id.base_toolbar);
        //减去状态栏高度
        toolBarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolBarBack = (ImageView) findViewById(R.id.toolbar_back);
        messageLayout = (RelativeLayout) findViewById(R.id.messageLayout);
        settingLayout = (RelativeLayout) findViewById(R.id.settingLayout);

        switch (getToolBarType()) {
            case TOOLBAR_NOT:
                mToolbar.setVisibility(View.GONE);
                break;
            case TOOLBAR_RED_STYLE:
                setRedStyle();
                break;
            case TOOLBAR_BACK:
                setBackStyle(this.getTitle().toString());
                break;
            case TOOLBAR_RED_WHITE_TITLE_STYLE:
                setRedWhiteStyle(this.getTitle().toString());
                break;
            default:
                break;
        }

        //mToolbar.setPadding(0, BarUtils.getStatusBarHeight(this),0,0);

        toolBarBack.setOnClickListener(mBtnClickListener);
        messageLayout.setOnClickListener(mBtnClickListener);
        settingLayout.setOnClickListener(mBtnClickListener);
    }

    public void visibilityBack() {
        toolBarBack.setVisibility(View.VISIBLE);
    }

    @CallSuper
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        activityView = LayoutInflater.from(this).inflate(layoutResID, null, false);
        ViewGroup viewGroup = (ViewGroup) mToolbar.getParent();
        viewGroup.addView(activityView);
        //空出边距给toolbar
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) activityView.getLayoutParams();
        lp.setMargins(0, mToolbar.getLayoutParams().height, 0, 0);
        ButterKnife.bind(this);
    }

    @CallSuper
    @Override
    public void setContentView(View view) {
        ViewGroup viewGroup = (ViewGroup) mToolbar.getParent();
        viewGroup.addView(view);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) view.getLayoutParams();
        lp.setMargins(0, mToolbar.getLayoutParams().height, 0, 0);
        ButterKnife.bind(this);
    }

    public void setToolBarTitle(String title) {
        toolBarTitle.setVisibility(View.GONE);
        toolBarTitle.setText(title);
    }

    /**
     * 左边返回键  黑色标题 白底  因为不设置padding 用非沉浸式 所以有下面计算
     */
    private void setBackStyle(String title) {
        //没有改变状态栏 4.4以下系统
        if (StatusBarFontHelper.setStatusBarMode(this, true) == 0) {
            FrameLayout.LayoutParams fl = (FrameLayout.LayoutParams) mToolbar.getLayoutParams();
            fl.height = fl.height - BarUtils.getStatusBarHeight(this);
            mToolbar.setLayoutParams(fl);
        } else {
            mToolbar.setPadding(0, BarUtils.getStatusBarHeight(this), 0, 0);
        }
        mToolbar.setBackgroundColor(this.getResources().getColor(R.color.white));
      //  toolSetting.setVisibility(View.GONE);
      //  toolMessage.setVisibility(View.GONE);
        toolBarTitle.setVisibility(View.VISIBLE);
        toolBarTitle.setText(title);
        toolBarBack.setVisibility(View.VISIBLE);
    }

    /**
     * 渐变红底 设置 信息按钮
     */
    private void setRedStyle() {

        //没有改变状态栏 4.4以下系统
        if (StatusBarFontHelper.setStatusBarMode(this, false) == 0) {
            FrameLayout.LayoutParams fl = (FrameLayout.LayoutParams) mToolbar.getLayoutParams();
            fl.height = fl.height - BarUtils.getStatusBarHeight(this);
            mToolbar.setLayoutParams(fl);
        } else {
            mToolbar.setPadding(0, BarUtils.getStatusBarHeight(this), 0, 0);
            //设置透明才能看得到底下布局
            StatusBarFontHelper.setStatusBarTransparent(this);
        }

        mToolbar.setBackground(this.getResources().getDrawable(R.drawable.shape_toolbar_shade));
        toolBarTitle.setVisibility(View.GONE);
        toolBarBack.setVisibility(View.GONE);
    }

    private void setRedWhiteStyle(String title){
        setRedStyle();
        toolBarTitle.setTextColor(getResources().getColor(R.color.white));
        toolBarTitle.setText(title);
        toolBarTitle.setVisibility(View.VISIBLE);
    }

    public void changeStyle(int stypeType,String title){
        changeStyle(stypeType);
        toolBarTitle.setText(title);
    }

    public void changeStyle(int stypeType){
        switch (stypeType) {
            case TOOLBAR_NOT:
                mToolbar.setVisibility(View.GONE);
                break;
            case TOOLBAR_RED_STYLE:
                setRedStyle();
                break;
            case TOOLBAR_BACK:
                setBackStyle(this.getTitle().toString());
                break;
            case TOOLBAR_RED_WHITE_TITLE_STYLE:
                setRedWhiteStyle(this.getTitle().toString());
                break;
            default:
                break;
        }
    }

    public void goneToolBar() {
        Log.d("goneToolBar","goneToolBar");
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) activityView.getLayoutParams();
        lp.setMargins(0, BarUtils.getStatusBarHeight(this), 0, 0);
        activityView.setLayoutParams(lp);
        mToolbar.setVisibility(View.GONE);
        BarUtils.setStatusBarColor(this,getResources().getColor(R.color.launch_gray));
    }
    public void showToolBar() {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) activityView.getLayoutParams();
        lp.setMargins(0, mToolbar.getLayoutParams().height, 0, 0);
        activityView.setLayoutParams(lp);
        mToolbar.setVisibility(View.VISIBLE);
    }
    /**
     * 隐藏标题栏 0
     * 红色无标题 1
     * 白底返回键 2
     * 红色白标题 3
     */
    public abstract int getToolBarType();

    private class BtnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){

                case R.id.toolbar_back:
                    onBackPressed();
                    break;
                case R.id.messageLayout:
                    ToastUtil.getInstance().showToast("更多功能敬请期待");
                    break;
                case R.id.settingLayout:
                    ToastUtil.getInstance().showToast("更多功能敬请期待");
                    break;
                default:
                    break;

            }
        }
    }
}
