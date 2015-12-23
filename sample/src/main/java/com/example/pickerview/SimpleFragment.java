package com.example.pickerview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emreeran.pickerview.PickerView;

/**
 * Fragment class demonstrating a simple usage of PickerView
 * Created by Emre Eran on 23/12/15.
 */
public class SimpleFragment extends Fragment {

    ViewPager mViewPager;

    public static SimpleFragment newInstance(Context context) {
        return (SimpleFragment) instantiate(context, SimpleFragment.class.getName());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_simple, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final PickerView pickerView = (PickerView) view.findViewById(R.id.horizontal_picker);
        PickerAdapter pickerAdapter = new PickerAdapter();
        pickerView.setAdapter(pickerAdapter);

        View indicator = view.findViewById(R.id.indicator);
        pickerView.setIndicator(indicator, true);

        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager(), getContext());
        mViewPager.setAdapter(pagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pickerView.scrollToPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class PickerAdapter extends PickerView.Adapter {
        @Override
        public View onCreateView(ViewGroup parent) {
            return LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_horizontal_picker_item, parent, false);
        }

        @Override
        public void onBindView(View view, int position) {
            TextView textView = (TextView) view.findViewById(R.id.picker_item_text_view);
            String text = "Item " + (position + 1);
            textView.setText(text);
        }

        @Override
        public int getItemCount() {
            return 10;
        }

        @Override
        public void onViewClicked(View view, int position) {
            mViewPager.setCurrentItem(position, true);
        }
    }
}
