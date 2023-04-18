package org.smartplayer.checkrootavailable;

import android.os.Bundle;
import android.os.Handler;
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
        boolean isUgoos = true;
        executeCommands("settings put system accelerometer_rotation 0");
        executeCommands("setprop media.omx.display_mode 1");
        executeCommands("setprop persist.sys.app.rotation original");
        logSetRotation.append("Will reboot after 10 seconds");
        new Handler().postDelayed(() -> executeCommands("reboot"), 10000);
    }

    public void onRotate90(View v) {
        logSetRotation.setText("");
        executeCommands("settings put system accelerometer_rotation 0");
        executeCommands("setprop media.omx.display_mode 0");
        executeCommands("setprop persist.sys.app.rotation force_port");
        logSetRotation.append("Will reboot after 10 seconds");
        new Handler().postDelayed(() -> executeCommands("reboot"), 10000);
        Toast.makeText(this, "Clicked onRotate90", Toast.LENGTH_LONG).show();
    }

    public void onRotate180(View v) {
        logSetRotation.setText("");
        Toast.makeText(this, "Clicked onRotate1270", Toast.LENGTH_LONG).show();
        executeCommands("settings put system accelerometer_rotation 0");
        executeCommands("setprop media.omx.display_mode 0");
        executeCommands("setprop persist.sys.app.rotation force_land_r");
        logSetRotation.append("Will reboot after 10 seconds");
        new Handler().postDelayed(() -> executeCommands("reboot"), 10000);
    }

    public void onRotate270(View v) {
        logSetRotation.setText("");
        Toast.makeText(this, "Clicked onRotate180", Toast.LENGTH_LONG).show();
        executeCommands("settings put system accelerometer_rotation 0");
        executeCommands("setprop media.omx.display_mode 0");
        executeCommands("setprop persist.sys.app.rotation original");
        logSetRotation.append("Will reboot after 10 seconds");
        new Handler().postDelayed(() -> executeCommands("reboot"), 10000);
    }

    private void executeCommands(String command) {
        try {
            logSetRotation.append("Command:" + command + "\n");
            Process sh = Runtime.getRuntime().exec("su", null, null);
            OutputStream os = sh.getOutputStream();
            os.write((command).getBytes("ASCII"));
            os.flush();
            os.close();
            BufferedReader errorStream = new BufferedReader(new InputStreamReader(sh.getErrorStream(), "UTF-8"));
            String data;
            while ((data = errorStream.readLine()) != null) {
                String errorMsg = "Error sh: " + data;
                Log.e(TAG, errorMsg);
                logSetRotation.append(errorMsg + "\n");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
