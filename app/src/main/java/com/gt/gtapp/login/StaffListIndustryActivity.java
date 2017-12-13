package com.gt.gtapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gt.gtapp.R;
import com.gt.gtapp.base.BaseActivity;
import com.gt.gtapp.base.recyclerview.BaseRecyclerAdapter;
import com.gt.gtapp.base.recyclerview.SpaceItemDecoration;
import com.gt.gtapp.bean.LoginFinishMsg;
import com.gt.gtapp.bean.StaffListIndustryBean;
import com.gt.gtapp.http.rxjava.RxBus;
import com.gt.gtapp.main.MainActivity;
import com.gt.gtapp.utils.commonutil.ConvertUtils;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by wzb on 2017/12/8 0008.
 */

public class StaffListIndustryActivity extends BaseActivity {
    @BindView(R.id.staff_list_rv)
    RecyclerView staffListRv;
    private List<StaffListIndustryBean> staffList;
    public static final String STAFF_CHOOSE_URL="staffChooseErpUrl";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statff_industry_lis);
        staffList = wipeStatus(getIntent().<StaffListIndustryBean>getParcelableArrayListExtra("staffListIndustryList"));
        init();
    }

    /**
     * 去除status 状态等于0 的erp
     */
    private List<StaffListIndustryBean> wipeStatus( List<StaffListIndustryBean> list){
        List<StaffListIndustryBean> newList=new ArrayList<>();
        if (list!=null&&list.size()>0){
            for (StaffListIndustryBean bean:list){
                if (bean.getStatus()!=0){
                    newList.add(bean);
                }
            }
        }
       return  newList;
    }

    private void init(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(StaffListIndustryActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        staffListRv.setLayoutManager(layoutManager);
        
        StaffListAdapter adapter=new StaffListAdapter(StaffListIndustryActivity.this,staffList);
        staffListRv.setAdapter(adapter);
        staffListRv.addItemDecoration(new SpaceItemDecoration(ConvertUtils.dp2px(10),SpaceItemDecoration.SPACE_BOTTOM));
        adapter.setmOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, Object item, int position) {
                    RxBus.get().post(new LoginFinishMsg());
                    StaffListIndustryBean staffListIndustryBean= (StaffListIndustryBean) item;
                    Hawk.put(STAFF_CHOOSE_URL,staffListIndustryBean.getUrl());

                    Intent intent =new Intent(StaffListIndustryActivity.this, MainActivity.class);
                    intent.putExtra("url",((StaffListIndustryBean)item).getUrl());
                    startActivity(intent);
                    finish();
            }
        });
    }

    @Override
    public int getToolBarType() {
        return TOOLBAR_BACK;
    }
}
