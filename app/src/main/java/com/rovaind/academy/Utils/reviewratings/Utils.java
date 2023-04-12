

package com.rovaind.academy.Utils.reviewratings;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;


class Utils {
    static int DEFAULT_BAR_COLOR = Color.parseColor("#333333");
    static int DEFAULT_BAR_TEXT_COLOR = Color.parseColor("#333333");
    static int DEFAULT_BAR_SPACE = 5;

    /**
     * convertDpToPixel converts device specific density independent pixels to pixels.
     *
     * @param dp      A value in dp unit.
     * @param context Context to get resources and device specific display metrics.
     * @return A float value that represents px.
     */
    static float convertDpToPixel(float dp, Context context) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * convertPixelsToDp converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit.
     * @param context Context to get resources and device specific display metrics
     * @return A float value that represents dp equivalent to px value
     */
    static float convertPixelsToDp(float px, Context context) {
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * @param bgColor Background color of bar.
     * @param radius  Corner Radius of bar.
     * @return Rounded Corner Drawable.
     */
    static Drawable getRoundedBarDrawable(int bgColor, int radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadii(new float[]{
                radius, radius, radius, radius, radius, radius, radius, radius
        });
        drawable.setColor(bgColor);
        return drawable;
    }


    /**
     * @param startColor The start color for the Background gradient.
     * @param endColor The end color for the Background gradient.
     * @param radius  Corner Radius of bar.
     * @return Rounded Corner Drawable.
     */
    static GradientDrawable getRoundedBarGradientDrawable(int startColor, int endColor, int radius) {

        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[] {startColor, endColor});
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadii(new float[]{
                radius, radius, radius, radius, radius, radius, radius, radius
        });
        return gradientDrawable;
    }
}
