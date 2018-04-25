package zero.anytime.com.viewlibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import java.util.List;

public class MyListView extends ListView {
    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 这个嵌套显示全部
     *   if (heightMode == MeasureSpec.AT_MOST) {
     // TODO: after first layout we should maybe start at the first visible position, not 0
     heightSize = measureHeightOfChildren(widthMeasureSpec, 0, NO_POSITION, heightSize, -1);
     }
     * 为什么给最大值 measureHeightOfChildren，
     * @param widthMeasureSpec  会包含两个信息32位，第一个是模式：2位， 包含一个size值：30位
     *                                                   xx                xxxxxxxxxxxxxxxxx....
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST );
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
