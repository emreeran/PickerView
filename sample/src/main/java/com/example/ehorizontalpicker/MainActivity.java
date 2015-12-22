package com.example.ehorizontalpicker;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, HorizontalFragment.newInstance(this)).commit();

        Button horizontalButton = (Button) findViewById(R.id.button_horizontal);
        Button verticalButton = (Button) findViewById(R.id.button_vertical);
        horizontalButton.setOnClickListener(this);
        verticalButton.setOnClickListener(this);
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
        }
    }
}
