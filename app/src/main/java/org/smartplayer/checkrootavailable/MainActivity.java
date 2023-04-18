package org.smartplayer.checkrootavailable;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
        Log.d(TAG, "ProcessMyUID: " + android.os.Process.myUid());
        textViewInfo = findViewById(R.id.textInfo);
        this.checkAvailableRoot(this.hasSUFileOnFirmware());
        this.takeScreenWithRoot();
        this.takeScreenWithoutRootBySignAppFirmwareKey();
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
                Log.d(TAG, "#run. After read: " + line);
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

    private void takeScreenWithRoot() {
        try {
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US);
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
            String myPath = new File(path, "sp_check_root_" + dateFormat.format(date) + ".png").getAbsolutePath();
            Process sh = Runtime.getRuntime().exec("su", null, null);
            OutputStream os = sh.getOutputStream();
            os.write(("screencap " + myPath).getBytes("ASCII"));
            os.flush();
            os.close();
            BufferedReader errorStream = new BufferedReader(new InputStreamReader(sh.getErrorStream(), "UTF-8"));
            String data;
            while ((data = errorStream.readLine()) != null) {
                Log.d(TAG, "Error sh: " + data);
            }
            Log.d(TAG, "TakeScreen ROOT finish, myPath: " + myPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void takeScreenWithoutRootBySignAppFirmwareKey() {
        try {
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US);
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
            String myPath = new File(path, "sp_check_without_" + dateFormat.format(date) + ".png").getAbsolutePath();
            Process process;
            process = Runtime.getRuntime().exec("screencap -p " + myPath);
            process.waitFor();
            Log.d(TAG, "TakeScreen WITHOUT ROOT finish, myPath: " + myPath);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onOpenOrientationTest(View v) {
        Toast.makeText(this, "onOpenOrientationTest clicked", Toast.LENGTH_LONG).show();
    }
}