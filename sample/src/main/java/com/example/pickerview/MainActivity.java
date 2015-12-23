package com.example.pickerview;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, SimpleFragment.newInstance(this)).commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_horizontal:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, HorizontalFragment.newInstance(this)).commit();
                break;
            case R.id.button_vertical:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, VerticalFragment.newInstance(this)).commit();
                break;
            case R.id.button_simple:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, SimpleFragment.newInstance(this)).commit();
                break;
        }
    }
}
