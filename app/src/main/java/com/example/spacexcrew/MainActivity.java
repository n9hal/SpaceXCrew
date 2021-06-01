package com.example.spacexcrew;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.spacexcrew.CrewDB.Crew;
import com.example.spacexcrew.CrewDB.CrewDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Crew> CrewList;
    Adapter adapter;
    FloatingActionButton btnDelete,btnFloat1, btnRefresh;
    Animation close_anim,open_anim,submenu_show,submenu_close;
    boolean isOpen = false;
    RecyclerView recyclerView;
    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDelete = findViewById(R.id.btnFloatDelete);
        btnFloat1 = findViewById(R.id.btnFloat1);
        btnRefresh = findViewById(R.id.btnFloatRefresh);
        recyclerView = findViewById(R.id.recyclerView);
        open_anim = AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim);
        close_anim = AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim);
        submenu_show = AnimationUtils.loadAnimation(this, R.anim.submenu_show_anim);
        submenu_close = AnimationUtils.loadAnimation(this, R.anim.submenu_close_anim);
        if (setUpDB().dao().checkCrew() == 0) {
            api_Client();
        }
        setUpRecycler();
        this.btnFloat1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.animation();
            }
        });
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // disable button for 3000 millis
                btnRefresh.setEnabled(false);
                btnRefresh.postDelayed(() -> btnRefresh.setEnabled(true), 2000);

                // if the crew table is empty fill it.
                if(setUpDB().dao().checkCrew() == 0) {
                    api_Client();
                } else {
                    Toast.makeText(MainActivity.this, "Already updated", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // delete all data from room db
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnDelete.setEnabled(false);
                btnDelete.postDelayed(() -> btnDelete.setEnabled(true), 2000);

                setUpDB().dao().deleteAll();
                CrewList.clear();
                adapter.notifyDataSetChanged();
            }
        });

    }
    private void animation() {
        if (isOpen) {
            btnFloat1.startAnimation(close_anim);
            btnRefresh.startAnimation(submenu_close);
            btnDelete.startAnimation(submenu_close);
            btnRefresh.setClickable(false);
            btnRefresh.setClickable(false);
            isOpen = false;
        } else {
            btnFloat1.startAnimation(open_anim);
            btnRefresh.startAnimation(submenu_show);
            btnDelete.startAnimation(submenu_show);
            btnRefresh.setClickable(true);
            btnRefresh.setClickable(true);
            isOpen = true;
        }
    }

    public CrewDB setUpDB() {
        CrewDB database = Room.databaseBuilder(
                MainActivity.this, CrewDB.class, "CrewDatabase")
                .allowMainThreadQueries().build();
        return database;
    }

    public void api_Client() {
        String api_URL="https://api.spacexdata.com/v4/crew";
        requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new  JsonArrayRequest(Request.Method.GET,
                api_URL,
                null,
                new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jSONArray) {
                for (int i = 0; i < jSONArray.length(); i++) {
                    try {
                        JSONObject jSONObject = jSONArray.getJSONObject(i);
                        String name = jSONObject.get("name").toString();
                        String agency = jSONObject.get("agency").toString();
                        String image = jSONObject.get("image").toString();
                        String wikipedia = jSONObject.get("wikipedia").toString();
                        String status = jSONObject.get("status").toString();
                        Crew crew = new Crew(name, agency, image, wikipedia, status);

                        setUpDB().dao().crewInsertion(crew);
                        setUpRecycler();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity.this, "API Error",Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }
    public void setUpRecycler() {
        CrewList = new ArrayList();
        List<Crew> allCrew = setUpDB().dao().getAllCrew();
        CrewList = allCrew;
        adapter = new Adapter(this, allCrew);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        recyclerView.setAdapter(this.adapter);
        adapter.notifyDataSetChanged();
    }
}