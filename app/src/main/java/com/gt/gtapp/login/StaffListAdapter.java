package com.gt.gtapp.login;

import android.content.Context;

import com.gt.gtapp.R;
import com.gt.gtapp.base.recyclerview.BaseRecyclerAdapter;
import com.gt.gtapp.base.recyclerview.BaseViewHolder;
import com.gt.gtapp.bean.StaffListIndustryBean;

import java.util.List;

/**
 * Created by wzb on 2017/12/8 0008.
 */

public class StaffListAdapter extends BaseRecyclerAdapter<StaffListIndustryBean> {

    public StaffListAdapter(Context context, List<StaffListIndustryBean> listBean) {
        super(context, listBean);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_staff_list;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, StaffListIndustryBean bean, int position) {
            /*  EAT("1", "康小厨"),
                CAR("2", "车小算"),
                LOOK("3", "样子"),
                PROPERTY("5", "揽胜家园"),
                MEDICAL("6", "小红十"),
                SHOP("7", "翼店"),
                STOCK("8", "敏库"),
                HOTEL("9", "客来驿"),
                EDUCATION("11", "小盼"),

                ERROR1("4", ""),
                ERROR2("10", ""),*/

        switch (Integer.valueOf(bean.getCode())){
            case 1:
                holder.setText(R.id.staff_list_item_name,IndustryEnums.EAT.getDesc())
                        .setImageResource(R.id.staff_list_item_iv,R.drawable.erp_kangxiaochu_icon);
                break;
            case 2:
                holder.setText(R.id.staff_list_item_name,IndustryEnums.CAR.getDesc())
                        .setImageResource(R.id.staff_list_item_iv,R.drawable.erp_chexiaosuan_icon);
                break;
            case 3:
                holder.setText(R.id.staff_list_item_name,IndustryEnums.LOOK.getDesc())
                        .setImageResource(R.id.staff_list_item_iv,R.drawable.erp_yangzi_icon);
                break;
            case 5:
                holder.setText(R.id.staff_list_item_name,IndustryEnums.PROPERTY.getDesc())
                        .setImageResource(R.id.staff_list_item_iv,R.drawable.erp_lanshengjiayuan_icon);
                break;
            case 6:
                holder.setText(R.id.staff_list_item_name,IndustryEnums.MEDICAL.getDesc())
                        .setImageResource(R.id.staff_list_item_iv,R.drawable.erp_xiaohongshi_icon);
                break;
            case 7:
                holder.setText(R.id.staff_list_item_name,IndustryEnums.SHOP.getDesc())
                        .setImageResource(R.id.staff_list_item_iv,R.drawable.erp_shangcheng_icon);
                break;
            case 8:
                holder.setText(R.id.staff_list_item_name,IndustryEnums.STOCK.getDesc())
                        .setImageResource(R.id.staff_list_item_iv,R.drawable.erp_minku_icon);
                break;
            case 9:
                holder.setText(R.id.staff_list_item_name,IndustryEnums.HOTEL.getDesc())
                        .setImageResource(R.id.staff_list_item_iv,R.drawable.erp_kelaiyi_icon);
                break;
            case 11:
                holder.setText(R.id.staff_list_item_name,IndustryEnums.EDUCATION.getDesc())
                        .setImageResource(R.id.staff_list_item_iv,R.drawable.erp_xiaopang_icon);
                break;
            default:
                break;
        }
    }
}
