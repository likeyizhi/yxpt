package com.bbld.yxpt.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by likey on 2017/3/27.
 */

public class ZGridView extends GridView {
    public ZGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZGridView(Context context) {
        super(context);
    }

    public ZGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
