package zero.anytime.com.viewlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

//1. 分析效果
//2. 确定自定义属性，编写attrs
//3. 在布局中使用
// 4. 在自定义View中获取自定义属性

//5. onMeasure（）


//6. 画外内圆弧，文字

//7. 其他处理
public class QQStepView extends View {

    private int mOuterColor = Color.RED;
    private int mInnerColor = Color.BLUE;
    private int mBoderWidth = 20;
    private int mStepTextSize = 10;
    private int mStepTextColor = Color.RED;
    private Paint mOutPaint;
    private Paint mInnerPaint;
    private Paint mTextPaint;
    private int mStepMax = 10000;
    private int mCurrentStep = 0;
    public QQStepView(Context context) {
        this(context,null);
    }

    public QQStepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QQStepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray types = context.obtainStyledAttributes(attrs,R.styleable.QQStepView);
        mOuterColor = types.getColor(R.styleable.QQStepView_outerColor,mOuterColor);
        mInnerColor = types.getColor(R.styleable.QQStepView_innerColor,mInnerColor);
        mBoderWidth = (int) types.getDimension(R.styleable.QQStepView_borderWidth,mBoderWidth);
        mStepTextSize = types.getDimensionPixelSize(R.styleable.QQStepView_stepTextSize,mStepTextSize);
        mStepTextColor = types.getColor(R.styleable.QQStepView_stepTextColor,mStepTextColor);
        mStepMax = types.getInteger(R.styleable.QQStepView_stepMax,mStepMax);
        mCurrentStep = types.getInteger(R.styleable.QQStepView_stepCurrent,mCurrentStep);
        initPatin();
        types.recycle();
    }

    private void initPatin(){
        mOutPaint = new Paint();
        mOutPaint = new Paint();
        mOutPaint.setAntiAlias(true);
        mOutPaint.setStrokeWidth(mBoderWidth);
        mOutPaint.setStrokeCap(Paint.Cap.ROUND);
        mOutPaint.setColor(mOuterColor);
        mOutPaint.setStyle(Paint.Style.STROKE);
        mInnerPaint = new Paint();
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setStrokeWidth(mBoderWidth);
        mInnerPaint.setColor(mInnerColor);
        mInnerPaint.setStyle(Paint.Style.STROKE);
        mInnerPaint.setStrokeCap(Paint.Cap.ROUND);
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mStepTextColor);
        mTextPaint.setTextSize(mStepTextSize);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //调用者在布局文件中可能  wrap_content宽度或者高度不一致
        //获取模式 AT_MOST 40dp
        //宽度高度不一致取最小值。
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        //设置宽高度
        setMeasuredDimension( width > height?height:width, width > height?height:width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画最外面的圆弧 线的宽度  Paint是在线的中间，所以要减一半
        RectF rectF = new RectF(mBoderWidth/2,mBoderWidth/2,getWidth()-mBoderWidth/2,getHeight()-mBoderWidth/2);
        canvas.drawArc(rectF,135,270,false,mOutPaint);
        //获取比例
        float seepAngle =((float)mCurrentStep/mStepMax >=1 ? 1 : (float)mCurrentStep/mStepMax ) * 270 ;
        //画内圆弧
        canvas.drawArc(rectF,135,seepAngle,false,mInnerPaint);
        //画文字
        //测量文字的宽高
        String stepText = mCurrentStep+"";
        Rect textBounds = new Rect();
        mTextPaint.getTextBounds(stepText, 0, stepText.length(), textBounds);
        int dx = getWidth()/2 - textBounds.width()/2;
        //基线 baseLine
        Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top)/2 -fontMetricsInt.bottom;
        int baseLine = getHeight()/2 + dy;
        canvas.drawText(stepText,dx,baseLine,mTextPaint);
    }

    /**
     * 设置底颜色
     * @param outerColor
     */
    public synchronized void setmOuterColor(int outerColor){
        this.mOuterColor = outerColor;
    }

    public synchronized void setmInnerColor(int innerColor){
        this.mInnerColor = innerColor;
    }

    /**
     * 设置最大值
     * @param stepMax
     */
    public synchronized void setStepMax(int stepMax){
        this.mStepMax = stepMax;
    }

    /**
     * 设置进度
     * @param currentStep
     */
    public synchronized void setCurrentStep(int currentStep){
        if(currentStep < 0){
            throw new IllegalArgumentException("currentStep 不能小于0！");
        }
        this.mCurrentStep = currentStep;
        //不断绘制 invalidate -》 onDraw()
        invalidate();
    }
    /**
     * 界面过度绘制优化：
     *  1. 不要嵌套
     *  2. 能不设置背景的不设置
     *  3. 获取数据去设置 setText() setImageView()其实调用了onInvalidate
     *      最好是自己画 ，不要用系统的嵌套布局，运行效率高，实现功能效率低
     *
     */
}
