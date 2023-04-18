package org.smartplayer.checkrootavailable;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class OrientationTestActivity extends AppCompatActivity {
    private final static String TAG = OrientationTestActivity.class.getSimpleName();
    private TextView logSetRotation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orientation_test);
        logSetRotation = findViewById(R.id.logSetRotation);
    }

    public void onRotate0(View v) {
        logSetRotation.setText("");
        Toast.makeText(this, "Clicked onRotate0", Toast.LENGTH_LONG).show();
        executeCommands("settings put system accelerometer_rotation 0");
        executeCommands("settings put system user_rotation 0");
        executeCommands("setprop ro.sf.hwrotation 0");
        executeCommands("setprop persist.sys.app.rotation original");

    }

    public void onRotate90(View v) {
        logSetRotation.setText("");
        Toast.makeText(this, "Clicked onRotate90", Toast.LENGTH_LONG).show();
        executeCommands("settings put system accelerometer_rotation 0");
        executeCommands("settings put system user_rotation 90");
        executeCommands("setprop ro.sf.hwrotation 90");
        executeCommands("setprop persist.sys.app.rotation original");

    }

    public void onRotate180(View v) {
        logSetRotation.setText("");
        Toast.makeText(this, "Clicked onRotate180", Toast.LENGTH_LONG).show();
        executeCommands("settings put system accelerometer_rotation 0");
        executeCommands("settings put system user_rotation 180");
        executeCommands("setprop ro.sf.hwrotation 180");
        executeCommands("setprop persist.sys.app.rotation original");
    }

    public void onRotate270(View v) {
        logSetRotation.setText("");
        Toast.makeText(this, "Clicked onRotate1270", Toast.LENGTH_LONG).show();
        executeCommands("settings put system accelerometer_rotation 0");
        executeCommands("settings put system user_rotation 270");
        executeCommands("setprop ro.sf.hwrotation 270");
        executeCommands("setprop persist.sys.app.rotation original");
    }

    private void executeCommands(String command) {
        try {
            logSetRotation.append(command + "\n");
            Process sh = Runtime.getRuntime().exec("su", null, null);
            OutputStream os = sh.getOutputStream();
            os.write((command).getBytes("ASCII"));
            os.flush();
            os.close();
            BufferedReader errorStream = new BufferedReader(new InputStreamReader(sh.getErrorStream(), "UTF-8"));
            String data;
            while ((data = errorStream.readLine()) != null) {
                Log.d(TAG, "Error sh: " + data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
