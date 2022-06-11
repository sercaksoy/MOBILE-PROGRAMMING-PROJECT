package serc.mphw.memoryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.internal.SafeIterableMap;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ImageView addMemoryImage;
    private RecyclerView recyclerView;
    public static MemoryAdapter mAdapter;
    public static ArrayList<MemoryBody> memoriesList;
    private static final String FILE_NAME = "memories_storage.txt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addMemoryImage = (ImageView) findViewById(R.id.add_memory_image);
        addMemoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent add_memory_intent = new Intent(MainActivity.this, AddMemoryActivity.class);
                startActivity(add_memory_intent);
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.main_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        //memoriesList = getMemories_fake();
        try {
            memoriesList = getMemories();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (memoriesList.size() == 0)
            Toast.makeText(getApplicationContext(), "Burası çok boş, biraz anılarını paylaş.", Toast.LENGTH_LONG).show();

        mAdapter = new MemoryAdapter(memoriesList, MainActivity.this);
        recyclerView.setAdapter(mAdapter);

    }
    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        String all_data = dataToString(memoriesList);
        FileOutputStream fos = null; //new FileOutputStream();
        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(all_data.getBytes(StandardCharsets.UTF_8));
            Toast.makeText(this, "Anılar başarılı bir şekilde lokale alındı.", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Open file
        // Write all_data
        // Close file
    }
    private String dataToString(ArrayList<MemoryBody> memoriesList){
        String totalString = "";
        for(MemoryBody memory:memoriesList){
            String memory_name = memory.getMemoryName();
            String memory_text = memory.getMemoryText();
            String memory_location = memory.getMemoryLocation();
            Date memory_date_date = memory.getMemoryDate();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String memory_date = (sdf.format(memory_date_date));
            String memory_emotion = memory.getMemoryEmotion();
            String memory_pwd = memory.getMemoryPwd();

            String localStr =  memory_name + "_MEMOPART_"
                                + memory_text + "_MEMOPART_"
                                + memory_location + "_MEMOPART_"
                                + memory_date + "_MEMOPART_"
                                + memory_emotion + "_MEMOPART_"
                                + memory_pwd;
            if(totalString == ""){
                totalString = localStr;
            }
            else{
                totalString = totalString + "_ENDOFMEMORY_" + localStr;
            }
        }
        return totalString;
    }

    private ArrayList<MemoryBody> getMemories() throws ParseException {
        ArrayList<MemoryBody> list = new ArrayList<>();
        // Open a file
        FileInputStream fis = null;
        StringBuilder sb = null;
        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            sb = new StringBuilder();
            String text;
            while ((text = br.readLine()) != null){
                sb.append(text).append("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if (fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Read the file entirely
        // String memos1 = "memoname1_MEMOPART_memotext1_MEMOPART_memolocation1_MEMOPART_30/07/1998_MEMOPART_memoemotion1_MEMOPART_memopwd1";
        // String memos2 = "memoname2_MEMOPART_memotext2_MEMOPART_memolocation2_MEMOPART_30/07/2022_MEMOPART_memoemotion2_MEMOPART_memopwd2";
        // String allMemories = memos1 + "_ENDOFMEMORY_" + memos2;
        // String allMemories = readFile();
        // Split memories like _ENDOFMEMORY_
        String allMemories = sb.toString();
        String[] memoriesList = allMemories.split("_ENDOFMEMORY_",-1);

        for(String memory : memoriesList){
            String[] memory_parts = memory.split("_MEMOPART_",-1);
            if(memory_parts.length != 6){
                break;
            }
            String memory_name = memory_parts[0];
            String memory_text = memory_parts[1];
            String memory_location = memory_parts[2];
            String memory_date = memory_parts[3];
            Date memory_date_date = new SimpleDateFormat("dd/MM/yyyy").parse(memory_date);
            String memory_emotion = memory_parts[4];
            String memory_pwd = memory_parts[5];

            MemoryBody tempMemo = new MemoryBody(memory_name, memory_text, memory_pwd, memory_location, memory_emotion);
            tempMemo.setMemoryDate(memory_date_date);
            list.add(tempMemo);

        }
        return list;
    }
    private ArrayList<MemoryBody> getMemories_fake() {
        ArrayList<MemoryBody> list = new ArrayList<>();
        list.add(new MemoryBody("Bir Hüzün", getResources().getString(R.string.lorem_ipsum),
                "","Üsküdar", "Üzgün \uD83D\uDE14"));
        list.add(new MemoryBody("Bir Mutluluk", getResources().getString(R.string.lorem_ipsum),
                "","Kadıköy", "Mutlu \uD83D\uDE04"));
        list.add(new MemoryBody("Bir Heyecan", getResources().getString(R.string.lorem_ipsum),
                "","Beşiktaş", "Heyecanlı \uD83D\uDE32"));
        list.add(new MemoryBody("Bir Kötülük", getResources().getString(R.string.lorem_ipsum),
                "","Karaköy", "Kötücül \uD83D\uDE08"));
        list.add(new MemoryBody("Bir İyilik", getResources().getString(R.string.lorem_ipsum),
                "","Levent", "Pozitif \uD83D\uDE07"));
        return list;
    }
    public static int idxOfMemory(String memoryName){
        int idx = 0;
        for (int i=0; i<memoriesList.size(); i++)
            if (memoriesList.get(i).getMemoryName().equals(memoryName))
                return i;
        return -1;
    }
}