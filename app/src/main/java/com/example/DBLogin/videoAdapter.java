package com.example.DBLogin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.DBLogin.api.videoModel;

import java.util.List;

public class videoAdapter extends RecyclerView.Adapter<videoAdapter.myViewHolder> {
    private Context context;
    private List<videoModel> mList;
//    private final Dialog myDialog;

    public videoAdapter(Context context, List<videoModel> mList){
        this.context = context;
        this.mList = mList;
    }
    @NonNull
    @Override
    public videoAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recycler_video, parent, false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull videoAdapter.myViewHolder holder, int position) {
        final ImageView imageView = holder.image;
        final videoModel movie = mList.get(position);

        holder.judul.setText(movie.getJudul());
        holder.kategori.setText(String.valueOf(movie.getKategori()));
        holder.desc.setText(String.valueOf(movie.getDesc()));
        System.out.println("movie - image " + movie.getImage());
        Glide.with(context)
                .load(movie.getImage())
                .asBitmap()
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("id", movie.getId());
                bundle.putString("link", movie.getVideo());
                Intent intent = new Intent(context, dialogInterface.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtras(bundle);
                context.startActivity(intent);
//
//                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
//                View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_video, null);
//                dialog.setView(dialogView);
//                dialog.setCancelable(true);
//                dialog.setIcon(R.drawable.ic_phone_android_black_24dp);
//                dialog.setTitle("Form Biodata");
//                Log.d("debug", dialog.toString());
//
//                dialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//                dialog.show();
//                myDialog.setContentView(R.layout.dialog_video);
//                VideoView videoView;
//                ImageButton btnCancel;
////
//                btnCancel = (ImageButton) myDialog.findViewById(R.id.btnClose);
////                videoView = myDialog.findViewById(R.id.videoViews);
//
//                btnCancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        myDialog.dismiss();
//
//                    }
//                });
////
//                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                Toast.makeText(context, "myDialog : " + myDialog.toString(), Toast.LENGTH_SHORT).show();
//                myDialog.getWindow();
//                myDialog.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        public TextView judul, kategori, desc;
        public ImageView image;

        private myViewHolder(View itemView) {
            super(itemView);

            judul = itemView.findViewById(R.id.tvJudul);
            kategori = itemView.findViewById(R.id.tvKategori);
            desc = itemView.findViewById(R.id.tvDeskripsi);
            image = (ImageView) itemView.findViewById(R.id.videoLayout);

        }
    }

}
