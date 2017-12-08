package com.gt.gtapp.base.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by wzb on 2017/8/4 0004.
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    public List<T> listBean;
    public Context mContext;

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public BaseRecyclerAdapter(Context context,List<T> listBean){
        this.mContext=context;
        this.listBean=listBean;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view=LayoutInflater.from(mContext).inflate(getLayoutId(),parent,false);
        final BaseViewHolder baseViewHolder=new BaseViewHolder(view,mContext);
        changeLayout(parent,view);

        bindListener(baseViewHolder);

        return baseViewHolder;
    }

    /**
     * 动态布局
     */
    protected void changeLayout(ViewGroup parent,View view){

    }

    protected void bindListener(final BaseViewHolder baseViewHolder){
        if (mOnItemClickListener!=null){
            baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = baseViewHolder.getAdapterPosition() ;
                    mOnItemClickListener.onClick(v, listBean.get(position), position);
                }
            });
        }

        if (mOnItemLongClickListener!=null){
            baseViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener(){

                @Override
                public boolean onLongClick(View v) {
                    final int position = baseViewHolder.getAdapterPosition() ;
                    mOnItemLongClickListener.onLongClick(v, listBean.get(position), position);
                    return true;
                }
            });
        }
    }
    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        onBindViewHolder(holder,listBean.get(position), position);
    }

    @Override
    public int getItemCount() {
        if (listBean==null){
            return 0;
        }
        return listBean.size();
    }

    public  abstract int getLayoutId();
    public abstract void onBindViewHolder(BaseViewHolder holder,T bean, int position);


    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener =mOnItemClickListener;
    }

    public void setmOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    public interface OnItemClickListener<T> {
        void onClick(View view, T item, int position);
    }

    public interface OnItemLongClickListener<T> {
        void onLongClick(View view, T item, int position);
    }

}
