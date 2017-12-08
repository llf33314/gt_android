package com.gt.gtapp.base.recyclerview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by wzb on 2017/8/24 0024.
 *
 * 间隙
 */

public  class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    public static final int SPACE_LEFT=0;
    public static final int SPACE_TOP=1;
    public static final int SPACE_RIGHT=2;
    public static final int SPACE_BOTTOM=3;

    int mSpace;
    /**
     * 0:left 1:top 2:right 3:t
     */
    int mSpaceType;
    public SpaceItemDecoration(int space) {
        this.mSpace = space;
    }
    public SpaceItemDecoration(int space, int spaceType){
        this.mSpaceType=spaceType;
        this.mSpace=space;

    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildAdapterPosition(view) !=state.getItemCount()-1) {
            switch (mSpaceType){
                case SPACE_LEFT:
                    outRect.left = mSpace;
                    break;
                case SPACE_TOP:
                    outRect.top = mSpace;
                    break;
                case SPACE_RIGHT:
                    outRect.right = mSpace;
                    break;
                case SPACE_BOTTOM:
                    outRect.bottom = mSpace;
                    break;
                default:
                    outRect.bottom = mSpace;
                    break;
            }

        }
    }
    public int getSpace(){
        return this.mSpace;
    }
}

