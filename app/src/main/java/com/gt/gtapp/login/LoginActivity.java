package com.gt.gtapp.login;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gt.gtapp.R;
import com.gt.gtapp.bean.LoginAccountBean;
import com.gt.gtapp.bean.LoginBean;
import com.gt.gtapp.bean.SignBean;
import com.gt.gtapp.bean.StaffListIndustryBean;
import com.gt.gtapp.http.HttpResponseException;
import com.gt.gtapp.http.retrofit.BaseResponse;
import com.gt.gtapp.http.retrofit.HttpCall;
import com.gt.gtapp.http.rxjava.observable.DialogTransformer;
import com.gt.gtapp.http.rxjava.observable.ResultTransformer;
import com.gt.gtapp.http.rxjava.observer.BaseObserver;
import com.gt.gtapp.main.MainActivity;
import com.gt.gtapp.util.statusbar.StatusBarFontHelper;
import com.gt.gtapp.utils.commonutil.ConvertUtils;
import com.gt.gtapp.widget.ImageCheckBox;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

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

    Gson gson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        StatusBarFontHelper.setStatusBarMode(this,true);
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

        loginPsd.setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    loginLogin.performClick();
                    return true;
                }
                return false;
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

                final String account=loginAccount.getText().toString().trim();
                final String psd=loginPsd.getText().toString().trim();

                HttpCall.getApiService()
                        .getSign(account,psd)
                        .flatMap(ResultTransformer.<SignBean>flatMap())
                        .flatMap(new Function<SignBean, ObservableSource<String>>() {
                            @Override
                            public ObservableSource<String> apply(@NonNull SignBean signBean) throws Exception {

                                return HttpCall.getApiService().erpLogin(account, psd, gson.toJson(signBean));
                            }
                        })
                        .flatMap(new Function<String, ObservableSource<BaseResponse<LoginAccountBean>>>() {
                            @Override
                            public ObservableSource<BaseResponse<LoginAccountBean>> apply(@NonNull String s) throws Exception {
                                String jsonResult;
                                LoginBean loginBean = null;
                                try {
                                    jsonResult = s.substring(s.indexOf("(") + 1, s.indexOf(")"));
                                    loginBean = gson.fromJson(jsonResult, LoginBean.class);
                                } catch (Exception e) { //后台数据有误json转化出错
                                    throw  new HttpResponseException(500,"后台数据有误");
                                }

                                if ("0".equals(loginBean.getCode())) {//登录成功
                                    return HttpCall.getApiService().getLoginAccount();
                                }else{
                                    throw new HttpResponseException(1,"erp接口登录失败");
                                }
                            }
                        })
                        .flatMap(ResultTransformer.<LoginAccountBean>flatMap())
                        .flatMap(new Function<LoginAccountBean, ObservableSource<BaseResponse<List<StaffListIndustryBean>>>>() {
                            @Override
                            public ObservableSource<BaseResponse<List<StaffListIndustryBean>>> apply(@NonNull LoginAccountBean loginAccountBean) throws Exception {
                                if (loginAccountBean.getAccountType()==1){//老板账号
                                    String duoFriendUrl=loginAccountBean.getHomeUrl();
                                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                    intent.putExtra("url",duoFriendUrl);
                                    startActivity(intent);
                                    finish();
                                    throw  new HttpResponseException(HttpResponseException.SUCCESS_BREAK,"");
                                }else{//员工账号  老板账号直接跳转到网页  员工账号再请求接口或者erp
                                    return HttpCall.getApiService().staffListIndustry();
                                }
                            }
                        })
                        .compose(ResultTransformer.<List<StaffListIndustryBean>>transformer())
                        .compose(LoginActivity.this.<List<StaffListIndustryBean>>bindToLifecycle())
                        .compose(new DialogTransformer().<List<StaffListIndustryBean>>transformer())
                        .subscribe(new BaseObserver<List<StaffListIndustryBean>>() {
                            @Override
                            protected void onSuccess(List<StaffListIndustryBean> staffListIndustryBeanList) {
                                //获取到员工账号的列表
                                Intent intent=new Intent(LoginActivity.this,StaffListIndustryActivity.class);
                                intent.putParcelableArrayListExtra("staffListIndustryList", (ArrayList<? extends Parcelable>) staffListIndustryBeanList);
                                startActivity(intent);
                            }
                        });
                break;
        }
    }
}
