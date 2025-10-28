package com.example.ledmatrixcontroller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class MenuActivity extends AppCompatActivity {
    public static boolean isBackgroundOn = true;
    public static boolean isGifOn = true;
    public static String name = "name";
    public static ArrayList<String> anims = new ArrayList<>(Arrays.asList(new String[]{"text"}));

    private EditText nameView;
    private Button rebootButtonView;
    private ImageButton backgroundSelectButtonView, gifSelectButtonView;
    private Switch isBackgroundOnView, isGifOnView;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BluetoothManager.disconnect();
        ProjectManager.wasConnected = false;
        clearAll();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        nameView = findViewById(R.id.bluetooth_name_text_edit_menu_activity);
        rebootButtonView = findViewById(R.id.reboot_button_menu_activity);
        backgroundSelectButtonView = findViewById(R.id.background_select_button_menu_activity);
        gifSelectButtonView = findViewById(R.id.anim_select_button_menu_activity);
        isBackgroundOnView = findViewById(R.id.background_is_on_switch_menu_activity);
        isGifOnView = findViewById(R.id.anim_is_on_switch_menu_activity);

        updateAll();
        listenersInit();
    }

    private void updateAll() {
        nameView.setText(name);
        isBackgroundOnView.setChecked(isBackgroundOn);
        isGifOnView.setChecked(isGifOn);
    }

    private void listenersInit() {
        nameView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                    ProjectManager.hideKeyboard(MenuActivity.this, nameView);
                    name = String.valueOf(nameView.getText());
                    byte[] buf = new byte[name.length() + 1];
                    buf[0] = 2;
                    for (int j = 0; j < name.length(); ++j) buf[j + 1] = name.getBytes()[j];
                    BluetoothManager.send(buf);
                    ProjectManager.hideKeyboard(MenuActivity.this, nameView);
                    return true;
                }
                return false;
            }
        });
        rebootButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothManager.send(new byte[]{3});
                ProjectManager.wasConnected = false;
//                clearAll();
                finish();
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BluetoothManager.disconnect();
                    }
                }, 100);
            }
        });
        backgroundSelectButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProjectManager.showPopupMenu(MenuActivity.this, backgroundSelectButtonView,
                        ProjectManager.backgrounds, new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String bufS = ProjectManager.backgrounds.get(i);
                                byte[] buf = new byte[bufS.length() + 2];
                                buf[0] = 1;
                                buf[1] = 0;
                                for (int j = 0; j < bufS.length(); ++j) buf[j + 2] = bufS.getBytes()[j];
                                BluetoothManager.send(buf);
                                finish();
                                setNewBackground(bufS);
                            }
                        });
            }
        });
        gifSelectButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProjectManager.showPopupMenu(MenuActivity.this, gifSelectButtonView,
                        anims, new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String bufS = anims.get(i);
                                byte[] buf = new byte[bufS.length() + 2];
                                buf[0] = 0; buf[1] = 0;
                                for (int j = 0; j < bufS.length(); ++j) buf[j + 2] = bufS.getBytes()[j];
                                BluetoothManager.send(buf);
                                finish();
                                Intent intent;
                                if (bufS.equals("text")) intent = new Intent(MenuActivity.this, RunningTextActivity.class);
                                else {
                                    intent = new Intent(MenuActivity.this, GifActivity.class);
                                    GifActivity.name = bufS;
                                    GifActivity.currProfile = ProjectManager.currProfilesGifs.get(bufS);
                                    GifActivity.profilesNames = ProjectManager.gifsProfiles.get(bufS);
                                    GifActivity.profiles = ProjectManager.gifs.get(bufS);
                                }
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                MenuActivity.this.startActivity(intent);
                            }
                        });
            }
        });
        isBackgroundOnView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isBackgroundOn = b;
                BluetoothManager.send(new byte[]{1, 3});
            }
        });
        isGifOnView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isGifOn = b;
                BluetoothManager.send(new byte[]{0, 3});
            }
        });
    }

    private void setNewBackground(String name) {
        Intent intent = null;
        if (name.equals(ProjectManager.backgrounds.get(0)))
            intent = new Intent(this, SingleColorBackgroundActivity.class);
        else if (name.equals(ProjectManager.backgrounds.get(1)))
            intent = new Intent(this, RainbowBackgroundActivity.class);
        else if (name.equals(ProjectManager.backgrounds.get(2)))
            intent = new Intent(this, PerlinBackgroundActivity.class);
        else if (name.equals(ProjectManager.backgrounds.get(3)))
            intent = new Intent(this, PartyBackgroundActivity.class);
        if (intent == null) return;
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    public static void clearAll(){
        anims.clear();
        anims.add("text");
    }
}