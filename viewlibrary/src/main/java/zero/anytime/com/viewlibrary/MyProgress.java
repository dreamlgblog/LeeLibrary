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

import java.text.DecimalFormat;

public class MyProgress extends View {

    private int mOuterColor = Color.RED;
    private int mInnerColor = Color.BLUE;
    private int mBoderWidth = 20;
    private int mInnerTextSize = 10;
    private int mInnerTextColor = Color.RED;
    private Paint mOutPaint;
    private Paint mInnerPaint;
    private Paint mTextPaint;
    private int mProgressMax = 100;
    private int mProgress = 0;
    public MyProgress(Context context) {
        this(context,null);
    }

    public MyProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray types = context.obtainStyledAttributes(attrs,R.styleable.MyProgress);
        mOuterColor = types.getColor(R.styleable.MyProgress_outerpColor,mOuterColor);
        mInnerColor = types.getColor(R.styleable.MyProgress_innerpColor,mInnerColor);
        mBoderWidth = (int) types.getDimension(R.styleable.MyProgress_borderpWidth,mBoderWidth);
        mInnerTextSize = types.getDimensionPixelSize(R.styleable.MyProgress_innerTextSize,mInnerTextSize);
        mInnerTextColor = types.getColor(R.styleable.MyProgress_innerTextColor,mInnerTextColor);
        mProgressMax = types.getInteger(R.styleable.MyProgress_progressMax,mProgressMax);
        mProgress = types.getInteger(R.styleable.MyProgress_progress,mProgress);
        initPatin();
        types.recycle();

    }

    private void initPatin() {
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
        mTextPaint.setColor(mInnerTextColor);
        mTextPaint.setTextSize(mInnerTextSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(widthSize>heightSize?heightSize:widthSize,widthSize>heightSize?heightSize:widthSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画最外面的圆弧 线的宽度  Paint是在线的中间，所以要减一半
        RectF rectF = new RectF(mBoderWidth/2,mBoderWidth/2,getWidth()-mBoderWidth/2,getHeight()-mBoderWidth/2);
        canvas.drawArc(rectF,0,360,false,mOutPaint);
        //获取比例
        float seepAngle = (float) mProgress/mProgressMax * 360;
        //画内圆弧
        canvas.drawArc(rectF,0,seepAngle,false,mInnerPaint);
        //画文字
        //测量文字的宽高
        String stepText = String.format("%.1f",(float)mProgress/mProgressMax*100) + "%";
        Rect textBounds = new Rect();
        mTextPaint.getTextBounds(stepText, 0, stepText.length(), textBounds);
        int dx = getWidth()/2 - textBounds.width()/2;
        //基线 baseLine
        Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top)/2 -fontMetricsInt.bottom;
        int baseLine = getHeight()/2 + dy;
        canvas.drawText(stepText,dx,baseLine,mTextPaint);
    }

    public synchronized void setmProgress(int progress){
        if(progress < 0) {
            new IllegalArgumentException("progress 不能小于0！");
        }
        this.mProgress = progress;
        invalidate();
    }
    public String formatFloat(float s) {
        DecimalFormat fmt = new DecimalFormat("##0.0");
        return fmt.format(s);
    }
}
