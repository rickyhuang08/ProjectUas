package com.example.DBLogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DBLogin.api.videoModel;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class showVideo extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<videoModel> movieList;
    private RecyclerView.Adapter adapter;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_video);
        mContext = this;
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerShowVideo);
        actionBarDrawerToggle = new ActionBarDrawerToggle(showVideo.this, drawerLayout,R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView)findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);

        initComponents();
        listData();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if(id == R.id.home) {
            Toast.makeText(this, "This is Home", Toast.LENGTH_SHORT).show();
            Bundle bundle = getIntent().getExtras();
            String data = bundle.getString("data");
            String username = bundle.getString("username");

            bundle = new Bundle();
            bundle.putString("data", data);
            bundle.putString("username", username);
            Intent intent = new Intent(this, showVideo.class);
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        if(id == R.id.account){
            Toast.makeText(this, "This is Account", Toast.LENGTH_SHORT).show();
            Bundle bundle = getIntent().getExtras();
            String data = bundle.getString("data");
            String username = bundle.getString("username");

            bundle = new Bundle();
            bundle.putString("data", data);
            bundle.putString("username", username);
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        if(id == R.id.logout){
            Toast.makeText(this, "Log Out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(mContext, com.example.DBLogin.LoginActivity.class)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
        return false;
    }

    public void initComponents(){
        recyclerView = findViewById(R.id.recyclerView);
        movieList = new ArrayList<>();
        adapter = new videoAdapter(getApplicationContext(),movieList);

        linearLayoutManager = new LinearLayoutManager(this);
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);
    }

    public void listData(){
        Bundle bundle = getIntent().getExtras();
        String data = bundle.getString("data");
        try {
            JSONArray jsonArray = new JSONArray(data);
            for(int i = 0; i<jsonArray.length(); i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                System.out.println("image "+obj.getString("image"));
                movieList.add(new videoModel(
                        obj.getString("id"),
                        obj.getString("judul"),
                        obj.getString("kategory_name"),
                        obj.getString("link"),
                        obj.getString("deskripsi"),
                        obj.getString("image")


                ));

                videoAdapter adapters = new videoAdapter(showVideo.this, movieList);
                recyclerView.setAdapter(adapter);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
