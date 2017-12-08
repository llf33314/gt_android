package com.gt.gtapp.base.recyclerview;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by wzb on 2017/8/4 0004.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {
    private  final SparseArray<View> mViews;
    private Context mContext;
    private View mItemView;
    public BaseViewHolder(View itemView,Context context ) {
        super(itemView);
        mViews=new SparseArray<>();
        mItemView=itemView;
        this.mContext=context;
    }
    public View findView(int id){
        View v=mViews.get(id);
        if (v==null){
            v=mItemView.findViewById(id);
            mViews.put(id,v);
        }
        return v;
    }
    public BaseViewHolder setText(int viewId, CharSequence value) {
        TextView view = (TextView) findView(viewId);
        view.setText(value);
        return this;
    }

    public BaseViewHolder setImageURI(int viewId, Uri uri) {
        ImageView view = (ImageView) findView(viewId);
        view.setImageURI(uri);
        return this;
    }

    public BaseViewHolder setTextColor(int viewId, int textColor) {
        TextView view = (TextView) findView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    public BaseViewHolder setTextColorRes(int viewId, int textColorRes) {
        TextView view = (TextView) findView(viewId);
        view.setTextColor(mContext.getResources().getColor(textColorRes));
        return this;
    }

    public BaseViewHolder setImageResource(int viewId, int imageResId) {
        ImageView view = (ImageView) findView(viewId);
        view.setImageResource(imageResId);
        return this;
    }

    public BaseViewHolder setImageForUrl(int viewId, String url) {
        ImageView view = (ImageView) findView(viewId);
       // Glide.with(mContext).load(url).into(view);
        return this;
    }

    public BaseViewHolder setBackgroundColor(int viewId, int color) {
        View view = findView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public BaseViewHolder setBackgroundResource(int viewId, int backgroundRes) {
        View view = findView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    public BaseViewHolder setVisible(int viewId, boolean visible) {
        View view = findView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }
    public BaseViewHolder setTag(int viewId, Object tag) {
        View view = findView(viewId);
        view.setTag(tag);
        return this;
    }
}
