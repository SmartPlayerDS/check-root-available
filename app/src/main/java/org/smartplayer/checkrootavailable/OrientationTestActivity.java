package org.smartplayer.checkrootavailable;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class OrientationTestActivity  extends AppCompatActivity {
    private final static String TAG = OrientationTestActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orientation_test);
    }
}
