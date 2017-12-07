package com.gt.gtapp.utils.commonutil;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

/**
 * Description:
 * Created by jack-lin on 2017/7/18 0018.
 */

public class DrawableUtils {
    public static StateListDrawable addColorStateDrawable(int colorNormal, int colorPressed, int colorFocused) {
        StateListDrawable sd = new StateListDrawable();
        Drawable normal = colorNormal == -1 ? null : new ColorDrawable(colorNormal);
        Drawable pressed = colorPressed == -1 ? null : new ColorDrawable(colorPressed);
        Drawable focus = colorFocused == -1 ? null : new ColorDrawable(colorFocused);
        //注意该处的顺序，只要有一个状态与之相配，背景就会被换掉
        //所以不要把大范围放在前面了，如果sd.addState(new[]{},normal)放在第一个的话，就没有什么效果了
        sd.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_focused}, focus);
        sd.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
        sd.addState(new int[]{android.R.attr.state_focused}, focus);
        sd.addState(new int[]{android.R.attr.state_pressed}, pressed);
        sd.addState(new int[]{android.R.attr.state_enabled}, normal);
        sd.addState(new int[]{}, normal);
        return sd;
    }
    public static StateListDrawable addImageStateDrawable(Integer imageNormal, int imagePressed, int imageFocused) {
        StateListDrawable sd = new StateListDrawable();
        Drawable normal = imageNormal == -1 ? null : Utils.getContext().getResources().getDrawable(imageNormal);
        Drawable pressed = imagePressed == -1 ? null : Utils.getContext().getResources().getDrawable(imagePressed);
        Drawable focus = imageFocused == -1 ? null : Utils.getContext().getResources().getDrawable(imageFocused);
        //注意该处的顺序，只要有一个状态与之相配，背景就会被换掉
        //所以不要把大范围放在前面了，如果sd.addState(new[]{},normal)放在第一个的话，就没有什么效果了
        sd.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_focused}, focus);
        sd.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
        sd.addState(new int[]{android.R.attr.state_focused}, focus);
        sd.addState(new int[]{android.R.attr.state_pressed}, pressed);
        sd.addState(new int[]{android.R.attr.state_enabled}, normal);
        sd.addState(new int[]{}, normal);
        return sd;
    }
}
