PickerView
=====


**PickerView** is an easy way to create selection menus.

Usage
-----

###Including to your project

If using Gradle add jcenter to repositories

        repositories {
            jcenter()
        }

Add to your module dependencies
        
        dependencies {
            compile 'com.emreeran.pickerview:pickerview:1.0.0'
        }
        
###Adding the **PickerView**

Just use the view in your layout and set an orientation
Add the following line to the following layout ``` xmlns:app="http://schemas.android.com/apk/res-auto" ``` then use the view
like the following:
       
        <com.emreeran.pickerview.PickerView
                android:layout_width="match_parent"
                android:layout_height="50dp"
                app:orientation="horizontal"/>
                
####Additional attributes
* Fit a number of items per screen ```app:item_per_screen="ITEM_COUNT"```

Resizes items so that the specified number of them fit the total view length or height depending on the orientation

* Divider

        app:divider_color="#55000000"
        app:divider_size="2dp"
        
Adds dividers between items with given color and width or height depending on the orientation

* Adding an indicator view

A view can be added (for example an arrow) and set as an indicator. It will follow the selected items. Best way to use this is; 
in a wrapping RelativeLayout use ```layout_below```, ```layout_above```, ```layout_toRightOf``` or ```layout_toLeftOf``` attributes
on determining the relationship between the indicator view and **PickerView**.

Then assign by ```pickerView.setIndicator(indicator);``` or if you want a bounce animation use ```pickerView.setIndicator(indicator, true);```

###Setting the adapter

Extend ```PickerView.Adapter``` to create your adapter class. 

Set your item layouts in ```onCreateView``` and manipulate them in ```onBindView``` functions.

Implement ```onViewSelected``` function to manipulate menu item views on selection.

Implement ```onPositionSelected``` function to add functionality to selected positions.

Set the adapter to the view.

        PickerView pickerView = (PickerView) view.findViewById(R.id.horizontal_picker);
        PickerAdapter pickerAdapter = new PickerAdapter();
        pickerView.setAdapter(pickerAdapter);

###Extras

Call ```pickerView.scrollToPosition``` to go to an item, the function can also get a ```PickerView.OnScrolledToViewListener``` parameter 
to manipulate the target view on scroll.

Sample usages are demonstrated in the sample project