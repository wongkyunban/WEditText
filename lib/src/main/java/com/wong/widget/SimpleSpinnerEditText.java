package com.wong.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.wong.adapter.SpinnerEditTextAdapter;
import com.wong.utils.DensityUtils;
import com.wong.utils.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * usage demo:
 * SimpleSpinnerEditText simpleSpinnerEditText = (SimpleSpinnerEditText)findViewById(R.id.sset);
 * <p>
 * List<Bean> strings = new ArrayList<Bean>();
 * for (int i = 0; i < 50; i++) {
 * Bean bean = new Bean("Tom"+i,"NO."+i);
 * strings.add(bean);
 * }
 * simpleSpinnerEditText.setOptions(strings);
 * simpleSpinnerEditText.setItemTextColor(Color.BLUE);
 * simpleSpinnerEditText.setItemTextSize(DensityUtils.sp2px(this,5));
 */
public class SimpleSpinnerEditText extends AppCompatEditText implements AdapterView.OnItemClickListener {
    /*popup window to show the selection*/
    private PopupWindow mPopupWindow;
    /*View to list the data item*/
    private ListView mListView;
    private int itemTextColor = Color.BLACK;
    private float itemTextSize = 18;
    private Drawable drawable;
    private Drawable popupBackground;
    private Drawable popupDivider;
    private float popupDividerHeight;
    private SpinnerEditTextAdapter adapter = new SpinnerEditTextAdapter(getContext());
    private List mOptions = new ArrayList();

    public SimpleSpinnerEditText(Context context) {
        this(context, null);
    }

