package com.example.ledmatrixcontroller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.welie.blessed.BluetoothPeripheral;
import com.welie.blessed.GattStatus;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ProjectManager extends AppCompatActivity {
    private static final String TAG = "Project manager";
    private static final String version = "LED_matrix_V_1.0";
    public static boolean wasConnected = false;
    public static boolean wasVersionError = false;
    public static boolean isGettingSettings = false;
    public static Context context;
    private static String gettingSettingsFor = "";
    private static int indexOfGettingSetting = 0;
    public static final ArrayList<String> backgrounds = new ArrayList<>(Arrays.asList(new String[]{"SingleColor", "Rainbow", "PerlinBackground", "Party"}));
    public static HashMap<String, HashMap<String, int[]>> gifs = new HashMap<>();
    public static HashMap<String, String> currProfilesGifs = new HashMap<>();
    public static HashMap<String, ArrayList<String>> gifsProfiles = new HashMap<>();
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
                else if (gettingSettingsFor.isEmpty()) {
                    indexOfGettingSetting = 0;
                    if (data.equals("\tfinish")) {
                        Log.i(TAG, "finish");
                        MenuActivity.name = peripheral.getName();
                        Intent intent = new Intent(context, MenuActivity.class);
                        context.startActivity(intent);
                    }
                    else if (backgrounds.contains(data) || data.equals("isBackgroundOn") ||
                            data.equals("isGifOn") || data.equals("RunningText")) gettingSettingsFor = data;
                    else {
                        MenuActivity.anims.add(data);
                        gifs.put(data, new HashMap<>());
                        gifsProfiles.put(data, new ArrayList<>());
                        gettingSettingsFor = data;
                    }
                }
                else if (data.equals("\tfinish")) gettingSettingsFor = "";
                else {
                    if (gettingSettingsFor.equals("isGifOn")) MenuActivity.isGifOn = data.getBytes()[0] != 0;
                    else if (gettingSettingsFor.equals("isBackgroundOn")) MenuActivity.isBackgroundOn = data.getBytes()[0] != 0;
                    else if (gettingSettingsFor.equals(backgrounds.get(0))) SingleColorBackgroundActivity.setSettings(indexOfGettingSetting, value);
                    else if (gettingSettingsFor.equals(backgrounds.get(1))) RainbowBackgroundActivity.setSettings(indexOfGettingSetting, value);
                    else if (gettingSettingsFor.equals(backgrounds.get(2))) PerlinBackgroundActivity.setSettings(indexOfGettingSetting, value);
                    else if (gettingSettingsFor.equals(backgrounds.get(3))) PartyBackgroundActivity.setSettings(indexOfGettingSetting, value);
                    else if (gettingSettingsFor.equals("RunningText")) RunningTextActivity.setSettings(indexOfGettingSetting, value);
                    else {
                        if (indexOfGettingSetting == 0) currProfilesGifs.put(gettingSettingsFor, new String(value));
                        else if (indexOfGettingSetting % 2 == 1) gifsProfiles.get(gettingSettingsFor).add(new String(value));
                        else Objects.requireNonNull(gifs.get(gettingSettingsFor)).
                                    put(Objects.requireNonNull(gifsProfiles.get(gettingSettingsFor)).
                                            get(Objects.requireNonNull(gifsProfiles.
                                                    get(gettingSettingsFor)).size() - 1), strToIntArr(value));
                    }
                    // todo logic for getting regular settings
                    indexOfGettingSetting++;
                }
            }
        }
    };
    public static void showPopupMenu(Context context, View anchorView, List<String> items, AdapterView.OnItemClickListener listener) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_menu, null);
        ListView listView = popupView.findViewById(R.id.popup_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
        PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.colorBackground, typedValue, true);
        int backgroundColor = typedValue.data;

        popupView.setBackgroundColor(backgroundColor);
        popupWindow.showAsDropDown(anchorView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listener.onItemClick(adapterView, view, i, l);
                popupWindow.dismiss();
            }
        });
    }
    public static void showInputDialog(Context context, String title, BluetoothFunc func) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_input, null);
        EditText inputField = dialogView.findViewById(R.id.inputField);
        Button submitButton = dialogView.findViewById(R.id.submitButton);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        inputField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                submitButton.setEnabled(!s.toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = inputField.getText().toString().trim();
                if (!input.isEmpty()) {
                    dialog.dismiss();
                    func.run(input);
                }
            }
        });

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(inputField, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        dialog.show();
    }
    public static boolean initBluetooth(Activity activity, Context context, BluetoothFunc onDeviceFound){
        ProjectManager.context = context;
        return BluetoothManager.init(activity, context, onDeviceFound, onNotify);
    }
    public static void hideKeyboard(Context context, View view){
        view.clearFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public static int[] strToIntArr(byte[] v){
        String a = new String(v);
        int[] buf = new int[a.length()];
        for (int i = 0; i < a.length(); i++) buf[i] = (256 + v[i]) % 256;
        return buf;
    }
}
