package com.awesomeproject.view;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;

/**
 * 圆、三角形、正方形变换
 * on 2022/11/9
 */
public class ShapeView extends View {
    /**
     * 画笔
     */
    private final Paint paint = new Paint();

    /**
     * 圆形颜色
     */
    private final int CIRCLE_COLOR = Color.parseColor("#AA738FFE");

    /**
     * 正方形颜色
     */
    private final int SQUARE_COLOR = Color.parseColor("#AAE84E40");

    /**
     * 三角形颜色
     */
    private final int TRIANGLE_COLOR = Color.parseColor("#AA72D572");

    /**
     * 当前形状
     */
    private Shape currentShape = Shape.CIRCLE;

    /**
     * 正方形Rect
     */
    private final Rect squareRect = new Rect();

    /**
     * 三角形path
     */
    private final Path path = new Path();

    public ShapeView(Context context) {
        this(context, null);
    }

    public ShapeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setDither(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //设置宽高始终一致
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSize > heightSize) {
            widthSize = heightSize;
        } else {
            heightSize = widthSize;
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //设置正方形rect
        squareRect.left = 0;
        squareRect.top = 0;
        squareRect.right = getWidth();
        squareRect.bottom = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        switch (currentShape) {
            case SQUARE:
                paint.setColor(SQUARE_COLOR);
                canvas.drawRect(squareRect, paint);
                break;
            case CIRCLE:
                paint.setColor(CIRCLE_COLOR);
                canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, getWidth() / 2f, paint);
                break;
            case TRIANGLE:
                paint.setColor(TRIANGLE_COLOR);
                path.moveTo(getWidth() / 2f, 0f);
                path.lineTo(0f, (float) (Math.sqrt(3.0) * getWidth() / 2f));
                path.lineTo(getWidth(), (float) (Math.sqrt(3.0) * getWidth() / 2f));
                path.close();
                canvas.drawPath(path, paint);
                break;
        }
    }

    public void exchangeShape() {
        switch (currentShape) {
            case CIRCLE:
                currentShape = Shape.SQUARE;
                break;
            case SQUARE:
                currentShape = Shape.TRIANGLE;
                break;
            case TRIANGLE:
                currentShape = Shape.CIRCLE;
                break;
        }
        postInvalidate();
    }

    /**
     * 获取当前形状
     */
    public Shape getCurrentShape() {
        return currentShape;
    }

    public enum Shape {
        CIRCLE, SQUARE, TRIANGLE
    }
}
