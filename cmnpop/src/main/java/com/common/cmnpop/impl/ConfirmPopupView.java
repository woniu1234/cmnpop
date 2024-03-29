package com.common.cmnpop.impl;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.common.cmnpop.R;
import com.common.cmnpop.XPopup;
import com.common.cmnpop.core.CenterPopupView;
import com.common.cmnpop.interfaces.OnCancelListener;
import com.common.cmnpop.interfaces.OnConfirmListener;


/**
 * Description: 确定和取消的对话框
 */
public class ConfirmPopupView extends CenterPopupView implements View.OnClickListener {
    OnCancelListener cancelListener;
    OnConfirmListener confirmListener;
    TextView tv_title, tv_content, tv_cancel, tv_confirm;
    CharSequence title, content, hint, cancelText, confirmText;
    EditText et_input;
    View divider1, divider2;
    public boolean isHideCancel = false;

    /**
     * @param bindLayoutId layoutId 要求布局中必须包含的TextView以及id有：tv_title，tv_content，tv_cancel，tv_confirm
     */
    public ConfirmPopupView(@NonNull Context context, int bindLayoutId) {
        super(context);
        this.bindLayoutId = bindLayoutId;
        addInnerContent();
    }

    @Override
    protected int getImplLayoutId() {
        return bindLayoutId != 0 ? bindLayoutId : R.layout._xpopup_center_impl_confirm;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        tv_title = findViewById(R.id.tv_title);
        tv_content = findViewById(R.id.tv_content);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_confirm = findViewById(R.id.tv_confirm);
        tv_content.setMovementMethod(LinkMovementMethod.getInstance());
        et_input = findViewById(R.id.et_input);
        divider1 = findViewById(R.id.xpopup_divider1);
        divider2 = findViewById(R.id.xpopup_divider2);

        tv_cancel.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);

        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
        } else {
            tv_title.setVisibility(GONE);
        }

        if (!TextUtils.isEmpty(content)) {
            tv_content.setText(content);
        } else {
            tv_content.setVisibility(GONE);
        }
        if (!TextUtils.isEmpty(cancelText)) {
            tv_cancel.setText(cancelText);
        }
        if (!TextUtils.isEmpty(confirmText)) {
            tv_confirm.setText(confirmText);
        }
        if (isHideCancel) {
            tv_cancel.setVisibility(GONE);
            if (divider2 != null) divider2.setVisibility(GONE);
        }
        applyTheme();
    }

    protected void applyLightTheme() {
        super.applyLightTheme();
        tv_title.setTextColor(getResources().getColor(R.color._xpopup_content_color));
        tv_content.setTextColor(getResources().getColor(R.color._xpopup_content_color));
        tv_cancel.setTextColor(popupInfo.cancelColor);
        tv_confirm.setTextColor(popupInfo.confirmColor);
        if (divider1 != null)
            divider1.setBackgroundColor(getResources().getColor(R.color._xpopup_list_divider));
        if (divider2 != null)
            divider2.setBackgroundColor(getResources().getColor(R.color._xpopup_list_divider));
    }

    public TextView getTitleTextView() {
        return findViewById(R.id.tv_title);
    }

    public TextView getContentTextView() {
        return findViewById(R.id.tv_content);
    }

    public TextView getCancelTextView() {
        return findViewById(R.id.tv_cancel);
    }

    public TextView getConfirmTextView() {
        return findViewById(R.id.tv_confirm);
    }

    @Override
    protected void applyDarkTheme() {
        super.applyDarkTheme();
        tv_title.setTextColor(getResources().getColor(R.color._xpopup_white_color));
        tv_content.setTextColor(getResources().getColor(R.color._xpopup_white_color));
        tv_cancel.setTextColor(getResources().getColor(R.color._xpopup_white_color));
        tv_confirm.setTextColor(getResources().getColor(R.color._xpopup_white_color));
        if (divider1 != null)
            divider1.setBackgroundColor(getResources().getColor(R.color._xpopup_list_dark_divider));
        if (divider2 != null)
            divider2.setBackgroundColor(getResources().getColor(R.color._xpopup_list_dark_divider));
    }

    public ConfirmPopupView setListener(OnConfirmListener confirmListener, OnCancelListener cancelListener) {
        this.cancelListener = cancelListener;
        this.confirmListener = confirmListener;
        return this;
    }

    public ConfirmPopupView setTitleContent(CharSequence title, CharSequence content, CharSequence hint) {
        this.title = title;
        this.content = content;
        this.hint = hint;
        return this;
    }

    public ConfirmPopupView setCancelText(CharSequence cancelText) {
        this.cancelText = cancelText;
        return this;
    }

    public ConfirmPopupView setConfirmText(CharSequence confirmText) {
        this.confirmText = confirmText;
        return this;
    }

    @Override
    public void onClick(View v) {
        if (v == tv_cancel) {
            if (cancelListener != null) cancelListener.onCancel();
            dismiss();
        } else if (v == tv_confirm) {
            if (confirmListener != null) confirmListener.onConfirm();
            if (popupInfo.autoDismiss) dismiss();
        }
    }

    protected int getMaxWidth() {
        return popupInfo.maxWidth == 0 ? super.getMaxWidth() : popupInfo.maxWidth;
    }
}
