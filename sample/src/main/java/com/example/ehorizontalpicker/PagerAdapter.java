package com.example.ehorizontalpicker;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Simple pager adapter
 * Created by Emre Eran on 22/12/15.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext;

    public PagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return PagerFragment.newInstance(mContext, position);
    }

    @Override
    public int getCount() {
        return 10;
    }

    public static class PagerFragment extends Fragment {
        public static PagerFragment newInstance(Context context, int position) {
            Bundle args = new Bundle();
            args.putInt("pos", position);
            return (PagerFragment) instantiate(context, PagerFragment.class.getName(), args);
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            int position = getArguments().getInt("pos");
            View view = inflater.inflate(R.layout.fragment_pager, container, false);
            TextView textView = (TextView) view.findViewById(R.id.fragment_pager_text_view);
            String text = "Page " + (position + 1);
            textView.setText(text);
            return view;
        }
    }
}
