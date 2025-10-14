package com.example.ledmatrixcontroller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.welie.blessed.BluetoothPeripheral;
import com.welie.blessed.GattStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class ProjectManager extends AppCompatActivity {
    private static final String TAG = "Project manager";
    private static final String version = "LED_matrix_V_1.0";
    public static boolean wasConnected = false;
    public static boolean wasVersionError = false;
    public static boolean isGettingSettings = false;
    public static Context context;
    private static String gettingSettingsFor;
    private static int indexOfGettingSetting;
    private static final ArrayList<String> backgrounds = new ArrayList<>(Arrays.asList(new String[]{"SingleColor", "Rainbow", "Perlin", "Party"}));
    private static final BluetoothFunc onNotify = new BluetoothFunc() {
        @Override
        public void run(BluetoothPeripheral peripheral, byte[] value, GattStatus status) {
            BluetoothFunc.super.run(peripheral, value, status);
            if (status != GattStatus.SUCCESS){
                Log.e(TAG, "Received data error: " + status);
                return;
            }
            String data = new String(value);
            if (!Arrays.equals(value, new byte[]{13, 10}) && !data.trim().equals(peripheral.getName().trim()) &&
                    !Arrays.equals(value, new byte[]{0, 0})) {
                Log.i(TAG, data.trim() + "\t(" + Arrays.toString(value) + ")");
                if (!wasConnected){
                    if (data.equals(version)){
                        BluetoothManager.send(new byte[]{1});
                        wasConnected = true;
                        isGettingSettings = true;
                    }
                    else {
                        BluetoothManager.send(new byte[]{0});
                        wasVersionError = true;
                        MainActivity.isConnecting = false;
                        BluetoothManager.startScan();
                        Toast.makeText(context, "какая-то хня пошла", Toast.LENGTH_SHORT).show();
                        BluetoothManager.disconnect();
                    }
                }
                if (gettingSettingsFor.isEmpty()) {
                    indexOfGettingSetting = 0;
                    if (data.equals("\tfinish")) {
                        Log.i(TAG, "finish");
                        Intent intent = new Intent(context, MenuActivity.class);
                        context.startActivity(intent);
                    }
                    else if (backgrounds.contains(data) || data.equals("isBackgroundOn") ||
                            data.equals("isGifOn") || data.equals("Text")) gettingSettingsFor = data;
                    else {
                        //todo logic of creating new Gif setting
                    }
                }
                else if (data.equals("\tfinish")) gettingSettingsFor = "";
                else {
                    // todo logic for getting regular settings
                    indexOfGettingSetting++;
                }
            }
        }
    };

    public static boolean initBluetooth(Activity activity, Context context, BluetoothFunc onDeviceFound){
        ProjectManager.context = context;
        return BluetoothManager.init(activity, context, onDeviceFound, onNotify);
    }
}
