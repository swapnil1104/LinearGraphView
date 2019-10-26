package com.broooapps.lineargraphview2;


import android.animation.*;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Swapnil Tiwari on 2019-08-07.
 * swapniltiwari775@gmail.com
 */
public class LinearGraphView extends View {

    // Paint Object for background
    private Paint mBgPaint;

    private ArrayList<Paint> dataPaintList;
    private ArrayList<RectF> dataRect;

    private Path boundaryPath;

    private float cornerRadius = 16;
    // width of the view
    private int width;

    // height of the view
    private int height;

    // Context

    private float totalValue;
    private float mScaleFactor;
    private boolean firstAnim = true;

    private boolean shouldStartAnim = false;
    private float length;
    private List<DataModel> dataList;

    @NonNull
    private WeakReference<Context> contextWeakReference;
    @Nullable
    private AttributeSet attributeSet;
    private int defStyleAttr = 0;

    @ColorInt
    private int borderColor;
    private int strokeWidth = 2;
    private int strokeAnimDuration;

    public LinearGraphView(Context context) {
        super(context);
        this.contextWeakReference = new WeakReference<>(context);

        init();
    }

    public LinearGraphView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.contextWeakReference = new WeakReference<>(context);
        attributeSet = attrs;

        init();
    }

    public LinearGraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.contextWeakReference = new WeakReference<>(context);
        this.attributeSet = attrs;
        this.defStyleAttr = defStyleAttr;

        init();
    }

    private void getAttrsFromTypedArray(AttributeSet attributeSet) {
        final TypedArray a = getContext().obtainStyledAttributes(attributeSet, R.styleable.LinearGraphView, 0, 0);

        // cornerRadius = a.getDimensionPixelSize(R.styleable.LinearGraphView_lgv_corner_radius, 16);
        // strokeWidth = a.getDimensionPixelSize(R.styleable.LinearGraphView_lgv_stroke_width, 2);
        borderColor = a.getColor(R.styleable.LinearGraphView_lgv_border_color, getResources().getColor(R.color.linear_graph_border));
        strokeAnimDuration = a.getInt(R.styleable.LinearGraphView_lgv_border_anim_duration, 1000);

        a.recycle();
    }

    private void init() {
        getAttrsFromTypedArray(attributeSet);

        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        dataRect = new ArrayList<>();
        mBgPaint = new Paint();
        mBgPaint.setStrokeWidth(strokeWidth);
        mBgPaint.setStyle(Paint.Style.STROKE);
        mBgPaint.setAntiAlias(true);
        mBgPaint.setColor(borderColor);

        boundaryPath = new Path();
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "phase", 1.0f, 0.0f);
        animator.setDuration(strokeAnimDuration);
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                firstAnim = false;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPath(boundaryPath, mBgPaint);

        int itr = 0;
        for (RectF rectF : dataRect) {
            if (dataRect.size() == 1) {
                canvas.drawRoundRect(rectF.left * mScaleFactor,
                        rectF.top,
                        rectF.right * mScaleFactor,
                        rectF.bottom,
                        cornerRadius, cornerRadius, dataPaintList.get(itr));

            } else if (itr == 0) {
                Path path = RoundedRect(rectF.left * mScaleFactor, rectF.top, rectF.right * mScaleFactor, rectF.bottom, cornerRadius, cornerRadius,
                        true, false, false, true);

                canvas.drawPath(path, dataPaintList.get(itr));
            } else if (itr == dataRect.size() - 1) {

                Path path = RoundedRect(rectF.left * mScaleFactor, rectF.top, rectF.right * mScaleFactor, rectF.bottom, cornerRadius, cornerRadius,
                        false, true, true, false);

                canvas.drawPath(path, dataPaintList.get(itr));

            } else {
                canvas.drawRect(rectF.left * mScaleFactor,
                        rectF.top,
                        rectF.right * mScaleFactor,
                        rectF.bottom,
                        dataPaintList.get(itr));
            }
            itr++;

        }
    }

    private int measureDimension(int desiredSize, int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = desiredSize;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }

        return result;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = getMeasuredWidth();
        height = measureDimension(LinearGraphView.convertDpToPixel(16, contextWeakReference.get()), heightMeasureSpec);
        setMeasuredDimension(width, height);

        boundaryPath = RoundedRect(2, 2, width - 2, height - 2, cornerRadius, cornerRadius);

        PathMeasure measure = new PathMeasure(boundaryPath, true);
        length = measure.getLength();

        if (shouldStartAnim) {
            startAnim();
        }
    }

    public void startAnim() {

        dataPaintList = new ArrayList<>();
        dataRect = new ArrayList<>();

        float prev = 2;

        for (DataModel model : dataList) {

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.parseColor(model.colorRes));
            dataPaintList.add(paint);

            float curWidth = model.data / totalValue * width;
            if (curWidth < cornerRadius) {
                curWidth = cornerRadius * 1.5F;
            }
            RectF rectF = new RectF(prev, 2, (prev + curWidth - 2 > width) ? width - 2 : prev + curWidth, height - 2);
            prev += curWidth - 2;

            dataRect.add(rectF);

        }

        PropertyValuesHolder propertyFactor = PropertyValuesHolder.ofFloat("PROPERTY_FACTOR", .2f, 1f);
        PropertyValuesHolder alphaFactor = PropertyValuesHolder.ofInt("PROPERTY_ALPHA", 0, 255);

        ValueAnimator valueAnimator = new ValueAnimator();

        valueAnimator.setValues(alphaFactor, propertyFactor);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setDuration(512);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                mScaleFactor = (float) animation.getAnimatedValue("PROPERTY_FACTOR");

                invalidate();
            }
        });

        if (firstAnim) {
            valueAnimator.setStartDelay(900);
        }
        valueAnimator.start();

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                shouldStartAnim = false;
            }
        });

    }

    public void setData(List<DataModel> dataList, float totalValue) {
        this.dataList = dataList;
        this.totalValue = totalValue;

        shouldStartAnim = true;
    }

    public static Path RoundedRect(
            float left, float top, float right, float bottom, float rx, float ry,
            boolean tl, boolean tr, boolean br, boolean bl
    ) {
        Path path = new Path();
        if (rx < 0) rx = 0;
        if (ry < 0) ry = 0;
        float width = right - left;
        float height = bottom - top;
        if (rx > width / 2) rx = width / 2;
        if (ry > height / 2) ry = height / 2;
        float widthMinusCorners = (width - (2 * rx));
        float heightMinusCorners = (height - (2 * ry));

        path.moveTo(right, top + ry);
        if (tr)
            path.rQuadTo(0, -ry, -rx, -ry);
        else {
            path.rLineTo(0, -ry);
            path.rLineTo(-rx, 0);
        }
        path.rLineTo(-widthMinusCorners, 0);
        if (tl)
            path.rQuadTo(-rx, 0, -rx, ry);
        else {
            path.rLineTo(-rx, 0);
            path.rLineTo(0, ry);
        }
        path.rLineTo(0, heightMinusCorners);

        if (bl)
            path.rQuadTo(0, ry, rx, ry);
        else {
            path.rLineTo(0, ry);
            path.rLineTo(rx, 0);
        }

        path.rLineTo(widthMinusCorners, 0);
        if (br)
            path.rQuadTo(rx, 0, rx, -ry);
        else {
            path.rLineTo(rx, 0);
            path.rLineTo(0, -ry);
        }

        path.rLineTo(0, -heightMinusCorners);

        path.close();
        return path;
    }

    public void setPhase(float phase) {
        mBgPaint.setPathEffect(createPathEffect(length, phase, 0.0f));
        invalidate();
    }

    private static PathEffect createPathEffect(float pathLength, float phase, float offset) {
        return new DashPathEffect(new float[]{pathLength, pathLength},
                Math.max(phase * pathLength, offset));
    }


    static public Path RoundedRect(float left, float top, float right, float bottom, float rx, float ry) {
        Path path = new Path();
        if (rx < 0) rx = 0;
        if (ry < 0) ry = 0;
        float width = right - left;
        float height = bottom - top;
        if (rx > width / 2) rx = width / 2;
        if (ry > height / 2) ry = height / 2;
        float widthMinusCorners = (width - (2 * rx));
        float heightMinusCorners = (height - (2 * ry));

        path.moveTo(right, top + ry);
        path.rQuadTo(0, -ry, -rx, -ry);//top-right corner
        path.rLineTo(-widthMinusCorners, 0);
        path.rQuadTo(-rx, 0, -rx, ry); //top-left corner
        path.rLineTo(0, heightMinusCorners);


        path.rQuadTo(0, ry, rx, ry);//bottom-left corner
        path.rLineTo(widthMinusCorners, 0);
        path.rQuadTo(rx, 0, rx, -ry); //bottom-right corner

        path.rLineTo(0, -heightMinusCorners);

        path.close();//Given close, last lineto can be removed.

        return path;
    }

    private static int convertDpToPixel(float dp, Context context) {
        return (int) (dp * (context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
