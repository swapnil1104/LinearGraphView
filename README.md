# LinearGraphView

[![](https://jitpack.io/v/swapnil1104/LinearGraphView.svg)](https://jitpack.io/#swapnil1104/LinearGraphView)

A custom view to display information in a Linear Graph with smooth animations! 

![Demo with underline](images/demo.gif)

## How to integrate the library in your app?
Step 1: Add it in your root build.gradle at the end of repositories:

```
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```
Step 2. Add the dependency

```
dependencies {
    implementation 'com.github.swapnil1104:LinearGraphView:{current_lib_ver}'
}
```
Step 3. Add OtpEditText to your layout file

```
<com.broooapps.lineargraphview2.LinearGraphView
    android:id="@+id/linear_graph_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

Step 4. Refer this View in your activity file, create a List<DataModel> and populate it with your values, use this list and invoke `setData(List<DataModel> dataModel, int range)`.

The second param is the entire span graph view, and `value` param in each DataModel object will occupy a percent length.
Width of each item is calculated by `model.value / range * viewWidth`.
```
LinearGraphView lgv = findViewById(R.id.linear_graph_view);

List<DataModel> dataList = new ArrayList<>();

dm.add(new DataModel("One", "#00ffff", 100));
dm.add(new DataModel("Two", "#74EEA1", 250));
dm.add(new DataModel("Three", "#f2002f", 100));
dm.add(new DataModel("four", "#B61CB3", 180));

lgv.setData(dm, 999);

```
The above code will result in this:

![Demo with underline](images/demo2.gif)

## How to customize the view.
### Setting custom border color

To use a custom color for the border of LinearGraphView, use

```app:lgv_border_color="@color/colorAccent"```

### Setting border animation duration
To change the animation duration of the initial border transition, use

```app:lgv_border_anim_duration="{TIME_IN_MS}"```