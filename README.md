# Gradle
[![](https://jitpack.io/v/zj565061763/passwordview.svg)](https://jitpack.io/#zj565061763/passwordview)

# Demo
![](https://raw.githubusercontent.com/zj565061763/passwordview/master/screenshot/demo.png)

```xml
<com.sd.lib.passwordview.FPasswordView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:background="@drawable/bg_white_stroke_black_corner"
    app:pvItemCount="6"
    app:pvItemDivider="@drawable/divider_black_1dp" />
```
```xml
<com.sd.lib.passwordview.FPasswordView
    android:layout_width="160dp"
    android:layout_height="wrap_content"
    app:pvItemBackground="@drawable/bg_white_stroke_black_corner"
    app:pvItemMargin="10dp"
    app:pvItemSquare="true" />
```
```xml
<com.sd.lib.passwordview.FPasswordView
    android:layout_width="wrap_content"
    android:layout_height="35dp"
    app:pvItemBackground="@drawable/bg_underline"
    app:pvItemMargin="10dp"
    app:pvItemWidth="50dp"
    app:pvPasswordPlaceholder="" />
```

# 支持的xml属性
```xml
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

    <!-- item之间的间距 -->
    <attr name="pvItemMargin" format="dimension" />

    <!-- item之间的分割线 -->
    <attr name="pvItemDivider" format="reference" />

    <!-- item是否以正方形显示 -->
    <attr name="pvItemSquare" format="boolean" />

    <!-- 密码占位符，如果为空，则显示明文 -->
    <attr name="pvPasswordPlaceholder" format="string" />

</declare-styleable>
```