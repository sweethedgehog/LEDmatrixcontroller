package com.example.ledmatrixcontroller;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class RainbowBackgroundActivity extends AppCompatActivity {
    private static HashMap<String, int[]> profiles = new HashMap<>();
    public static ArrayList<String> profilesNames = new ArrayList<>();
    public static String currProfile = "default";
    private static boolean isSettingProfile = false;

    private static EditText micWindowView, micTimeoutView, pulseMinView, speedView, brightnessView,
            maxFlashVew, reduceFlashView, maxBrightnessJumpView, reduceBrightnessJumpView, runJumpView;
    private static Switch allowMicView;
    private static TextView profileView;
    private static ImageButton profilesButton, menuButton, deleteButton;

    private static EditText baseStepView, saturationView, xStepView, yStepView;
    private static Switch directionView, xDirectionView, yDirectionView, xMirrorView, yMirrorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rainbow_background);

        if (!profilesNames.get(profilesNames.size() - 1).equals("Добавить")) profilesNames.add("Добавить");
        profileView = findViewById(R.id.rainbow_profile_text_view);
        menuButton = findViewById(R.id.rainbow_back_to_menu_button);
        profilesButton = findViewById(R.id.rainbow_profile_select_button);
        deleteButton = findViewById(R.id.rainbow_delete_button);
        micWindowView = findViewById(R.id.rainbow_mic_window_plain_text);
        micTimeoutView = findViewById(R.id.rainbow_mic_timeout_plain_text);
        pulseMinView = findViewById(R.id.rainbow_mic_pulse_plain_text);
        speedView = findViewById(R.id.rainbow_speed_plain_text);
        brightnessView = findViewById(R.id.rainbow_brightness_plain_text);
        runJumpView = findViewById(R.id.rainbow_run_jump_plain_text);
        maxFlashVew = findViewById(R.id.rainbow_max_sat_flash_plain_text);
        reduceFlashView = findViewById(R.id.rainbow_sat_flash_reduce_plain_text);
        maxBrightnessJumpView = findViewById(R.id.rainbow_max_br_flash_plain_text);
        reduceBrightnessJumpView = findViewById(R.id.rainbow_br_flash_reduce_plain_text);
        allowMicView = findViewById(R.id.rainbow_allow_mic_switch);
