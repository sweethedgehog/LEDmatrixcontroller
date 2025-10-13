package com.example.ledmatrixcontroller;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.welie.blessed.BluetoothPeripheral;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    ListView listView;
    Button button;
    EditText editText;
    ArrayList<Integer> bytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        bytes = new ArrayList<>();
        button = findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byte[] buf = new byte[bytes.size()];
                for (int i = 0; i < bytes.size(); ++i) buf[i] = (byte) (int) bytes.get(i);
                BluetoothManager.send(buf);
                bytes.clear();
                updateListView();
            }
        });
        listView = findViewById(R.id.listView2);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                bytes.remove(i);
                updateListView();
            }
        });
        editText = findViewById(R.id.editTextNumber);
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER){
                    int buf = (256 + Integer.parseInt(String.valueOf(editText.getText()))) % 256;
                    editText.setText("");
                    bytes.add(buf);
                    updateListView();
                    return true;
                }
                return false;
            }
        });
    }
    private void updateListView(){
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bytes);
        listView.setAdapter(adapter);
    }
}