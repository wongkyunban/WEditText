package com.wong.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.wong.utils.DensityUtils;
import com.wong.utils.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * usage demo:
 * String[] strings = new String[10];
 * for (int i = 0; i < 10; i++) {
 * strings[i] = "No." + i + "号";
 * }
 * SimpleSpinnerEditText simpleSpinnerEditText = findViewById(R.id.sset);
 * BaseAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strings);
 * simpleSpinnerEditText.setAdapter(adapter);
 */
public class SpinnerEditText extends AppCompatEditText implements AdapterView.OnItemClickListener,TextWatcher {
    /*popup window to show the selection*/
    private PopupWindow mPopupWindow;
    /*View to list the data item*/
    private ListView mListView;
    private Drawable drawable;
    private Drawable popupBackground;
    private Drawable popupDivider;
    private float popupDividerHeight;
    private BaseAdapter adapter;
    private List<Object> mOptions = new ArrayList<>();
    private List<Object> displayedOptions = new ArrayList<>();

    public SpinnerEditText(Context context) {
        this(context, null);
    }

    public SpinnerEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SpinnerEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }

    private void init(Context context, AttributeSet attrs) {


        if(ObjectUtils.isNotNull(attrs)) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SimpleSpinnerEditText);
            popupBackground = typedArray.getDrawable(R.styleable.SimpleSpinnerEditText_popup_background);
            popupDivider = typedArray.getDrawable(R.styleable.SimpleSpinnerEditText_popup_divider);
            popupDividerHeight = typedArray.getDimension(R.styleable.SimpleSpinnerEditText_popup_divider_height, DensityUtils.dp2px(context, 1));
            typedArray.recycle();
        }
        popupBackground = ObjectUtils.isNull(popupBackground) ? context.getResources().getDrawable(R.drawable.popup_window_bg, null) : popupBackground;
        popupDivider = ObjectUtils.isNull(popupDivider) ? context.getResources().getDrawable(R.drawable.divider_bg, null) : popupDivider;

        setLongClickable(false);
        setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        mListView = new ListView(context);
        mListView.setBackground(popupBackground);
        mListView.setDivider(popupDivider);
        mListView.setDividerHeight((int)popupDividerHeight);
        mListView.setOnItemClickListener(this);
        mPopupWindow = new PopupWindow(mListView, getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(false);


        drawable = getCompoundDrawablesRelative()[2] == null ? getCompoundDrawables()[2] : getCompoundDrawablesRelative()[2];
        if (ObjectUtils.isNull(drawable)) {
            drawable = getContext().getResources().getDrawable(R.drawable.ic_arrow_down_black, null);
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
            public void onDestroyActionMode(ActionMode mode) {}

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
        setDrawableVisibility(false);
    }

    public void setPopupBackground(Drawable popupBackground) {
        this.popupBackground = popupBackground;
        if(ObjectUtils.isNotNull(mListView)){
            mListView.setBackground(popupBackground);
        }
    }

    public void setPopupDivider(Drawable popupDivider) {
        this.popupDivider = popupDivider;
        if(ObjectUtils.isNotNull(mListView)){
            mListView.setDivider(popupDivider);
        }
    }

    public void setPopupDividerHeight(float popupDividerHeight) {
        this.popupDividerHeight = popupDividerHeight;
        if(ObjectUtils.isNotNull(mListView)){
            mListView.setDividerHeight((int)popupDividerHeight);
        }
    }

    public void setSelectDrawable(Drawable drawable){
        this.drawable = drawable;
        super.setCompoundDrawables(getCompoundDrawables()[0],getCompoundDrawables()[1],drawable,getCompoundDrawables()[3]);
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
                    int start = getWidth() - getTotalPaddingEnd() + getPaddingEnd();
                    int end = getWidth();
                    boolean available = (event.getX() > start) && (event.getX() < end);
                    if (available) {
                        closeInputMethod();
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                show(mOptions);
                            }
                        },200);
                        return true;
                    }
                }
        }
        return super.onTouchEvent(event);
    }



    private void show(List<Object> displayedOptions) {
        /**
         * measure the size of {@link ListView},if not ,{@link ListView#getMeasuredHeight()} is zero
         */
        if(ObjectUtils.isNull(displayedOptions) || displayedOptions.size() <= 0){
            return;
        }

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, displayedOptions);
        mListView.setAdapter(adapter);
        mListView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        int measuredHeight = getMeasuredHeight();
//        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
//        int screenHeight = metrics.heightPixels;
        Rect rect = new Rect();
        getWindowVisibleDisplayFrame(rect);
        int screenHeight = rect.bottom;

        int[] location = new int[2];
        getLocationOnScreen(location);
        boolean isUp = (screenHeight - location[1] - measuredHeight <= measuredHeight);


        if (isUp) {
            /**
             * mListView.getMeasuredHeight() is a height of an item of list view
             */
            int totalListHeight = mListView.getMeasuredHeight() * mListView.getCount();
            mPopupWindow.setHeight(0);
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
            mPopupWindow.showAtLocation(SpinnerEditText.this, Gravity.TOP | Gravity.START, location[0], location[1] - mPopupWindow.getHeight());

        } else {
            mPopupWindow.setAnimationStyle(R.style.AnimationDropDown);

            mPopupWindow.showAsDropDown(this, 0, 5);
        }


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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if(ObjectUtils.isNull(mPopupWindow) || ObjectUtils.isNull(getText()))return;
        if(!TextUtils.isEmpty(s)){

            boolean isSame = false;
            displayedOptions.clear();
            for(Object obj:mOptions){
                String objStr = obj.toString().toUpperCase();
                if(objStr.equals(s.toString().toUpperCase())){
                    isSame = true;
                    mPopupWindow.dismiss();
                    break;
                }
                if(objStr.contains(s.toString().toUpperCase())){
                    displayedOptions.add(obj);
                }
            }

            if(!isSame) {
                mPopupWindow.dismiss();
                show(displayedOptions);
            }
        }else {
            mPopupWindow.dismiss();
        }


    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public List<Object> getOptions() {
        return mOptions;
    }

    public void setOptions(List<Object> options) {
        this.mOptions = options;
        this.displayedOptions.addAll(options);
        if(ObjectUtils.isNotNull(options) && options.size() > 0){
            setDrawableVisibility(true);
        }else{
            setDrawableVisibility(false);
        }
    }
}