////////////////////////////////////////////////////////////////////////////////////////////////////////
        baseStepView = findViewById(R.id.rainbow_base_step_plain_text);
        saturationView = findViewById(R.id.rainbow_saturation_plain_text);
        xStepView = findViewById(R.id.rainbow_x_step_plain_text);
        yStepView = findViewById(R.id.rainbow_y_step_plain_text);
        directionView = findViewById(R.id.rainbow_direction_switch);
        xDirectionView = findViewById(R.id.rainbow_direction_x_switch);
        yDirectionView = findViewById(R.id.rainbow_direction_y_switch);
        xMirrorView = findViewById(R.id.rainbow_mirror_x_switch);
        yMirrorView = findViewById(R.id.rainbow_mirror_y_switch);

        updateAll();
        listenerInit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BluetoothManager.disconnect();
        ProjectManager.wasConnected = false;
        clearAll();
    }

    public static void setSettings(int i, byte[] settings) {
        if (i == 0) currProfile = new String(settings);
        else if (i % 2 == 1) profilesNames.add(new String(settings));
        else profiles.put(profilesNames.get(profilesNames.size() - 1), ProjectManager.strToIntArr(settings));
    }

    public static void clearAll(){
        profiles.clear();
        profilesNames.clear();
    }

    private void updateAll(){
        profileView.setText(currProfile);
        micWindowView.setText(String.valueOf(Objects.requireNonNull(profiles.get(currProfile))[0]));
        micTimeoutView.setText(String.valueOf(Objects.requireNonNull(profiles.get(currProfile))[1]));
        pulseMinView.setText(String.valueOf(Objects.requireNonNull(profiles.get(currProfile))[2]));
        speedView.setText(String.valueOf(Objects.requireNonNull(profiles.get(currProfile))[3]));
        brightnessView.setText(String.valueOf(Objects.requireNonNull(profiles.get(currProfile))[4]));
        allowMicView.setChecked(Objects.requireNonNull(profiles.get(currProfile))[5] != 0);
        maxFlashVew.setText(String.valueOf(Objects.requireNonNull(profiles.get(currProfile))[6]));
        reduceFlashView.setText(String.valueOf(Objects.requireNonNull(profiles.get(currProfile))[7]));
        maxBrightnessJumpView.setText(String.valueOf(Objects.requireNonNull(profiles.get(currProfile))[8]));
        reduceBrightnessJumpView.setText(String.valueOf(Objects.requireNonNull(profiles.get(currProfile))[9]));
        runJumpView.setText(String.valueOf(Objects.requireNonNull(profiles.get(currProfile))[10]));
        if (currProfile.equals("default")) deleteButton.setVisibility(View.GONE);
        else deleteButton.setVisibility(View.VISIBLE);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        baseStepView.setText(String.valueOf(Objects.requireNonNull(profiles.get(currProfile))[11]));
        xStepView.setText(String.valueOf(Objects.requireNonNull(profiles.get(currProfile))[12]));
        yStepView.setText(String.valueOf(Objects.requireNonNull(profiles.get(currProfile))[13]));
        saturationView.setText(String.valueOf(Objects.requireNonNull(profiles.get(currProfile))[14]));
        directionView.setChecked(Objects.requireNonNull(profiles.get(currProfile))[15] != 0);
        xDirectionView.setChecked(Objects.requireNonNull(profiles.get(currProfile))[16] != 0);
        yDirectionView.setChecked(Objects.requireNonNull(profiles.get(currProfile))[17] != 0);
        xMirrorView.setChecked(Objects.requireNonNull(profiles.get(currProfile))[18] != 0);
        yMirrorView.setChecked(Objects.requireNonNull(profiles.get(currProfile))[19] != 0);
    }
    private void listenerInit(){
        micWindowView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER){

                    int index = 0;
                    EditText bufView = micWindowView;

                    int buf = Integer.parseInt(String.valueOf(bufView.getText()));
                    if (buf > 255) { buf = 255; bufView.setText(String.valueOf(buf)); }
                    Objects.requireNonNull(profiles.get(currProfile))[index] = buf;
                    byte[] send = new byte[4]; send[0] = 1; send[1] = 1;
                    send[2] = (byte) (index);
                    send[3] = (byte) (buf);
                    BluetoothManager.send(send);
                    ProjectManager.hideKeyboard(RainbowBackgroundActivity.this, bufView);
                    return true;
                }
                return false;
            }
        });
        micTimeoutView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER){

                    int index = 1;
                    EditText bufView = micTimeoutView;

                    int buf = Integer.parseInt(String.valueOf(bufView.getText()));
                    if (buf > 255) { buf = 255; bufView.setText(String.valueOf(buf)); }
                    Objects.requireNonNull(profiles.get(currProfile))[index] = buf;
                    byte[] send = new byte[4]; send[0] = 1; send[1] = 1;
                    send[2] = (byte) (index);
                    send[3] = (byte) (buf);
                    BluetoothManager.send(send);
                    ProjectManager.hideKeyboard(RainbowBackgroundActivity.this, bufView);
                    return true;
                }
                return false;
            }
        });
        pulseMinView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER){

                    int index = 2;
                    EditText bufView = pulseMinView;

                    int buf = Integer.parseInt(String.valueOf(bufView.getText()));
                    if (buf > 255) { buf = 255; bufView.setText(String.valueOf(buf)); }
                    Objects.requireNonNull(profiles.get(currProfile))[index] = buf;
                    byte[] send = new byte[4]; send[0] = 1; send[1] = 1;
                    send[2] = (byte) (index);
                    send[3] = (byte) (buf);
                    BluetoothManager.send(send);
                    ProjectManager.hideKeyboard(RainbowBackgroundActivity.this, bufView);
                    return true;
                }
                return false;
            }
        });
        speedView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER){

                    int index = 3;
                    EditText bufView = speedView;

                    int buf = Integer.parseInt(String.valueOf(bufView.getText()));
                    if (buf > 255) { buf = 255; bufView.setText(String.valueOf(buf)); }
                    Objects.requireNonNull(profiles.get(currProfile))[index] = buf;
                    byte[] send = new byte[4]; send[0] = 1; send[1] = 1;
                    send[2] = (byte) (index);
                    send[3] = (byte) (buf);
                    BluetoothManager.send(send);
                    ProjectManager.hideKeyboard(RainbowBackgroundActivity.this, bufView);
                    return true;
                }
                return false;
            }
        });
        brightnessView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER){

                    int index = 4;
                    EditText bufView = brightnessView;

                    int buf = Integer.parseInt(String.valueOf(bufView.getText()));
                    if (buf > 255) { buf = 255; bufView.setText(String.valueOf(buf)); }
                    Objects.requireNonNull(profiles.get(currProfile))[index] = buf;
                    byte[] send = new byte[4]; send[0] = 1; send[1] = 1;
                    send[2] = (byte) (index);
                    send[3] = (byte) (buf);
                    BluetoothManager.send(send);
                    ProjectManager.hideKeyboard(RainbowBackgroundActivity.this, bufView);
                    return true;
                }
                return false;
            }
        });
        allowMicView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (isSettingProfile) return;

                int index = 5;

                Objects.requireNonNull(profiles.get(currProfile))[index] = b ? 1 : 0;
                byte[] send = new byte[4]; send[0] = 1; send[1] = 1;
                send[2] = (byte) (index);
                send[3] = (byte) (b ? 1 : 0);
                BluetoothManager.send(send);
            }
        });
        maxFlashVew.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER){

                    int index = 6;
                    EditText bufView = maxFlashVew;

                    int buf = Integer.parseInt(String.valueOf(bufView.getText()));
                    if (buf > 255) { buf = 255; bufView.setText(String.valueOf(buf)); }
                    Objects.requireNonNull(profiles.get(currProfile))[index] = buf;
                    byte[] send = new byte[4]; send[0] = 1; send[1] = 1;
                    send[2] = (byte) (index);
                    send[3] = (byte) (buf);
                    BluetoothManager.send(send);
                    ProjectManager.hideKeyboard(RainbowBackgroundActivity.this, bufView);
                    return true;
                }
                return false;
            }
        });
        reduceFlashView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER){

                    int index = 7;
                    EditText bufView = reduceFlashView;

                    int buf = Integer.parseInt(String.valueOf(bufView.getText()));
                    if (buf > 255) { buf = 255; bufView.setText(String.valueOf(buf)); }
                    Objects.requireNonNull(profiles.get(currProfile))[index] = buf;
                    byte[] send = new byte[4]; send[0] = 1; send[1] = 1;
                    send[2] = (byte) (index);
                    send[3] = (byte) (buf);
                    BluetoothManager.send(send);
                    ProjectManager.hideKeyboard(RainbowBackgroundActivity.this, bufView);
                    return true;
                }
                return false;
            }
        });
        maxBrightnessJumpView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER){

                    int index = 8;
                    EditText bufView = maxBrightnessJumpView;

                    int buf = Integer.parseInt(String.valueOf(bufView.getText()));
                    if (buf > 255) { buf = 255; bufView.setText(String.valueOf(buf)); }
                    Objects.requireNonNull(profiles.get(currProfile))[index] = buf;
                    byte[] send = new byte[4]; send[0] = 1; send[1] = 1;
                    send[2] = (byte) (index);
                    send[3] = (byte) (buf);
                    BluetoothManager.send(send);
                    ProjectManager.hideKeyboard(RainbowBackgroundActivity.this, bufView);
                    return true;
                }
                return false;
            }
        });
        reduceFlashView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER){

                    int index = 9;
                    EditText bufView = reduceFlashView;

                    int buf = Integer.parseInt(String.valueOf(bufView.getText()));
                    if (buf > 255) { buf = 255; bufView.setText(String.valueOf(buf)); }
                    Objects.requireNonNull(profiles.get(currProfile))[index] = buf;
                    byte[] send = new byte[4]; send[0] = 1; send[1] = 1;
                    send[2] = (byte) (index);
                    send[3] = (byte) (buf);
                    BluetoothManager.send(send);
                    ProjectManager.hideKeyboard(RainbowBackgroundActivity.this, bufView);
                    return true;
                }
                return false;
            }
        });
        runJumpView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER){

                    int index = 10;
                    EditText bufView = runJumpView;

                    int buf = Integer.parseInt(String.valueOf(bufView.getText()));
                    if (buf > 255) { buf = 255; bufView.setText(String.valueOf(buf)); }
                    Objects.requireNonNull(profiles.get(currProfile))[index] = buf;
                    byte[] send = new byte[4]; send[0] = 1; send[1] = 1;
                    send[2] = (byte) (index);
                    send[3] = (byte) (buf);
                    BluetoothManager.send(send);
                    ProjectManager.hideKeyboard(RainbowBackgroundActivity.this, bufView);
                    return true;
                }
                return false;
            }
        });
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RainbowBackgroundActivity.this, MenuActivity.class);
                RainbowBackgroundActivity.this.startActivity(intent);
                finish();
            }
        });
        profilesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProjectManager.showPopupMenu(RainbowBackgroundActivity.this, profilesButton,
                        profilesNames, new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                isSettingProfile = true;
                                if (profilesNames.get(i).equals("Добавить")){
                                    ProjectManager.showInputDialog(RainbowBackgroundActivity.this,
                                            "Введите имя нового профиля", new BluetoothFunc() {
                                                @Override
                                                public void run(String s) {
                                                    if (profilesNames.contains(s)){
                                                        Toast.makeText(RainbowBackgroundActivity.this,
                                                                "Такой профиль уже есть", Toast.LENGTH_SHORT).show();
                                                        return;
                                                    }
                                                    byte[] send = new byte[3 + s.length()];
                                                    send[0] = 1; send[1] = 2; send[2] = 1;
                                                    for (int i = 0; i < s.length(); ++i) send[i + 3] = (s.getBytes()[i]);
                                                    BluetoothManager.send(send);
                                                    Toast.makeText(RainbowBackgroundActivity.this, "Создан новый профиль \""
                                                            + s + "\"", Toast.LENGTH_SHORT).show();
                                                    int[] buf = Arrays.copyOf(Objects.requireNonNull(profiles.get("default")),
                                                            Objects.requireNonNull(profiles.get("default")).length);
                                                    profilesNames.remove("Добавить");
                                                    profilesNames.add(s);
                                                    profilesNames.add("Добавить");
                                                    currProfile = s;
                                                    profiles.put(s, buf);
                                                    updateAll();
                                                }
                                            });
                                    return;
                                }
                                currProfile = profilesNames.get(i);
                                updateAll();
                                byte[] send = new byte[3 + currProfile.length()];
                                send[0] = 1; send[1] = 2; send[2] = 0;
                                for (int j = 0; j < currProfile.length(); j++) send[j + 3] = currProfile.getBytes()[j];
                                BluetoothManager.send(send);
                                updateAll();
                                isSettingProfile = false;
                            }
                        });
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byte[] send = new byte[3 + currProfile.length()];
                send[0] = 1; send[1] = 2; send[2] = 2;
                for (int i = 0; i < currProfile.length(); ++i) send[i + 3] = currProfile.getBytes()[i];
                BluetoothManager.send(send);
                profilesNames.remove(currProfile);
                profiles.remove(currProfile);
                currProfile = "default";
                updateAll();
            }
        });
        //////////////////////////////////////////////////////////////////////////////////////////////
        baseStepView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER){

                    int index = 11;
                    EditText bufView = baseStepView;

                    int buf = Integer.parseInt(String.valueOf(bufView.getText()));
                    if (buf > 255) { buf = 255; bufView.setText(String.valueOf(buf)); }
                    Objects.requireNonNull(profiles.get(currProfile))[index] = buf;
                    byte[] send = new byte[4]; send[0] = 1; send[1] = 1;
                    send[2] = (byte) (index);
                    send[3] = (byte) (buf);
                    BluetoothManager.send(send);
                    ProjectManager.hideKeyboard(RainbowBackgroundActivity.this, bufView);
                    return true;
                }
                return false;
            }
        });
        xStepView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER){

                    int index = 12;
                    EditText bufView = xStepView;

                    int buf = Integer.parseInt(String.valueOf(bufView.getText()));
                    if (buf > 255) { buf = 255; bufView.setText(String.valueOf(buf)); }
                    Objects.requireNonNull(profiles.get(currProfile))[index] = buf;
                    byte[] send = new byte[4]; send[0] = 1; send[1] = 1;
                    send[2] = (byte) (index);
                    send[3] = (byte) (buf);
                    BluetoothManager.send(send);
                    ProjectManager.hideKeyboard(RainbowBackgroundActivity.this, bufView);
                    return true;
                }
                return false;
            }
        });
        yStepView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER){

                    int index = 13;
                    EditText bufView = yStepView;

                    int buf = Integer.parseInt(String.valueOf(bufView.getText()));
                    if (buf > 255) { buf = 255; bufView.setText(String.valueOf(buf)); }
                    Objects.requireNonNull(profiles.get(currProfile))[index] = buf;
                    byte[] send = new byte[4]; send[0] = 1; send[1] = 1;
                    send[2] = (byte) (index);
                    send[3] = (byte) (buf);
                    BluetoothManager.send(send);
                    ProjectManager.hideKeyboard(RainbowBackgroundActivity.this, bufView);
                    return true;
                }
                return false;
            }
        });
        saturationView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER){

                    int index = 14;
                    EditText bufView = saturationView;

                    int buf = Integer.parseInt(String.valueOf(bufView.getText()));
                    if (buf > 255) { buf = 255; bufView.setText(String.valueOf(buf)); }
                    Objects.requireNonNull(profiles.get(currProfile))[index] = buf;
                    byte[] send = new byte[4]; send[0] = 1; send[1] = 1;
                    send[2] = (byte) (index);
                    send[3] = (byte) (buf);
                    BluetoothManager.send(send);
                    ProjectManager.hideKeyboard(RainbowBackgroundActivity.this, bufView);
                    return true;
                }
                return false;
            }
        });
        directionView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (isSettingProfile) return;

                int index = 15;

                Objects.requireNonNull(profiles.get(currProfile))[index] = b ? 1 : 0;
                byte[] send = new byte[4]; send[0] = 1; send[1] = 1;
                send[2] = (byte) (index);
                send[3] = (byte) (b ? 1 : 0);
                BluetoothManager.send(send);
            }
        });
        xDirectionView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (isSettingProfile) return;

                int index = 16;

                Objects.requireNonNull(profiles.get(currProfile))[index] = b ? 1 : 0;
                byte[] send = new byte[4]; send[0] = 1; send[1] = 1;
                send[2] = (byte) (index);
                send[3] = (byte) (b ? 1 : 0);
                BluetoothManager.send(send);
            }
        });
        yDirectionView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (isSettingProfile) return;

                int index = 17;

                Objects.requireNonNull(profiles.get(currProfile))[index] = b ? 1 : 0;
                byte[] send = new byte[4]; send[0] = 1; send[1] = 1;
                send[2] = (byte) (index);
                send[3] = (byte) (b ? 1 : 0);
                BluetoothManager.send(send);
            }
        });
        xMirrorView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (isSettingProfile) return;

                int index = 18;

                Objects.requireNonNull(profiles.get(currProfile))[index] = b ? 1 : 0;
                byte[] send = new byte[4]; send[0] = 1; send[1] = 1;
                send[2] = (byte) (index);
                send[3] = (byte) (b ? 1 : 0);
                BluetoothManager.send(send);
            }
        });
        yMirrorView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (isSettingProfile) return;

                int index = 19;

                Objects.requireNonNull(profiles.get(currProfile))[index] = b ? 1 : 0;
                byte[] send = new byte[4]; send[0] = 1; send[1] = 1;
                send[2] = (byte) (index);
                send[3] = (byte) (b ? 1 : 0);
                BluetoothManager.send(send);
            }
        });
    }
}