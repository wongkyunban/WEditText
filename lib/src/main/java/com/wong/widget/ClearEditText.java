package com.wong.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.wong.animation.AnimationHelper;


public class ClearEditText extends AppCompatEditText implements View.OnFocusChangeListener, TextWatcher {
    private boolean hasFocus;
    private Drawable drawable;

    public ClearEditText(Context context) {
        super(context);
        init(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        if (attrs != null) {
            // TODO 获取xml设置的属性
        }
        setOnFocusChangeListener(this);
        addTextChangedListener(this);
        drawable = getCompoundDrawablesRelative()[2] == null ? getCompoundDrawables()[2]:getCompoundDrawablesRelative()[2];
        if(drawable == null){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                drawable = getContext().getResources().getDrawable(R.drawable.ic_backspace_black,null);
            }else{
                drawable = getContext().getResources().getDrawable(R.drawable.ic_backspace_black);
            }

            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        }
        /*hide the clear button*/
        setClearDrawableVisibility(false);
    }

    public void setClearDrawable(@Nullable Drawable drawable){
        this.drawable = drawable;
        super.setCompoundDrawables(getCompoundDrawables()[0],getCompoundDrawables()[1],drawable,getCompoundDrawables()[3]);
    }

    @Override
    public void setCompoundDrawables(@Nullable Drawable left, @Nullable Drawable top, @Nullable Drawable right, @Nullable Drawable bottom) {
        super.setCompoundDrawables(left, top, right, bottom);
        drawable = right;
        if (hasFocus && getText()!=null) {
            setClearDrawableVisibility(getText().toString().length() > 0);
        }else {
            setClearDrawableVisibility(false);
        }
    }

    @Override
    public void setCompoundDrawablesRelative(@Nullable Drawable start, @Nullable Drawable top, @Nullable Drawable end, @Nullable Drawable bottom) {
        super.setCompoundDrawablesRelative(start, top, end, bottom);
        drawable = end;
        if (hasFocus && getText()!=null) {
            setClearDrawableVisibility(getText().toString().length() > 0);
        }else {
            setClearDrawableVisibility(false);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            /*clear the text of EditText*/
            case MotionEvent.ACTION_DOWN:
                if (getCompoundDrawables()[2] != null) {
                    /*the start position of the drawable*/
                    int start = getWidth() - getTotalPaddingRight() + getPaddingRight();
                    /*the end position of the drawable*/
                    int end = getWidth();
                    /*check if event.getX() is in the range between start and end*/
                    boolean available = (event.getX() > start) && (event.getX() < end);
                    if (available) {
                        this.setText("");
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * Called when the focus state of a view has changed.
     *
     * @param v        The view whose state has changed.
     * @param hasFocus The new focus state of v.
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFocus = hasFocus;
        if (hasFocus && getText() != null && getText().length() > 0) {
            setClearDrawableVisibility(true);
        } else {
            setClearDrawableVisibility(false);
        }

    }

    /**
     * if the length of the characters,show the drawable,or hide it
     */
    private void setClearDrawableVisibility(boolean available) {
        Drawable d = available ? drawable : null;
        super.setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], d, getCompoundDrawables()[3]);
    }

    /**
     * This method is called to notify you that, within <code>s</code>,
     * the <code>count</code> characters beginning at <code>start</code>
     * are about to be replaced by new text with length <code>after</code>.
     * It is an error to attempt to make changes to <code>s</code> from
     * this callback.
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    /**
     * This method is called to notify you that, within <code>s</code>,
     * the <code>count</code> characters beginning at <code>start</code>
     * have just replaced old text that had length <code>before</code>.
     * It is an error to attempt to make changes to <code>s</code> from
     * this callback.
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (hasFocus) {
            setClearDrawableVisibility(s.length() > 0);
        }
    }

    /**
     * This method is called to notify you that, somewhere within
     * <code>s</code>, the text has been changed.
     * It is legitimate to make further changes to <code>s</code> from
     * this callback, but be careful not to get yourself into an infinite
     * loop, because any changes you make will cause this method to be
     * called again recursively.
     * (You are not told where the change took place because other
     * afterTextChanged() methods may already have made other changes
     * and invalidated the offsets.  But if you need to know here,
     * you can use {@link Spannable#setSpan} in {@link #onTextChanged}
     * to mark your place and then look up from here where the span
     * ended up.
     */
    @Override
    public void afterTextChanged(Editable s) {

    }
    /**
     * 设置晃动动画
     */
    public void setShakeAnimation() {
        this.setAnimation(AnimationHelper.shakeAnimation(5));
    }
}
