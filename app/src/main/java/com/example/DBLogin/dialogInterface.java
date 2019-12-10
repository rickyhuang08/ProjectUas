package com.example.DBLogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class dialogInterface extends AppCompatActivity {
    private VideoView videoView;
    private MediaController mediaController;
    private ProgressDialog progressDialog;
    private Uri uri;
    private boolean isContinously = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_video);

        Bundle bundle = getIntent().getExtras();
        String link = bundle.getString("link");
        String id = bundle.getString("id");

        videoView = findViewById(R.id.videoViews);
        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        progressDialog = new ProgressDialog(dialogInterface.this);
        progressDialog.setMessage("Buffering video please wait...");
        progressDialog.show();

        uri = Uri.parse(link);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                progressDialog.dismiss();
            }
        });

        initComponent();
    }

    public void initComponent(){
        ImageButton imageButton;
        imageButton = findViewById(R.id.btnClose);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
