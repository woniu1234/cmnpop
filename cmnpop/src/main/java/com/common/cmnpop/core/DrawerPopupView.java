package com.common.cmnpop.core;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.common.cmnpop.R;
import com.common.cmnpop.animator.PopupAnimator;
import com.common.cmnpop.enums.PopupPosition;
import com.common.cmnpop.enums.PopupStatus;
import com.common.cmnpop.util.KeyboardUtils;
import com.common.cmnpop.util.XPopupUtils;
import com.common.cmnpop.widget.PopupDrawerLayout;

/**
 * Description: 带Drawer的弹窗
 */
public abstract class DrawerPopupView extends BasePopupView {
    protected PopupDrawerLayout drawerLayout;
    protected FrameLayout drawerContentContainer;
    float mFraction = 0f;

    public DrawerPopupView(@NonNull Context context) {
        super(context);
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerContentContainer = findViewById(R.id.drawerContentContainer);
        View contentView = LayoutInflater.from(getContext()).inflate(getImplLayoutId(), drawerContentContainer, false);
        drawerContentContainer.addView(contentView);
    }

    @Override
    public View getPopupImplView() {
        return drawerContentContainer.getChildAt(0);
    }

    @Override
    final protected int getInnerLayoutId() {
        return R.layout._xpopup_drawer_popup_view;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        drawerLayout.isDismissOnTouchOutside = popupInfo.isDismissOnTouchOutside;
        drawerLayout.setOnCloseListener(new PopupDrawerLayout.OnCloseListener() {
            @Override
            public void onClose() {
                beforeDismiss();
                if (popupInfo != null && popupInfo.xPopupCallback != null)
                    popupInfo.xPopupCallback.beforeDismiss(DrawerPopupView.this);
                doAfterDismiss();
            }

            @Override
            public void onOpen() {
            }

            @Override
            public void onDrag(int x, float fraction, boolean isToLeft) {
                if (popupInfo == null) return;
                if (popupInfo.xPopupCallback != null)
                    popupInfo.xPopupCallback.onDrag(DrawerPopupView.this,
                            x, fraction, isToLeft);
                mFraction = fraction;
                if (popupInfo.hasShadowBg) shadowBgAnimator.applyColorValue(fraction);
                postInvalidate();
            }
        });
        getPopupImplView().setTranslationX(popupInfo.offsetX);
        getPopupImplView().setTranslationY(popupInfo.offsetY);
        drawerLayout.setDrawerPosition(popupInfo.popupPosition == null ? PopupPosition.Left : popupInfo.popupPosition);
        drawerLayout.enableDrag = popupInfo.enableDrag;
        drawerLayout.getChildAt(0).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupInfo != null) {
                    if (popupInfo.xPopupCallback != null) {
                        popupInfo.xPopupCallback.onClickOutside(DrawerPopupView.this);
                    }
                    if (popupInfo.isDismissOnTouchOutside != null) {
                        dismiss();
                    }
                }
            }
        });
    }

    Paint paint = new Paint();
    Rect shadowRect;
    public ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    int currColor = Color.TRANSPARENT;
    int defaultColor = Color.TRANSPARENT;

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (popupInfo != null && popupInfo.hasStatusBarShadow) {
            if (shadowRect == null) {
                shadowRect = new Rect(0, 0, getMeasuredWidth(), XPopupUtils.getStatusBarHeight());
            }
            paint.setColor((Integer) argbEvaluator.evaluate(mFraction, defaultColor, getStatusBarBgColor()));
            canvas.drawRect(shadowRect, paint);
        }
    }

    public void doStatusBarColorTransform(boolean isShow) {
        if (popupInfo != null && popupInfo.hasStatusBarShadow) {
            //状态栏渐变动画
            ValueAnimator animator = ValueAnimator.ofObject(argbEvaluator,
                    isShow ? Color.TRANSPARENT : getStatusBarBgColor(),
                    isShow ? getStatusBarBgColor() : Color.TRANSPARENT);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    currColor = (Integer) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            animator.setDuration(getAnimationDuration()).start();
        }
    }

    @Override
    public void doShowAnimation() {
        drawerLayout.open();
        doStatusBarColorTransform(true);
    }

    @Override
    public void doDismissAnimation() {
    }

    protected void doAfterDismiss() {
        if (popupInfo != null && popupInfo.autoOpenSoftInput)
            KeyboardUtils.hideSoftInput(this);
        handler.removeCallbacks(doAfterDismissTask);
        handler.postDelayed(doAfterDismissTask, 0);
    }

    @Override
    public void dismiss() {
        if (popupInfo == null) return;
        if (popupStatus == PopupStatus.Dismissing) return;
        popupStatus = PopupStatus.Dismissing;
        if (popupInfo.autoOpenSoftInput) KeyboardUtils.hideSoftInput(this);
        clearFocus();
        doStatusBarColorTransform(false);
        // 关闭Drawer，由于Drawer注册了关闭监听，会自动调用dismiss
        drawerLayout.close();
    }

    @Override
    protected PopupAnimator getPopupAnimator() {
        return null;
    }
}
