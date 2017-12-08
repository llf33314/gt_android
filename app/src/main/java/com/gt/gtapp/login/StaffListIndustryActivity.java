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
import com.gt.gtapp.bean.StaffListIndustryBean;
import com.gt.gtapp.main.MainActivity;
import com.gt.gtapp.util.statusbar.StatusBarFontHelper;
import com.gt.gtapp.utils.commonutil.ConvertUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wzb on 2017/12/8 0008.
 */

public class StaffListIndustryActivity extends BaseActivity {
    @BindView(R.id.staff_list_rv)
    RecyclerView staffListRv;
    private List<StaffListIndustryBean> staffList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statff_industry_lis);
        staffList = getIntent().getParcelableArrayListExtra("staffListIndustryList");
        init();

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
