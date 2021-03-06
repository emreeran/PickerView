package com.emreeran.pickerview;

import android.view.View;
import android.view.ViewGroup;

/**
 * PickerView adapter interface
 * Created by Emre Eran on 22/12/15.
 */
interface PickerAdapter {
    View onCreateView(ViewGroup parent);
    void onBindView(View view, int position);
    void onSelectView(View view);
    void onSelectPosition(int position);
}
