package serc.mphw.memoryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class EditMemory extends AppCompatActivity {
    private EditText memoryName;
    private EditText memoryLocation;
    private EditText memoryText;

    private Button addMemoryBtn;

    private Spinner emojiSpinner;

    private static final String FILE_NAME = "memories_storage.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_memory);

        memoryName = (EditText) findViewById(R.id.memoryName);
        memoryLocation = (EditText) findViewById(R.id.memoryLocation);
        memoryText = (EditText) findViewById(R.id.memoryText);
        addMemoryBtn = (Button) findViewById(R.id.addMemoryBtn);

        emojiSpinner = (Spinner) findViewById(R.id.emojispinner);

        Intent intent = getIntent();
        String memory_name = intent.getStringExtra("memory_name");
        String memory_emotion = intent.getStringExtra("memory_emotion");
        String memory_location = intent.getStringExtra("memory_location");
        String memory_text = intent.getStringExtra("memory_text");

        ArrayAdapter<String> spinnerArray = new ArrayAdapter<String>
                (EditMemory.this, android.R.layout.simple_list_item_1,
                        getResources().getStringArray(R.array.emojispinner));
        spinnerArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        emojiSpinner.setSelection(getIndex(emojiSpinner, memory_emotion));
        emojiSpinner.setAdapter(spinnerArray);

        memoryName.setText(memory_name);
        memoryLocation.setText(memory_location);
        memoryText.setText(memory_text);

        addMemoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.memoriesList.remove(MainActivity.idxOfMemory(memory_name));
                String memory_name = memoryName.getText().toString();
                String memory_location = memoryLocation.getText().toString();
                String memory_text = memoryText.getText().toString();
                String memory_emotion = emojiSpinner.getSelectedItem().toString();
                String memory_pwd = "";
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
    private int getIndex(Spinner spinner, String myString){
        int index = 0;
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }
    private boolean memoryNameAppears(String memory_name) {
        for (MemoryBody memory : MainActivity.memoriesList){
            if (memory_name.equals(memory.getMemoryName()))
                return true;
        }
        return false;
    }
}