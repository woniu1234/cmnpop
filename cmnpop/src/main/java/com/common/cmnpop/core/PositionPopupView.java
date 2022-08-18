package com.common.cmnpop.core;

import static com.common.cmnpop.enums.PopupAnimation.ScaleAlphaFromCenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.common.cmnpop.R;
import com.common.cmnpop.animator.PopupAnimator;
import com.common.cmnpop.animator.ScaleAlphaAnimator;
import com.common.cmnpop.util.XPopupUtils;


/**
 * Description: 用于自由定位的弹窗
 */
public class PositionPopupView extends BasePopupView {
    FrameLayout positionPopupContainer;

    public PositionPopupView(@NonNull Context context) {
        super(context);
        positionPopupContainer = findViewById(R.id.positionPopupContainer);
        View contentView = LayoutInflater.from(getContext()).inflate(getImplLayoutId(), positionPopupContainer, false);
        positionPopupContainer.addView(contentView);
    }

    @Override
    final protected int getInnerLayoutId() {
        return R.layout._xpopup_position_popup_view;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        XPopupUtils.applyPopupSize((ViewGroup) getPopupContentView(), getMaxWidth(), getMaxHeight(),
                getPopupWidth(), getPopupHeight(), new Runnable() {
                    @Override
                    public void run() {
                        if (popupInfo == null) return;
                        if (popupInfo.isCenterHorizontal) {
                            float left = !XPopupUtils.isLayoutRtl(getContext()) ? (XPopupUtils.getAppWidth(getContext()) - positionPopupContainer.getMeasuredWidth()) / 2f
                                    : -(XPopupUtils.getAppWidth(getContext()) - positionPopupContainer.getMeasuredWidth()) / 2f;
                            positionPopupContainer.setTranslationX(left);
                        } else {
                            positionPopupContainer.setTranslationX(popupInfo.offsetX);
                        }
                        positionPopupContainer.setTranslationY(popupInfo.offsetY);
                        initAndStartAnimation();
                    }
                });
    }

    protected void initAndStartAnimation() {
        initAnimator();
        doShowAnimation();
        doAfterShow();
    }

    @Override
    protected PopupAnimator getPopupAnimator() {
        return new ScaleAlphaAnimator(getPopupContentView(), getAnimationDuration(), ScaleAlphaFromCenter);
    }
}
