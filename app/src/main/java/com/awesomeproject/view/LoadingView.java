package com.awesomeproject.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.awesomeproject.R;
import com.awesomeproject.view.ShapeView;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.JustifyContent;

import org.w3c.dom.Text;

public class LoadingView extends LinearLayout {
    private final ShapeView shapeView;
    private final View shadowView;
    private float translationYValue = 0f;
    private final long DURATION_TIME = 500L;
    private boolean isStopLoading = false;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(getContext(), R.layout.view_loading, this);
        shapeView = findViewById(R.id.shape_view);
        shadowView = findViewById(R.id.shadow_view);
        translationYValue = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80f, getResources().getDisplayMetrics());
        post(this::downAnimation);
    }

    private void downAnimation() {
        if (isStopLoading) {
            return;
        }
        ObjectAnimator shapeViewAnimation = ObjectAnimator.ofFloat(shapeView, "translationY", 0f, translationYValue);
        ObjectAnimator shadowViewAnimation = ObjectAnimator.ofFloat(shadowView, "scaleX", 1f, 0.3f);
        AnimatorSet downAnimatorSet = new AnimatorSet();
        downAnimatorSet.playTogether(shapeViewAnimation, shadowViewAnimation);
        downAnimatorSet.setDuration(DURATION_TIME);
        downAnimatorSet.start();
        downAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                upAnimation();
            }
        });
    }

    private void upAnimation() {
        ObjectAnimator shapeViewAnimation = ObjectAnimator.ofFloat(shapeView, "translationY", translationYValue, 0f);
        ObjectAnimator shadowViewAnimation = ObjectAnimator.ofFloat(shadowView, "scaleX", 0.3f, 1f);
        shapeView.exchangeShape();
        AnimatorSet upAnimatorSet = new AnimatorSet();
        upAnimatorSet.playTogether(shapeViewAnimation, shadowViewAnimation, remoteShapeView());
        upAnimatorSet.setDuration(DURATION_TIME);
        upAnimatorSet.start();
        upAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                downAnimation();
            }
        });
    }

    private ObjectAnimator remoteShapeView() {
        switch (shapeView.getCurrentShape()) {
            case CIRCLE:
            case SQUARE:
                return ObjectAnimator.ofFloat(shapeView, "rotation", 0f, 180f);
            default:
                return ObjectAnimator.ofFloat(shapeView, "rotation", 0f, 120f);
        }
    }

    public void stopAnimation() {
        isStopLoading = true;
        shapeView.clearAnimation();
        shadowView.clearAnimation();
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
            removeAllViews();
        }
    }
}