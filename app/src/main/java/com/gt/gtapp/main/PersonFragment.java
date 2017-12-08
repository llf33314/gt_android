package com.gt.gtapp.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gt.gtapp.R;
import com.gt.gtapp.base.MyApplication;
import com.gt.gtapp.bean.BossAccountBean;
import com.gt.gtapp.bean.StaffAccountBean;
import com.gt.gtapp.http.retrofit.HttpCall;
import com.gt.gtapp.http.rxjava.observable.DialogTransformer;
import com.gt.gtapp.http.rxjava.observable.ResultTransformer;
import com.gt.gtapp.http.rxjava.observer.BaseObserver;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by wzb on 2017/12/6 0006.
 */

public class PersonFragment extends Fragment {
    @BindView(R.id.person_name)
    TextView personName;
    @BindView(R.id.person_level)
    TextView personLevel;
    @BindView(R.id.staff_list_item_name)
    TextView staffListItemName;
    @BindView(R.id.person_person_center_ll)
    LinearLayout personPersonCenterLl;
    @BindView(R.id.person_about_duofriend_ll)
    LinearLayout personAboutDuofriendLl;
    @BindView(R.id.person_out_login)
    TextView personOutLogin;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_person, container, false);
        unbinder = ButterKnife.bind(this, v);
        init();
        return v;
    }

    private void init(){
        switch (MyApplication.getAccountType()){
            case 1://老板登录的账号
                HttpCall.getApiService().bossAccountInfo()
                        .compose(ResultTransformer.<BossAccountBean>transformer())
                        .compose(new DialogTransformer().<BossAccountBean>transformer())
                        .subscribe(new BaseObserver<BossAccountBean>() {
                            @Override
                            protected void onSuccess(BossAccountBean bossAccountBean) {
                                personName.setText(bossAccountBean.getName());
                                personLevel.setText(bossAccountBean.getVersion());
                            }
                        });
                break;
            case 0://员工登录的账号
                HttpCall.getApiService().staffAccountInfo()
                        .compose(ResultTransformer.<StaffAccountBean>transformer())
                        .compose(new DialogTransformer().<StaffAccountBean>transformer())
                        .subscribe(new BaseObserver<StaffAccountBean>() {
                            @Override
                            protected void onSuccess(StaffAccountBean staffAccountBean) {
                                personName.setText(staffAccountBean.getName());
                                personLevel.setText(staffAccountBean.getPosName());
                            }
                        });
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.person_name, R.id.person_level, R.id.staff_list_item_name, R.id.person_person_center_ll, R.id.person_about_duofriend_ll, R.id.person_out_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.person_name:
                break;
            case R.id.person_level:
                break;
            case R.id.staff_list_item_name:
                break;
            case R.id.person_person_center_ll:
                break;
            case R.id.person_about_duofriend_ll:
                break;
            case R.id.person_out_login:
                break;
        }
    }
}
