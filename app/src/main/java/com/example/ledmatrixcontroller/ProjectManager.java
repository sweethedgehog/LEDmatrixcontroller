package com.example.ledmatrixcontroller;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.welie.blessed.BluetoothPeripheral;
import com.welie.blessed.GattStatus;

public class ProjectManager extends AppCompatActivity {
    private static final String TAG = "Project manager";
    private static final String version = "1.0";
    public static boolean wasConnected = false;
    public static boolean wasVersionError = false;
    public static boolean isGettingSettings = false;
    public static Context context;
    private static final BluetoothFunc onNotify = new BluetoothFunc() {
        @Override
        public void run(BluetoothPeripheral peripheral, byte[] value, GattStatus status) {
            BluetoothFunc.super.run(peripheral, value, status);

        }
    };

    public static boolean initBluetooth(Activity activity, Context context, BluetoothFunc onDeviceFound){
        ProjectManager.context = context;
        return BluetoothManager.init(activity, context, onDeviceFound, onNotify);
    }
}
