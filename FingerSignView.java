package com.youping.apple.support.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by liangtianmeng on 18-4-11.
 */

public class FingerSignView extends View {

    private static final String TAG = "FingerSignView";
    private Paint mPaint;
    private TextPaint mTextPaint;
    private Context mContext;
    private float mCurrentX;
    private float mCurrentY;
    private Path signPath;
    private Path currentPath;
    private Path backgroundPath;
    private BitmapDrawable bitmapDrawable;
    private RectF rectF = new RectF();
    private RoundRectShape roundShape;
    StaticLayout layout;
    private static final String SIGN_SUGGESSION = "请当前出售人使用正楷签名";

    public FingerSignView(Context context) {
        this(context, null);
    }

    public FingerSignView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FingerSignView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }


    private void initView() {
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(13f);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mTextPaint = new TextPaint();
        mTextPaint.setColor(Color.GRAY);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStrokeWidth(5f);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(50f);
        //mTextPaint.setStrokeJoin(Paint.Join.ROUND);
        layout = new StaticLayout(SIGN_SUGGESSION.toString(), mTextPaint, 300, Layout.Alignment.ALIGN_CENTER, 1f, 0f, true);

        signPath = new Path();
        currentPath = new Path();
        backgroundPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.clipPath(backgroundPath);
        canvas.drawColor(Color.WHITE);
        canvas.restore();

        canvas.save();
        // calculate text size
        float textWidth = mTextPaint.measureText(SIGN_SUGGESSION) / 2;
        canvas.translate((rectF.right - rectF.left - textWidth) / 2 , (rectF.bottom - rectF.top) / 2);
        //canvas.drawText(SIGN_SUGGESSION, 0f, (rectF.bottom - rectF.top) / 2, mTextPaint);
        layout.draw(canvas);
        canvas.restore();

        canvas.save();
        currentPath.lineTo(mCurrentX, mCurrentY);
        canvas.drawPath(currentPath, mPaint);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int actionCode = event.getAction();
        float x = event.getX();
        float y = event.getY();
        mCurrentX = x;
        mCurrentY = y;
        switch (actionCode) {
            case MotionEvent.ACTION_DOWN:
                currentPath.moveTo(mCurrentX, mCurrentY);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                invalidate();
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = getMeasuredHeight();
        int width = getMeasuredWidth();
        int left = 20;
        int top = 20;
        int right = width - left;
        int bottom = height - top;
        rectF.set(left, top, right, bottom);
        backgroundPath.addRoundRect(rectF, 20f, 20f, Path.Direction.CW);
    }

    public void reSetSignView() {
        if(currentPath == null) {
            return;
        }
        currentPath.reset();
        mCurrentX = 0f;
        mCurrentY = 0f;
        invalidate();
    }
}
