package com.emreeran.ehorizontalpicker;

import android.view.View;
import android.view.ViewGroup;

/**
 * PickerView adapter interface
 * Created by Emre Eran on 22/12/15.
 */
interface PickerAdapter {
    View onCreateView(ViewGroup parent);
    void onBindView(View view, int position);
    void onViewClicked(View view, int position);
}
