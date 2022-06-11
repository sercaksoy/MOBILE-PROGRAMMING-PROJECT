package serc.mphw.memoryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class AddMemoryActivity extends AppCompatActivity {
    private EditText memoryName;
    private EditText memoryLocation;
    private EditText memoryText;

    private Button addMemoryBtn;

    private Spinner emojiSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memory);

        memoryName = (EditText) findViewById(R.id.memoryName);
        memoryLocation = (EditText) findViewById(R.id.memoryLocation);
        memoryText = (EditText) findViewById(R.id.memoryText);

        addMemoryBtn = (Button) findViewById(R.id.addMemoryBtn);

        emojiSpinner = (Spinner) findViewById(R.id.emojispinner);

        ArrayAdapter<String> spinnerArray = new ArrayAdapter<String>
                (AddMemoryActivity.this, android.R.layout.simple_list_item_1,
                        getResources().getStringArray(R.array.emojispinner));
        spinnerArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        emojiSpinner.setAdapter(spinnerArray);

        addMemoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String memory_name = memoryName.getText().toString();
                String memory_location = memoryLocation.getText().toString();
                String memory_text = memoryText.getText().toString();
                String memory_emotion = emojiSpinner.getSelectedItem().toString();
                String memory_pwd = "NOPWD";
                if (memory_text.length() > 400){
                    Toast.makeText(getApplicationContext(), "Anı uzunluğu 400 karakterden fazla, azaltmalısın.", Toast.LENGTH_SHORT).show();
                }
                else if (memoryNameAppears(memory_name)){
                    Toast.makeText(getApplicationContext(), "Aynı adı verdiğin bir anı zaten bulunuyor, daha yaratıcı ol.", Toast.LENGTH_LONG).show();
                }
                else {
                    MemoryBody memory_new = new MemoryBody(memory_name, memory_text,
                            memory_pwd, memory_location, memory_emotion);

                    MainActivity.memoriesList.add(memory_new);
                    finish();
                }

            }
        });

    }
    private boolean memoryNameAppears(String memory_name) {
        for (MemoryBody memory : MainActivity.memoriesList){
            if (memory_name.equals(memory.getMemoryName()))
                return true;
        }
        return false;
    }

}