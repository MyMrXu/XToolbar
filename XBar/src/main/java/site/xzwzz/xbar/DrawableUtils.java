package site.xzwzz.xbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import java.math.BigDecimal;

/*
 * @Project_Name :Securities
 * @package_Name :com.xzwzz.securities.util
 * @AUTHOR      :xzwzz@vip.qq.com
 * @DATE        :2018/5/16
 * 我不知道我在干神魔勾把
 * {@link BottomView}有这一整套的逻辑,但我还是在这里写了
 */
public class DrawableUtils {
    public static Drawable generateDrawableChecker(Context context, int Idpressed, int Idnormal) {
        //多种状态的多种图片集合,对应xml格式的selector
        Drawable pressed = Idpressed == -1 ? null : ContextCompat.getDrawable(context, Idpressed);
        Drawable normal = Idnormal == -1 ? null : ContextCompat.getDrawable(context, Idnormal);
        //添加多种状态下的图片
        return generateDrawableChecker(context, pressed, normal);
    }


    public static Drawable generateDrawableChecker(Context context, Drawable pressed, Drawable normal) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_checked}, pressed);
        drawable.addState(new int[]{-android.R.attr.state_checked}, normal);

        if (Build.VERSION.SDK_INT > 10) {
            drawable.setEnterFadeDuration(160);
            drawable.setExitFadeDuration(160);
        }
        return drawable;
    }

    public static Drawable generateDrawableSelector(Context context, Drawable selected, Drawable unselect) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_selected}, selected);
        drawable.addState(new int[]{-android.R.attr.state_selected}, unselect);

        if (Build.VERSION.SDK_INT > 10) {
            drawable.setEnterFadeDuration(160);
            drawable.setExitFadeDuration(160);
        }
        return drawable;
    }

    public static Drawable generateDrawableSelector(Context context, @DrawableRes int selected, @DrawableRes int unselect) {
        return generateDrawableSelector(context, getTintDrawable(context, selected, -1), getTintDrawable(context, selected, -1));
    }

    public static ColorStateList generateColorChecked(Context context, @ColorInt int normal, @ColorInt int check) {
        int[][] states = new int[][]{
                new int[]{-android.R.attr.state_checked}, // unchecked
                new int[]{android.R.attr.state_checked}  // checked
        };
        int[] colors = new int[]{
                normal,
                check
        };
        return new ColorStateList(states, colors);
    }

    public static ColorStateList generateColorSeletor(Context context, @ColorInt int normal, @ColorInt int seleted) {
        int[][] states = new int[][]{
                new int[]{-android.R.attr.state_selected}, // unchecked
                new int[]{android.R.attr.state_selected}  // checked
        };
        int[] colors = new int[]{
                normal,
                seleted
        };
        return new ColorStateList(states, colors);
    }

    public static Drawable getTintDrawable(Drawable drawable, @ColorInt int color) {
        Drawable.ConstantState state = drawable.getConstantState();
        Drawable drawable1 = DrawableCompat.wrap(state == null ? drawable : state.newDrawable()).mutate();
        drawable1.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        if (color != -1) {
            DrawableCompat.setTint(drawable1, color);
        }
        return drawable1;
    }

    public static Drawable getTintDrawable(Context context, @DrawableRes int d, @ColorInt int color) {
        Drawable drawable = ContextCompat.getDrawable(context, d);
        Drawable.ConstantState state = drawable.getConstantState();
        Drawable drawable1 = DrawableCompat.wrap(state == null ? drawable : state.newDrawable()).mutate();
        drawable1.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        if (color != -1) {
            DrawableCompat.setTint(drawable1, color);
        }
        return drawable1;
    }

    public static Drawable getTintDrawable(Drawable drawable) {
        return getTintDrawable(drawable, -1);
    }


    private int evaluateColor(int startValue, int endValue, float fraction) {
        if (fraction <= 0) {
            return startValue;
        }
        if (fraction >= 1) {
            return endValue;
        }
        int startInt = startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return ((startA + (int) (fraction * (endA - startA))) << 24) | ((startR + (int) (fraction * (endR - startR))) << 16) | ((startG + (int) (fraction * (endG - startG))) << 8) | ((startB + (int) (fraction * (endB - startB))));
    }

    public static float div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }
}