    public SimpleSpinnerEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SimpleSpinnerEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }

    private void init(Context context, AttributeSet attrs) {


        if (ObjectUtils.isNotNull(attrs)) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SimpleSpinnerEditText);
            popupBackground = typedArray.getDrawable(R.styleable.SimpleSpinnerEditText_popup_background);
            popupDivider = typedArray.getDrawable(R.styleable.SimpleSpinnerEditText_popup_divider);
            popupDividerHeight = typedArray.getDimension(R.styleable.SimpleSpinnerEditText_popup_divider_height, DensityUtils.dp2px(context, 1));
            itemTextColor = typedArray.getColor(R.styleable.SpinnerEditText_popup_item_text_color, itemTextColor);
            itemTextSize = typedArray.getDimension(R.styleable.SpinnerEditText_popup_item_text_size, DensityUtils.sp2px(getContext(), 18));
            typedArray.recycle();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupBackground = ObjectUtils.isNull(popupBackground) ? context.getResources().getDrawable(R.drawable.popup_window_bg, null) : popupBackground;
            popupDivider = ObjectUtils.isNull(popupDivider) ? context.getResources().getDrawable(R.drawable.divider_bg, null) : popupDivider;
        } else {
            popupBackground = ObjectUtils.isNull(popupBackground) ? context.getResources().getDrawable(R.drawable.popup_window_bg) : popupBackground;
            popupDivider = ObjectUtils.isNull(popupDivider) ? context.getResources().getDrawable(R.drawable.divider_bg) : popupDivider;

        }
        setTextColor(Color.BLACK);
        setLongClickable(false);
        setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        mListView = new ListView(context);
        mListView.setAdapter(adapter);
        mListView.setBackground(popupBackground);
        mListView.setDivider(popupDivider);
        mListView.setDividerHeight((int) popupDividerHeight);
        mListView.setOnItemClickListener(this);
        mPopupWindow = new PopupWindow(mListView, getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(false);
        drawable = getCompoundDrawablesRelative()[2] == null ? getCompoundDrawables()[2] : getCompoundDrawablesRelative()[2];
        if (ObjectUtils.isNull(drawable)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable = getContext().getResources().getDrawable(R.drawable.ic_arrow_down_black, null);
            } else {
                drawable = getContext().getResources().getDrawable(R.drawable.ic_arrow_down_black);
            }
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        }
        this.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
        setDrawableVisibility(false);
    }


    public void setPopupBackground(Drawable popupBackground) {
        this.popupBackground = popupBackground;
        if (ObjectUtils.isNotNull(mListView)) {
            mListView.setBackground(popupBackground);
        }
    }

    public void setPopupDivider(Drawable popupDivider) {
        this.popupDivider = popupDivider;
        if (ObjectUtils.isNotNull(mListView)) {
            mListView.setDivider(popupDivider);
        }
    }

    public void setPopupDividerHeight(float popupDividerHeight) {
        this.popupDividerHeight = popupDividerHeight;
        if (ObjectUtils.isNotNull(mListView)) {
            mListView.setDividerHeight((int) popupDividerHeight);
        }
    }

    public void setSelectDrawable(Drawable drawable) {
        this.drawable = drawable;
        super.setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], drawable, getCompoundDrawables()[3]);
    }

    public void setItemTextColor(int itemTextColor) {
        this.itemTextColor = itemTextColor;
    }

    public void setItemTextSize(float itemTextSize) {
        this.itemTextSize = itemTextSize;
    }

    @Override
    public void setCompoundDrawables(@Nullable Drawable left, @Nullable Drawable top, @Nullable Drawable right, @Nullable Drawable bottom) {
        super.setCompoundDrawables(left, top, right, bottom);
        drawable = right;
    }

    @Override
    public void setCompoundDrawablesRelative(@Nullable Drawable start, @Nullable Drawable top, @Nullable Drawable end, @Nullable Drawable bottom) {
        super.setCompoundDrawablesRelative(start, top, end, bottom);
        drawable = end;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            mPopupWindow.setWidth(getWidth());
        }
    }


    /**
     * Implement this method to handle touch screen motion events.
     * <p>
     * If this method is used to detect click actions, it is recommended that
     * the actions be performed by implementing and calling
     * {@link #performClick()}. This will ensure consistent system behavior,
     * including:
     * <ul>
     * <li>obeying click sound preferences
     * <li>dispatching OnClickListener calls
     * <li>handling {@link AccessibilityNodeInfo#ACTION_CLICK ACTION_CLICK} when
     * accessibility features are enabled
     * </ul>
     *
     * @param event The motion event.
     * @return True if the event was handled, false otherwise.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (getCompoundDrawables()[2] != null) {
                    int start = getWidth() - getTotalPaddingEnd() + getPaddingEnd() - DensityUtils.dp2px(getContext(), 11);
                    int end = getWidth();
                    boolean available = (event.getX() > start) && (event.getX() < end);
                    if (available) {
                        closeInputMethod();
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                show();
                            }
                        }, 200);
                        return true;
                    }
                }
        }
        return super.onTouchEvent(event);
    }

    private void show() {
        /**
         * measure the size of {@link ListView},if not ,{@link ListView#getMeasuredHeight()} is zero
         */
        if (ObjectUtils.isNull(mOptions) || mOptions.size() == 0) {
            return;
        }
        adapter.setList(mOptions);
        adapter.setItemTextColor(itemTextColor);
        adapter.setItemTextSize(itemTextSize);
        adapter.notifyDataSetChanged();
        mListView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int measuredHeight = getMeasuredHeight();
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        int screenHeight = metrics.heightPixels;
        int[] location = new int[2];
        getLocationOnScreen(location);
        boolean isUp = (screenHeight - location[1] - measuredHeight <= measuredHeight);


        if (isUp) {
            /**
             * mListView.getMeasuredHeight() is a height of an item of list view
             */
            int totalListHeight = mListView.getMeasuredHeight() * mListView.getCount();
            if (totalListHeight > location[1]) {
                mPopupWindow.setHeight(location[1] - 2 * measuredHeight);
            } else {
                mPopupWindow.setHeight(totalListHeight);
            }
            mPopupWindow.setAnimationStyle(R.style.AnimationPopup);

            /**
             * {@link PopupWindow#showAtLocation(View, int, int, int)
             * x ：x < 0时，向左偏移， x >0 时，向右偏移
             * y ：显示效果受gravity参数影响。当参数不带Gravity.BOTTOM时，y < 0，向上偏移， y > 0 ，向下偏移；当参数带有Gravity.BOTTOM时, y < 0,向下偏移，y > 0，向上偏移
             */
            mPopupWindow.showAtLocation(this, Gravity.TOP | Gravity.START, location[0], location[1] - mPopupWindow.getHeight());
        } else {
            mPopupWindow.setAnimationStyle(R.style.AnimationDropDown);

            mPopupWindow.showAsDropDown(this, 0, 5);
        }


    }

    public <T> void setOptions(@NonNull List<T> options) {
        this.mOptions = options;
        setDrawableVisibility(options.size() > 0);
    }

    private void closeInputMethod() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(this.getWindowToken(), 0);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.setText(mListView.getAdapter().getItem(position).toString());
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }


    /**
     * @param available if true set drawable instance ,if false set null
     */
    private void setDrawableVisibility(boolean available) {
        Drawable d = available ? drawable : null;
        super.setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], d, getCompoundDrawables()[3]);
    }
}
