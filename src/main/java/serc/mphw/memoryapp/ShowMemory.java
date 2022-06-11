package serc.mphw.memoryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.media.audiofx.EnvironmentalReverb;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;


public class ShowMemory extends AppCompatActivity {

    private TextView memoryName;
    private TextView memoryEmotion;
    private TextView memoryLocation;
    private TextView memoryDate;
    private TextView memoryText;

    private ImageView convertPdfImg;
    private ImageView shareBtn;

    private static String memory_name;
    private static String memory_emotion;
    private static String memory_location;
    private static String memory_text;
    private static String memory_date;

    private static String phone_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_memory);

        memoryName = (TextView) findViewById(R.id.showmemory_memory_name);
        memoryEmotion = (TextView) findViewById(R.id.showmemory_memory_emotion);
        memoryLocation = (TextView) findViewById(R.id.showmemory_memory_location);
        memoryDate = (TextView) findViewById(R.id.showmemory_memory_date);
        memoryText = (TextView) findViewById(R.id.showmemory_memory_text);

        convertPdfImg = (ImageView) findViewById(R.id.convertPdfImg);
        shareBtn = (ImageView) findViewById(R.id.share_memory_image);

        Intent intent = getIntent();
        memory_name = intent.getStringExtra("memory_name");
        memory_emotion = intent.getStringExtra("memory_emotion");
        memory_location = intent.getStringExtra("memory_location");
        memory_text = intent.getStringExtra("memory_text");
        memory_date = intent.getStringExtra("memory_date");

        memoryName.setText(memory_name);
        memoryEmotion.setText(memory_emotion);
        memoryLocation.setText(memory_location);
        memoryText.setText(memory_text);
        memoryDate.setText(memory_date);

        if (ContextCompat.checkSelfPermission(ShowMemory.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ShowMemory.this, Manifest.permission.READ_CONTACTS)) {
                Toast.makeText(ShowMemory.this, "İzin sağlamalısınız.", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(ShowMemory.this, new String[]{Manifest.permission.READ_CONTACTS}, 1618);
            }
        }
        if (ContextCompat.checkSelfPermission(ShowMemory.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ShowMemory.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(ShowMemory.this, "İzin sağlamalısınız.", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(ShowMemory.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1619);
            }
        }


        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, 1);

            }
        });

        convertPdfImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create a new document
                String writeIntoFile = memory_name + "\n" + memory_text
                        + "\n" + memory_location
                        + "\n" + memory_emotion + "\n";
                Paint myPaint = new Paint();

                PdfDocument document = new PdfDocument();
                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(100, 100, 1).create();
                PdfDocument.Page page = document.startPage(pageInfo);
                Canvas canvas = page.getCanvas();
                canvas.drawText(writeIntoFile, 40, 50, myPaint);
                View content = findViewById(android.R.id.content);
                content.draw(page.getCanvas());
                document.finishPage(page);

                File file = new File(Environment.getExternalStorageDirectory(), "/" + memory_name + ".pdf");
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    document.writeTo(new FileOutputStream(file));
                    Toast.makeText(ShowMemory.this, "Pdf dosyası " + Environment.getExternalStorageDirectory() + " konumuna kaydedildi.", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                document.close();

            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 1) && (resultCode == RESULT_OK)) {
            Cursor cursor = null;
            try {
                Uri uri = data.getData();
                cursor = getContentResolver().query(uri, new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER }, null, null, null);
                if (cursor != null && cursor.moveToNext()) {
                    String phone = cursor.getString(0);
                    phone_num = phone;
                    Uri uri2 = Uri.parse("smsto:"+phone_num);
                    Intent it = new Intent(Intent.ACTION_SENDTO, uri2);
                    it.putExtra("sms_body", memory_name+"\n"+memory_emotion+"\n"+memory_location+"\n"+memory_text+"\n");
                    startActivity(it);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1618){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                ;
            }
            else{
                Toast.makeText(ShowMemory.this, "İzin sağlamalısınız.", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == 1619){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                ;
            }
            else{
                Toast.makeText(ShowMemory.this, "İzin sağlamalısınız.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
