package serc.mphw.memoryapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MemoryAdapter extends RecyclerView.Adapter<MemoryAdapter.ViewHolder>{
    ArrayList<MemoryBody> memories;
    Context context;
    public MemoryAdapter(ArrayList<MemoryBody> memories, MainActivity activity){
        this.memories = memories;
        this.context = activity;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.memory_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MemoryBody memoryBody = memories.get(position);
        holder.memoryName.setText(memoryBody.getMemoryName());
        String memoryText = memoryBody.getMemoryText();
        if (memoryText.length() <= 40)
            holder.memoryText.setText(memoryText);
        else
            holder.memoryText.setText(memoryText.substring(0,38) + "...");
        holder.memoryEmotion.setText(memoryBody.getMemoryEmotion());
        holder.memoryLocation.setText(memoryBody.getMemoryLocation());
        Date memoryDate = memoryBody.getMemoryDate();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        holder.memoryDate.setText(df.format(memoryDate));


        holder.memorySettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, holder.memorySettings);
                popup.inflate(R.menu.options_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.edit_memory:
                                Intent show_memory_intent = new  Intent(context, EditMemory.class);
                                show_memory_intent.putExtra("memory_name", memoryBody.getMemoryName());
                                show_memory_intent.putExtra("memory_emotion", memoryBody.getMemoryEmotion());
                                show_memory_intent.putExtra("memory_location", memoryBody.getMemoryLocation());
                                show_memory_intent.putExtra("memory_text", memoryBody.getMemoryText());

                                context.startActivity(show_memory_intent);
                                break;
                            case R.id.delete_memory:
                                AlertDialog diaBox = AskOption(holder);
                                diaBox.show();
                                break;
                            case R.id.pwd_memory:
                                if(!(memoryBody.getMemoryPwd().equals("NOPWD\n") || memoryBody.getMemoryPwd().equals("NOPWD"))) {
                                    //Toast.makeText(context, "Bu anı daha önce zaten şifrelenmiş.", Toast.LENGTH_LONG).show();
                                    Toast.makeText(context, memoryBody.getMemoryPwd(), Toast.LENGTH_LONG).show();
                                }
                                else{
                                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                                    final EditText edittext = new EditText(context);
                                    alert.setMessage("Şifreyi girin");
                                    alert.setTitle("Şifre");

                                    alert.setView(edittext);

                                    alert.setPositiveButton("Şifrele", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            String pwdString = edittext.getText().toString();
                                            memoryBody.setMemoryPwd(pwdString);
                                            Toast.makeText(context,"Başarılı bir şekilde şifrelendi.",Toast.LENGTH_SHORT);
                                        }
                                    });

                                    alert.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            ;
                                        }
                                    });

                                    alert.show();
                                }
                                break;
                        }
                        return false;

                    }
                });
                popup.show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(memoryBody.getMemoryPwd().equals("NOPWD\n") || memoryBody.getMemoryPwd().equals("NOPWD") ){

                    Intent show_memory_intent = new  Intent(context, ShowMemory.class);
                    show_memory_intent.putExtra("memory_name", memoryBody.getMemoryName());
                    show_memory_intent.putExtra("memory_emotion", memoryBody.getMemoryEmotion());
                    show_memory_intent.putExtra("memory_location", memoryBody.getMemoryLocation());
                    show_memory_intent.putExtra("memory_date", df.format(memoryBody.getMemoryDate()));
                    show_memory_intent.putExtra("memory_text", memoryBody.getMemoryText());

                    context.startActivity(show_memory_intent);
                }
                else{
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    final EditText edittext = new EditText(context);
                    alert.setMessage("Şifreyi girin");
                    alert.setTitle("Şifre");

                    alert.setView(edittext);

                    alert.setPositiveButton("Onayla", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String pwdString = edittext.getText().toString();
                            if(memoryBody.getMemoryPwd().equals(pwdString) || memoryBody.getMemoryPwd().equals(pwdString+"\n")){
                                Toast.makeText(context,"Şifre onaylandı",Toast.LENGTH_SHORT).show();

                                Intent show_memory_intent = new  Intent(context, ShowMemory.class);
                                show_memory_intent.putExtra("memory_name", memoryBody.getMemoryName());
                                show_memory_intent.putExtra("memory_emotion", memoryBody.getMemoryEmotion());
                                show_memory_intent.putExtra("memory_location", memoryBody.getMemoryLocation());
                                show_memory_intent.putExtra("memory_date", df.format(memoryBody.getMemoryDate()));
                                show_memory_intent.putExtra("memory_text", memoryBody.getMemoryText());

                                context.startActivity(show_memory_intent);
                            }
                            else{
                                Toast.makeText(context,"Girilen şifre yanlış.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    alert.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            ;
                        }
                    });

                    alert.show();
                }

            }
        });

    }
    @Override
    public int getItemCount() {
        return memories.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView memoryName;
        TextView memoryText;
        TextView memoryEmotion;
        TextView memoryLocation;
        TextView memoryDate;

        ImageView memorySettings;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            memoryName = itemView.findViewById(R.id.memory_item_memoryname);
            memoryText = itemView.findViewById(R.id.memory_item_memorytext);
            memoryEmotion = itemView.findViewById(R.id.memory_item_memoryemotion);
            memoryLocation = itemView.findViewById(R.id.memory_item_memorylocation);
            memoryDate = itemView.findViewById(R.id.memory_item_memorydate);

            memorySettings = itemView.findViewById(R.id.memory_item_memorysettings);
        }
    }


    private AlertDialog AskOption(ViewHolder holder){
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(context)
                // set message, title, and icon
                .setTitle("Sil")
                .setMessage("Silmek istediğinize emin misiniz?")

                .setPositiveButton("Sil", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        Toast.makeText(context, holder.memoryName.getText() + " silindi.",Toast.LENGTH_SHORT).show();
                        MainActivity.memoriesList.remove(MainActivity.idxOfMemory((String) holder.memoryName.getText()));
                        MainActivity.mAdapter.notifyDataSetChanged();

                    }

                })
                .setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ;
                    }
                })
                .create();

        return myQuittingDialogBox;
    }
}
