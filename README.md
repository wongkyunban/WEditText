# WEditText

# [![Release](https://jitpack.io/v/wongkyunban/WEditText.svg)](https://jitpack.io/#wongkyunban/WEditText)

ClearEditText SimpleSpinnerEditText SpinnerEditText lib

# Usage
__Step 1.__ Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:
```java
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```
__Step 2.__ Add the dependency
```java
dependencies {
  implementation 'com.github.wongkyunban:WEditText:v1.1.0'
}
```
if your API is less than 19，suggest that use following dependency:
```java
dependencies {
  implementation 'com.github.wongkyunban:WEditText_Legacy:v1.0.2'
}
```
You alse can refer to 
![WEditText_Legacy](https://github.com/wongkyunban/WEditText_Legacy)

# Detail
## ClearEditText

![](snapshot/snapshot1.png)

|API|Desc|
|--|--|
|setClearDrawable(Drawable)|set clear button image as same as setCompoundDrawables()|

We also can set drawable through attribute `android:drawableEnd` or `android:drawableRight` in xml layout.

## SimpleSpinnerEditText

![](snapshot/snapshot2.png)

|API|Desc|
|--|--|
|setPopupBackground(Drawable)|PopupWindow background|
|setPopupDivider(Drawable)|divider between items|
|setPopupDividerHeight(int)|set height of divider|
|setSelectDrawable(Drawable)|set drawable of button to select item|
|setItemTextColor(int)|set the text color of the popup items|
|setItemTextSize(float)|set the popup item text size|
|setOptions(List<T>)|set options|

We also can set drawable through attribute `android:drawableEnd` or `android:drawableRight` in xml layout.

### demo:
```java
        SimpleSpinnerEditText simpleSpinnerEditText = (SimpleSpinnerEditText)findViewById(R.id.sset);

        List<Bean> strings = new ArrayList<Bean>();
        for (int i = 0; i < 50; i++) {
            Bean bean = new Bean("Tom"+i,"NO."+i);
            strings.add(bean);
        }
        simpleSpinnerEditText.setOptions(strings);
        simpleSpinnerEditText.setItemTextColor(Color.BLUE);
        simpleSpinnerEditText.setItemTextSize(DensityUtils.sp2px(this,5));
```
## SpinnerEditText

SpinnerEditText is a little from SimpleSpinnerEditText.They both provide editting and selecting options.SpinnerEditText can show items which containe the characters that you are typing.

![](snapshot/snapshot3.png)


|API|Desc|
|--|--|
|setPopupBackground(Drawable)|PopupWindow background|
|setPopupDivider(Drawable)|divider between items|
|setPopupDividerHeight(int)|set height of divider|
|setSelectDrawable(Drawable)|set drawable of button to select item|
|setItemTextColor(int)|set the text color of the popup items|
|setItemTextSize(float)|set the popup item text size|
|setHintCount(int) |set the max count of hint itmes while you are typing|
|setOptions(List<T>)|set options|

We also can set drawable through attribute `android:drawableEnd` or `android:drawableRight` in xml layout.

### demo:
```java
        SpinnerEditText spinnerEditText = (SpinnerEditText)findViewById(R.id.set_select_input);
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 50; i++) {
            list.add("NNo." + i + "号");
        }
        spinnerEditText.setOptions(list);
        spinnerEditText.setItemTextColor(0xff00ff00);
        spinnerEditText.setItemTextSize(DensityUtils.sp2px(this,10));
```
# Note
One thining we should know is that we need to override the toString() method in our object instace.Because the item is provided by method toString of instance.

# Thank you!

