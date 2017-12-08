package com.gt.gtapp.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gt.gtapp.R;
import com.gt.gtapp.util.statusbar.StatusBarFontHelper;
import com.gt.gtapp.utils.commonutil.BarUtils;
import com.gt.gtapp.utils.commonutil.ConvertUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by wzb on 2017/12/7 0007.
 */

public abstract class BaseActivity extends RxAppCompatActivity {
    private RelativeLayout mToolbar;
    private TextView toolBarTitle;
    private ImageView toolBarBack;
    private ImageView toolSetting;
    private ImageView toolmessage;

    public final int TOOLBAR_NOT = 0;
    public final int TOOLBAR_RED_STYLE = 1;
    public final int TOOLBAR_BACK = 2;

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
        toolSetting = (ImageView) findViewById(R.id.toolbar_setting);
        toolmessage = (ImageView) findViewById(R.id.toolbar_message);

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
            default:
                break;
        }

        //mToolbar.setPadding(0, BarUtils.getStatusBarHeight(this),0,0);

        toolBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void visibilityBack() {
        toolBarBack.setVisibility(View.VISIBLE);
    }

    @CallSuper
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View activityView = LayoutInflater.from(this).inflate(layoutResID, null, false);
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
        toolSetting.setVisibility(View.GONE);
        toolmessage.setVisibility(View.GONE);
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
        toolSetting.setVisibility(View.VISIBLE);
        toolmessage.setVisibility(View.VISIBLE);
        toolBarTitle.setVisibility(View.GONE);
        toolBarBack.setVisibility(View.GONE);
    }

    public void goneToolBar() {
        mToolbar.setVisibility(View.GONE);
    }

    public abstract int getToolBarType();
}
