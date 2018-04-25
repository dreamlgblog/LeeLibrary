package zero.anytime.com.viewlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MyTextView extends View {
    private String mText;
    private int mTextSize = 15;
    private int mTextColor = Color.RED;

    //在new方法中个调用
    public MyTextView(Context context) {
        this(context,null);
    }
    //这个在layout文件中调用
    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }
    //这个在style中调用
    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
       /* //获取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.MyTextView);
        //获取text属性
        mText = typedArray.getString(R.styleable.MyTextView_text);
        mTextColor = typedArray.getColor(R.styleable.MyTextView_textColor,mTextColor);
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.MyTextView_textSize,mTextSize);
        //回收
        typedArray.recycle();*/
    }

    /** 测量模式 ：UNSPECIFIED  任意大小，想多大就多大，尽可能大，一般我们不会遇到，
     *                        如ListView ，RecyclerView， ScrollerView
     *                        测量子View 的时候给的就是UNSPECIFIED,
     *
     *           EXACTLY    一个确定的值，比如在布局中你是这样写的，
     *                      laout_width= "100dp",mathch_parent,
     *                      fill_parent
     *           AT_MOST    包裹内容，比如布局中你是这样写的 wrap_content.
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取测量的模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if(widthMode == MeasureSpec.AT_MOST){

        }
    }

    /**
     * 画图
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


    /**
     * 触摸事件
     * @param event 事件拦截
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return super.onTouchEvent(event);
    }
}
