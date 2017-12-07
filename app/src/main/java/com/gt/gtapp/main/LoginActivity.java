package com.gt.gtapp.main;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gt.gtapp.R;
import com.gt.gtapp.util.statusbar.StatusBarFontHelper;
import com.gt.gtapp.utils.commonutil.ConvertUtils;
import com.gt.gtapp.widget.ImageCheckBox;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by wzb on 2017/12/6 0006.
 */

public class LoginActivity extends RxAppCompatActivity {

    @BindView(R.id.login_icons)
    LinearLayout loginIcons;
    @BindView(R.id.login_account)
    EditText loginAccount;
    @BindView(R.id.login_psd)
    EditText loginPsd;
    @BindView(R.id.login_psd_switch)
    ImageCheckBox loginPsdSwitch;
    @BindView(R.id.login_login)
    TextView loginLogin;
    @BindView(R.id.login_login_ll)
    LinearLayout loginLoginLl;
    @BindView(R.id.login_bottom_text)
    TextView loginBottomText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        StatusBarFontHelper.setStatusBarMode(this, true);
        init();

    }
    private void init(){
        loginAccount.addTextChangedListener(new LoginEditTextListener());
        loginPsd.addTextChangedListener(new LoginEditTextListener());
        loginPsdSwitch.setOnCheckedChangeListener(new ImageCheckBox.OnCheckedChangeListener(){

            @Override
            public void chang(ImageCheckBox imageCheckBox, boolean isChecked) {
                if (isChecked){
                    loginPsd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else{
                    loginPsd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                loginPsd.setSelection(loginPsd.getText().length());
            }
        });

        //1秒后执行动画
        Observable.timer(1000, TimeUnit.MILLISECONDS)
                .compose(this.<Long>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        ObjectAnimator animatorIconsUp = ObjectAnimator.ofFloat(loginIcons, "translationY",
                                loginIcons.getTranslationY(),-ConvertUtils.dp2px(LoginActivity.this.getResources().getDimension(R.dimen.dp_50)));
                        animatorIconsUp.setDuration(500);

                        ObjectAnimator animatorLoginUp = ObjectAnimator.ofFloat(loginLoginLl, "translationY",
                                loginLoginLl.getTranslationY(),-ConvertUtils.dp2px(LoginActivity.this.getResources().getDimension(R.dimen.dp_30)));
                        ObjectAnimator animatorLoginAlpha = ObjectAnimator.ofFloat(loginLoginLl, "alpha", 0,1);

                        AnimatorSet loginAnimatorSet=new AnimatorSet();
                        loginAnimatorSet.play(animatorLoginUp).with(animatorLoginAlpha);
                        loginAnimatorSet.setDuration(500);

                        ObjectAnimator animatorBottomTextDown = ObjectAnimator.ofFloat(loginBottomText, "translationY",
                                loginBottomText.getTranslationY(),ConvertUtils.dp2px(LoginActivity.this.getResources().getDimension(R.dimen.dp_50)));
                        animatorBottomTextDown.setDuration(500);

                        animatorIconsUp.start();
                        loginAnimatorSet.start();
                        animatorBottomTextDown.start();

                    }
                });

    }
    private class LoginEditTextListener implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            loginLogin.setEnabled(!TextUtils.isEmpty(loginAccount.getText())&&!TextUtils.isEmpty(loginPsd.getText()));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    @OnClick({R.id.login_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.login_login:
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
