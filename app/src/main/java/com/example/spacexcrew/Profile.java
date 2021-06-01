package com.example.spacexcrew;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.spacexcrew.CrewDB.Crew;


public class Profile extends AppCompatActivity {
    Button btnWiki;
    Crew crew = null;
    ImageView imgProfile;
    ProgressBar progressBar;
    TextView tvAgency;
    TextView tvName;
    TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        tvName = findViewById(R.id.tvName);
        tvAgency = findViewById(R.id.tvAgency);
        tvStatus = findViewById(R.id.tvStatus);
        btnWiki = findViewById(R.id.btnWIKI);
        imgProfile = findViewById(R.id.imageView);


        Object obj = getIntent().getSerializableExtra("detail");
        if(obj instanceof Crew){
            crew = (Crew) obj;
        }
        Glide.with(getApplicationContext()).load(crew.getImage()).apply(new RequestOptions().override(1000, 1000)).into(imgProfile);
        TextView textView = tvName;
        textView.setText(crew.getName() + ", ");
        tvAgency.setText(crew.getAgency());
        tvStatus.setText(crew.getStatus());
        btnWiki.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String url = crew.getWikipedia();
                Uri web = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, web);
                    startActivity(intent);
            }
        });


    }
}