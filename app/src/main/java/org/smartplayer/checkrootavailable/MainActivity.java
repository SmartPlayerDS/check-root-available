package org.smartplayer.checkrootavailable;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();
    private TextView textViewInfo;
    /**
     * The set of SU location
     */
    public static final String[] SU_BINARY_DIRS = {
            "/system/bin", "/system/sbin", "/system/xbin",
            "/vendor/bin", "/sbin"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewInfo = findViewById(R.id.textInfo);
        this.checkAvailableRoot(this.hasSUFileOnFirmware());
    }

    /**
     * Check if the device is rooted
     * <p>
     * This function check if the system has SU file. Note that though SU file exits, it might not
     * work.
     * </p>
     *
     * @return this device is rooted or not.
     */
    public boolean hasSUFileOnFirmware() {
        boolean hasRootedSuFile = false;
        for (String path : MainActivity.SU_BINARY_DIRS) {
            File su = new File(path + "/su");
            if (su.exists()) {
                hasRootedSuFile = true;
                break;
            }
        }
        return hasRootedSuFile;
    }

    private void checkAvailableRoot(boolean hasSUFileOnFirmware) {
        if (hasSUFileOnFirmware) {
            try {
                Process proc = Runtime.getRuntime().exec("su");
                OutputStreamWriter outputStream = new OutputStreamWriter(proc.getOutputStream(), "UTF-8");
                BufferedReader inputStream = new BufferedReader(new InputStreamReader(proc.getInputStream(), "UTF-8"));
                this.run(outputStream, inputStream);
            } catch (IOException e) {
                e.printStackTrace();
                String stackTrace = Log.getStackTraceString(e);
                this.showErrorRoot(stackTrace);
            }
        } else {
            this.showErrorRoot("SU file doesn't exest");
        }
    }

    private void run(OutputStreamWriter outputStream, BufferedReader inputStream) {
        try {
            outputStream.write("echo Started\n");
            outputStream.flush();
            while (true) {
                String line = inputStream.readLine();
                if (line == null) {
                    throw new EOFException();
                }
                if ("".equals(line)) {
                    continue;
                }
                if ("Started".equals(line)) {
                    //todo root
                    Log.d(TAG, "root success");
                    this.showAvailableRoot();
                    break;
                }
                Log.e(TAG, "unknown error occurred.");
                this.showErrorRoot("unknown error occurred.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            String stackTrace = Log.getStackTraceString(e);
            this.showErrorRoot(stackTrace);
        }
    }

    private void showAvailableRoot() {
        runOnUiThread(() -> {
            String finalMsg = "Root shell available for applications";
            textViewInfo.setText(finalMsg);
            textViewInfo.setTextColor(Color.GREEN);
            textViewInfo.setTextSize(42f);
        });
    }

    private void showErrorRoot(String errorMsg) {
        runOnUiThread(() -> {
            String finalMsg = "Root shell unavailable for applications \n" + "errorMsg: " + errorMsg;
            textViewInfo.setText(finalMsg);
            textViewInfo.setTextColor(Color.RED);
            textViewInfo.setTextSize(14f);
        });
    }
}