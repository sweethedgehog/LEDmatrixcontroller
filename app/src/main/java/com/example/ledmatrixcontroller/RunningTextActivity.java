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

public class RunningTextActivity extends AppCompatActivity {
    private static HashMap<String, int[]> profiles = new HashMap<>();
    public static ArrayList<String> profilesNames = new ArrayList<>();
    public static String currProfile = "default";
    private static boolean isSettingProfile = false;

    private static EditText micWindowView, micTimeoutView, pulseMinView, speedView, brightnessView,
            maxFlashVew, reduceFlashView, maxBrightnessJumpView, reduceBrightnessJumpView, runJumpView;
    private static Switch allowMicView;
    private static TextView profileView;
    private static ImageButton profilesButton, menuButton, deleteButton;

    public static String text;
    private static EditText hueOffsetView, saturationOffsetView, valueOffsetView, textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_text);

        if (!profilesNames.get(profilesNames.size() - 1).equals("Добавить")) profilesNames.add("Добавить");
        profileView = findViewById(R.id.running_text_profile_text_view);
        menuButton = findViewById(R.id.running_text_back_to_menu_button);
        profilesButton = findViewById(R.id.running_text_profile_select_button);
        deleteButton = findViewById(R.id.running_text_delete_button);
        micWindowView = findViewById(R.id.running_text_mic_window_plain_text);
        micTimeoutView = findViewById(R.id.running_text_mic_timeout_plain_text);
        pulseMinView = findViewById(R.id.running_text_mic_pulse_plain_text);
        speedView = findViewById(R.id.running_text_speed_plain_text);
        brightnessView = findViewById(R.id.running_text_brightness_plain_text);
        runJumpView = findViewById(R.id.running_text_run_jump_plain_text);
        maxFlashVew = findViewById(R.id.running_text_max_sat_flash_plain_text);
        reduceFlashView = findViewById(R.id.running_text_sat_flash_reduce_plain_text);
        maxBrightnessJumpView = findViewById(R.id.running_text_max_br_flash_plain_text);
        reduceBrightnessJumpView = findViewById(R.id.running_text_br_flash_reduce_plain_text);
        allowMicView = findViewById(R.id.running_text_allow_mic_switch);
        /////////////////////////////////////////////////////////////////////////////////////////
        hueOffsetView = findViewById(R.id.running_text_hue_offset_plain_text);
        saturationOffsetView = findViewById(R.id.running_text_sat_offset_plain_text);
        valueOffsetView = findViewById(R.id.running_text_value_offset_plain_text);
        textView = findViewById(R.id.running_text_plain_text);

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

    private void updateAll() {
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
        hueOffsetView.setText(String.valueOf(Objects.requireNonNull(profiles.get(currProfile))[11]));
        saturationOffsetView.setText(String.valueOf(Objects.requireNonNull(profiles.get(currProfile))[12]));
        valueOffsetView.setText(String.valueOf(Objects.requireNonNull(profiles.get(currProfile))[13]));

        textView.setText(text);
    }

    private void listenerInit() {
        micWindowView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {

                    int index = 0;
                    EditText bufView = micWindowView;

                    int buf = Integer.parseInt(String.valueOf(bufView.getText()));
                    if (buf > 255) {
                        buf = 255;
                        bufView.setText(String.valueOf(buf));
                    }
                    Objects.requireNonNull(profiles.get(currProfile))[index] = buf;
                    byte[] send = new byte[4];
                    send[0] = 0;
                    send[1] = 1;
                    send[2] = (byte) (index);
                    send[3] = (byte) (buf);
                    BluetoothManager.send(send);
                    ProjectManager.hideKeyboard(RunningTextActivity.this, bufView);
                    return true;
                }
                return false;
            }
        });
        micTimeoutView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {

                    int index = 1;
                    EditText bufView = micTimeoutView;

                    int buf = Integer.parseInt(String.valueOf(bufView.getText()));
                    if (buf > 255) {
                        buf = 255;
                        bufView.setText(String.valueOf(buf));
                    }
                    Objects.requireNonNull(profiles.get(currProfile))[index] = buf;
                    byte[] send = new byte[4];
                    send[0] = 0;
                    send[1] = 1;
                    send[2] = (byte) (index);
                    send[3] = (byte) (buf);
                    BluetoothManager.send(send);
                    ProjectManager.hideKeyboard(RunningTextActivity.this, bufView);
                    return true;
                }
                return false;
            }
        });
        pulseMinView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {

                    int index = 2;
                    EditText bufView = pulseMinView;

                    int buf = Integer.parseInt(String.valueOf(bufView.getText()));
                    if (buf > 255) {
                        buf = 255;
                        bufView.setText(String.valueOf(buf));
                    }
                    Objects.requireNonNull(profiles.get(currProfile))[index] = buf;
                    byte[] send = new byte[4];
                    send[0] = 0;
                    send[1] = 1;
                    send[2] = (byte) (index);
                    send[3] = (byte) (buf);
                    BluetoothManager.send(send);
                    ProjectManager.hideKeyboard(RunningTextActivity.this, bufView);
                    return true;
                }
                return false;
            }
        });
        speedView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {

                    int index = 3;
                    EditText bufView = speedView;

                    int buf = Integer.parseInt(String.valueOf(bufView.getText()));
                    if (buf > 255) {
                        buf = 255;
                        bufView.setText(String.valueOf(buf));
                    }
                    Objects.requireNonNull(profiles.get(currProfile))[index] = buf;
                    byte[] send = new byte[4];
                    send[0] = 0;
                    send[1] = 1;
                    send[2] = (byte) (index);
                    send[3] = (byte) (buf);
                    BluetoothManager.send(send);
                    ProjectManager.hideKeyboard(RunningTextActivity.this, bufView);
                    return true;
                }
                return false;
            }
        });
        brightnessView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {

                    int index = 4;
                    EditText bufView = brightnessView;

                    int buf = Integer.parseInt(String.valueOf(bufView.getText()));
                    if (buf > 255) {
                        buf = 255;
                        bufView.setText(String.valueOf(buf));
                    }
                    Objects.requireNonNull(profiles.get(currProfile))[index] = buf;
                    byte[] send = new byte[4];
                    send[0] = 0;
                    send[1] = 1;
                    send[2] = (byte) (index);
                    send[3] = (byte) (buf);
                    BluetoothManager.send(send);
                    ProjectManager.hideKeyboard(RunningTextActivity.this, bufView);
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
                byte[] send = new byte[4];
                send[0] = 1;
                send[1] = 1;
                send[2] = (byte) (index);
                send[3] = (byte) (b ? 1 : 0);
                BluetoothManager.send(send);
            }
        });
        maxFlashVew.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {

                    int index = 6;
                    EditText bufView = maxFlashVew;

                    int buf = Integer.parseInt(String.valueOf(bufView.getText()));
                    if (buf > 255) {
                        buf = 255;
                        bufView.setText(String.valueOf(buf));
                    }
                    Objects.requireNonNull(profiles.get(currProfile))[index] = buf;
                    byte[] send = new byte[4];
                    send[0] = 0;
                    send[1] = 1;
                    send[2] = (byte) (index);
                    send[3] = (byte) (buf);
                    BluetoothManager.send(send);
                    ProjectManager.hideKeyboard(RunningTextActivity.this, bufView);
                    return true;
                }
                return false;
            }
        });
        reduceFlashView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {

                    int index = 7;
                    EditText bufView = reduceFlashView;

                    int buf = Integer.parseInt(String.valueOf(bufView.getText()));
                    if (buf > 255) {
                        buf = 255;
                        bufView.setText(String.valueOf(buf));
                    }
                    Objects.requireNonNull(profiles.get(currProfile))[index] = buf;
                    byte[] send = new byte[4];
                    send[0] = 0;
                    send[1] = 1;
                    send[2] = (byte) (index);
                    send[3] = (byte) (buf);
                    BluetoothManager.send(send);
                    ProjectManager.hideKeyboard(RunningTextActivity.this, bufView);
                    return true;
                }
                return false;
            }
        });
        maxBrightnessJumpView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {

                    int index = 8;
                    EditText bufView = maxBrightnessJumpView;

                    int buf = Integer.parseInt(String.valueOf(bufView.getText()));
                    if (buf > 255) {
                        buf = 255;
                        bufView.setText(String.valueOf(buf));
                    }
                    Objects.requireNonNull(profiles.get(currProfile))[index] = buf;
                    byte[] send = new byte[4];
                    send[0] = 0;
                    send[1] = 1;
                    send[2] = (byte) (index);
                    send[3] = (byte) (buf);
                    BluetoothManager.send(send);
                    ProjectManager.hideKeyboard(RunningTextActivity.this, bufView);
                    return true;
                }
                return false;
            }
        });
        reduceFlashView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {

                    int index = 9;
                    EditText bufView = reduceFlashView;

                    int buf = Integer.parseInt(String.valueOf(bufView.getText()));
                    if (buf > 255) {
                        buf = 255;
                        bufView.setText(String.valueOf(buf));
                    }
                    Objects.requireNonNull(profiles.get(currProfile))[index] = buf;
                    byte[] send = new byte[4];
                    send[0] = 0;
                    send[1] = 1;
                    send[2] = (byte) (index);
                    send[3] = (byte) (buf);
                    BluetoothManager.send(send);
                    ProjectManager.hideKeyboard(RunningTextActivity.this, bufView);
                    return true;
                }
                return false;
            }
        });
        runJumpView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {

                    int index = 10;
                    EditText bufView = runJumpView;

                    int buf = Integer.parseInt(String.valueOf(bufView.getText()));
                    if (buf > 255) {
                        buf = 255;
                        bufView.setText(String.valueOf(buf));
                    }
                    Objects.requireNonNull(profiles.get(currProfile))[index] = buf;
                    byte[] send = new byte[4];
                    send[0] = 0;
                    send[1] = 1;
                    send[2] = (byte) (index);
                    send[3] = (byte) (buf);
                    BluetoothManager.send(send);
                    ProjectManager.hideKeyboard(RunningTextActivity.this, bufView);
                    return true;
                }
                return false;
            }
        });
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RunningTextActivity.this, MenuActivity.class);
                RunningTextActivity.this.startActivity(intent);
                finish();
            }
        });
        profilesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProjectManager.showPopupMenu(RunningTextActivity.this, profilesButton,
                        profilesNames, new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                isSettingProfile = true;
                                if (profilesNames.get(i).equals("Добавить")) {
                                    ProjectManager.showInputDialog(RunningTextActivity.this,
                                            "Введите имя нового профиля", new BluetoothFunc() {
                                                @Override
                                                public void run(String s) {
                                                    if (profilesNames.contains(s)) {
                                                        Toast.makeText(RunningTextActivity.this,
                                                                "Такой профиль уже есть", Toast.LENGTH_SHORT).show();
                                                        return;
                                                    }
                                                    byte[] send = new byte[3 + s.length()];
                                                    send[0] = 0;
                                                    send[1] = 2;
                                                    send[2] = 1;
                                                    for (int i = 0; i < s.length(); ++i)
                                                        send[i + 3] = (s.getBytes()[i]);
                                                    BluetoothManager.send(send);
                                                    Toast.makeText(RunningTextActivity.this, "Создан новый профиль \""
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
                                send[0] = 0;
                                send[1] = 2;
                                send[2] = 0;
                                for (int j = 0; j < currProfile.length(); j++)
                                    send[j + 3] = currProfile.getBytes()[j];
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
                send[0] = 1;
                send[1] = 2;
                send[2] = 2;
                for (int i = 0; i < currProfile.length(); ++i)
                    send[i + 3] = currProfile.getBytes()[i];
                BluetoothManager.send(send);
                profilesNames.remove(currProfile);
                profiles.remove(currProfile);
                currProfile = "default";
                updateAll();
            }
        });
        //////////////////////////////////////////////////////////////////////////////////////////////
        hueOffsetView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {

                    int index = 11;
                    EditText bufView = hueOffsetView;

                    int buf = Integer.parseInt(String.valueOf(bufView.getText()));
                    if (buf > 255) {
                        buf = 255;
                        bufView.setText(String.valueOf(buf));
                    }
                    Objects.requireNonNull(profiles.get(currProfile))[index] = buf;
                    byte[] send = new byte[4];
                    send[0] = 0;
                    send[1] = 1;
                    send[2] = (byte) (index);
                    send[3] = (byte) (buf);
                    BluetoothManager.send(send);
                    ProjectManager.hideKeyboard(RunningTextActivity.this, bufView);
                    return true;
                }
                return false;
            }
        });
        saturationOffsetView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {

                    int index = 12;
                    EditText bufView = saturationOffsetView;

                    int buf = Integer.parseInt(String.valueOf(bufView.getText()));
                    if (buf > 255) {
                        buf = 255;
                        bufView.setText(String.valueOf(buf));
                    }
                    Objects.requireNonNull(profiles.get(currProfile))[index] = buf;
                    byte[] send = new byte[4];
                    send[0] = 0;
                    send[1] = 1;
                    send[2] = (byte) (index);
                    send[3] = (byte) (buf);
                    BluetoothManager.send(send);
                    ProjectManager.hideKeyboard(RunningTextActivity.this, bufView);
                    return true;
                }
                return false;
            }
        });
        valueOffsetView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {

                    int index = 13;
                    EditText bufView = valueOffsetView;

                    int buf = Integer.parseInt(String.valueOf(bufView.getText()));
                    if (buf > 255) {
                        buf = 255;
                        bufView.setText(String.valueOf(buf));
                    }
                    Objects.requireNonNull(profiles.get(currProfile))[index] = buf;
                    byte[] send = new byte[4];
                    send[0] = 0;
                    send[1] = 1;
                    send[2] = (byte) (index);
                    send[3] = (byte) (buf);
                    BluetoothManager.send(send);
                    ProjectManager.hideKeyboard(RunningTextActivity.this, bufView);
                    return true;
                }
                return false;
            }
        });
        //todo maybe some clever text setting
        textView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {

                    EditText bufView = textView;

                    String buf = String.valueOf(textView.getText());
                    buf = buf.toUpperCase();
                    textView.setText(buf);
                    byte[] send = new byte[2 + buf.length()];
                    send[0] = 0;
                    send[1] = 4;
                    for (int j = 0; j < buf.length(); j++) send[j + 2] = buf.getBytes()[j];
                    BluetoothManager.send(send);
                    ProjectManager.hideKeyboard(RunningTextActivity.this, bufView);
                    return true;
                }
                return false;
            }
        });
    }

    public static void setSettings(int i, byte[] settings) {
        if (i == 0) currProfile = new String(settings);
        else if (i == 1) text = new String(settings);
        else if (i % 2 == 0) profilesNames.add(new String(settings));
        else profiles.put(profilesNames.get(profilesNames.size() - 1), ProjectManager.strToIntArr(settings));
    }

    public static void clearAll(){
        profiles.clear();
        profilesNames.clear();
    }
}