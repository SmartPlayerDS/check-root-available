package org.smartplayer.checkrootavailable;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();
    private TextView textViewInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewInfo = (TextView) findViewById(R.id.textInfo);
        this.checkAvailableRoot();
    }

    private void checkAvailableRoot() {
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
            String finalMsg = "Root available for applications";
            textViewInfo.setText(finalMsg);
            textViewInfo.setTextColor(Color.GREEN);
            textViewInfo.setTextSize(42f);
        });
    }

    private void showErrorRoot(String errorMsg) {
        runOnUiThread(() -> {
            String finalMsg = "Root unavailable for applications \n" + "errorMsg: " + errorMsg;
            textViewInfo.setText(finalMsg);
            textViewInfo.setTextColor(Color.RED);
            textViewInfo.setTextSize(14f);
        });
    }
}