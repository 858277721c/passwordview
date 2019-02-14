# Gradle
[![](https://jitpack.io/v/zj565061763/passwordview.svg)](https://jitpack.io/#zj565061763/passwordview)

# Demo
![](https://raw.githubusercontent.com/zj565061763/passwordview/master/screenshot/demo.png)

```xml
<com.sd.lib.passwordview.FPasswordView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:background="@drawable/bg_white_stroke_black_corner"
    app:pvItemDivider="@drawable/divider_black_1dp" />
```
```xml
<com.sd.lib.passwordview.FPasswordView
    android:layout_width="160dp"
    android:layout_height="wrap_content"
    app:pvItemBackground="@drawable/bg_white_stroke_black_corner"
    app:pvItemCount="4"
    app:pvItemMargin="10dp"
    app:pvItemSquare="true" />
```
```xml
<com.sd.lib.passwordview.FPasswordView
    android:layout_width="wrap_content"
    android:layout_height="35dp"
    app:pvInputType="number"
    app:pvItemBackground="@drawable/bg_underline_gray"
    app:pvItemBackgroundFill="@drawable/bg_underline_black"
    app:pvItemMargin="5dp"
    app:pvItemWidth="40dp"
    app:pvPasswordPlaceholder="" />
```

```java
/**
 * 设置只允许输入数字
 */
mPasswordView.setInputType(InputType.TYPE_CLASS_NUMBER);
/**
 * 设置输入内容变化回调
 */
mPasswordView.setCallback(new FPasswordView.Callback()
{
    @Override
    public void onTextChanged(String text)
    {
        Log.i(TAG, "onTextChanged:" + text);

        if (text.length() == mPasswordView.getItemCount())
            Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }
});
```

# 支持的xml属性
```xml
<resources>

    <declare-styleable name="LibPasswordView">

        <!-- item的数量 -->
        <attr name="pvItemCount" format="integer" />

        <!-- item的文字颜色 -->
        <attr name="pvItemTextColor" format="color" />

        <!-- item的文字大小 -->
        <attr name="pvItemTextSize" format="dimension" />

        <!-- item的宽度，如果为0，则平分总宽度 -->
        <attr name="pvItemWidth" format="dimension" />

        <!-- item的背景 -->
        <attr name="pvItemBackground" format="reference" />

        <!-- item的背景（item的内容已经被填充） -->
        <attr name="pvItemBackgroundFill" format="reference" />

        <!-- item之间的间距 -->
        <attr name="pvItemMargin" format="dimension" />

        <!-- item之间的分割线 -->
        <attr name="pvItemDivider" format="reference" />

        <!-- item是否以正方形显示 -->
        <attr name="pvItemSquare" format="boolean" />

        <!-- 密码占位符，如果为空，则显示明文 -->
        <attr name="pvPasswordPlaceholder" format="string" />

    </declare-styleable>

</resources>
```

# 支持覆盖的默认配置
```xml
<resources>

    <!-- 默认item个数 -->
    <integer name="lib_passwordview_item_count">6</integer>

    <!-- 默认密码占位符 -->
    <string name="lib_passwordview_password_placeholder">●</string>

    <!-- 默认item文字颜色 -->
    <color name="lib_passwordview_text_item">#000000</color>

    <!-- 默认item文字大小 -->
    <dimen name="lib_passwordview_text_item">15sp</dimen>

</resources>
```