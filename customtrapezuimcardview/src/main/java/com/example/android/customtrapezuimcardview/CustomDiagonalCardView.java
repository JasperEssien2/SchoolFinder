package com.example.android.customtrapezuimcardview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class CustomDiagonalCardView extends ViewGroup {

    Path path = new Path();
    RectF ovalRect = new RectF();
    private Paint mCardPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private ImageView imageView;

    public CustomDiagonalCardView(Context context) {
        super(context);
        imageView = new ImageView(context);
    }

    public CustomDiagonalCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        imageView = new ImageView(context);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomDiagonalCardView);
//        int count = typedArray.getInt(R.styleable.PageIndicatorView_piv_count,0);
        typedArray.recycle();
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
//        int parentWidth = MeasureSpec.getSize(getWidth());
//        int parentHeight = MeasureSpec.getSize(getHeight());
//        this.setMeasuredDimension(parentWidth/2, parentHeight);
//        this.setLayoutParams(new *ParentLayoutType*.LayoutParams(parentWidth/2,parentHeight));
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


//        if (getChildCount() != 0)
//            getChildAt(0).measure(getWidth(), getHeight());
//        getChildAt(0).onDrawForeground();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        mCardPaint.setColor(ContextCompat.getColor(this.getContext(), R.color.colorwhite));
        drawTrapezium(canvas, mCardPaint, getWidth());
//        getBackground().draw(canvas);
//        canvas.clipP
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 1. Setup initial constraints.
        int widthConstraints = getPaddingLeft() + getPaddingRight();
        int heightConstraints = getPaddingTop() + getPaddingBottom();
        int width = getWidth();
        int height = getHeight();

        imageView.setImageResource(R.color.colorBlue);
        // 2. Measure the ProfilePhoto
        measureChildWithMargins(
                imageView,
                widthMeasureSpec,
                widthConstraints,
                heightMeasureSpec,
                heightConstraints);

//        getChildAt(0).setLayoutParams(new CustomDiagonalCardView.LayoutParams(getWidth()/2,getHeight()));
//        measureChildren(MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY),
//                MeasureSpec.makeMeasureSpec(getMeasuredWidth() / 2, MeasureSpec.EXACTLY));
//
////        measureChild(getChildAt(0), getWidth(), getHeight());
//        Log.e("onLayout", "child id ---- " + getChildAt(0).getId());
//        setMeasuredDimension(width, height);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(
                resolveSize(width, widthMeasureSpec),
                resolveSize(height, heightMeasureSpec));
    }

    @Override
    protected void measureChildWithMargins(
            View child,
            int parentWidthMeasureSpec,
            int widthUsed,
            int parentHeightMeasureSpec,
            int heightUsed) {
        imageView.setImageResource(R.color.colorBlue);
        imageView.setBackgroundResource(R.color.colorBlue);
        imageView.setLayoutParams(new CustomDiagonalCardView.MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        child = imageView;
        MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

        int childWidthMeasureSpec = getChildMeasureSpec(
                parentWidthMeasureSpec,
                lp != null ? widthUsed + lp.leftMargin + lp.rightMargin : 8,
                lp != null ? lp.width : getWidth());

        int childHeightMeasureSpec = getChildMeasureSpec(
                parentHeightMeasureSpec,
                lp != null ? heightUsed + lp.topMargin + lp.bottomMargin : 8,
                lp != null ? lp.height : getHeight());

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    private void drawTriangle(Canvas canvas, Paint paint, int x, int y, int width) {
        int halfWidth = width / 2;

        Path path = new Path();
        path.moveTo(getX(), getY() - width); //Top
        path.lineTo(getX() - width, getY() + width); //Bottom left
        path.lineTo(getX() + width, getY() + width); // Bottom right
        path.lineTo(getX(), getY() - width); // Back to Top
//        path.rewind();
        path.close();
        Log.e("---", "getX() ----- " + getX() + ", --- getY() --" + getY());
        canvas.drawPath(path, paint);
        canvas.rotate(-230);
//        canvas.
    }

    public void drawTrapezium(Canvas canvas, Paint paint, int width) {
        int halfWidth = width / 2;
        paint.setShadowLayer(20, 20, 10, Color.GRAY);
//        paint.setS
        setLayerType(LAYER_TYPE_SOFTWARE, paint);

        Path path = new Path();
        path.moveTo(getWidth(), -width); // Top
        path.lineTo(getWidth(), (getY() + width) / 2);
        path.lineTo(-getWidth(), width);
        path.lineTo(-getWidth(), -width);
        path.close();


        canvas.drawPath(path, paint);
    }

    public void drawRhombus(Canvas canvas, Paint paint, int x, int y, int width) {
        int halfWidth = width / 2;

        Path path = new Path();
        path.moveTo(x, y + halfWidth); // Top
        path.lineTo(x - halfWidth, y); // Left
        path.lineTo(x, y - halfWidth); // Bottom
        path.lineTo(x + halfWidth, y); // Right
        path.lineTo(x, y + halfWidth); // Back to Top
        path.close();

        canvas.drawPath(path, paint);
    }


}
