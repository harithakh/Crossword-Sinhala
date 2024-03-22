package com.haritha_kh.crosswordsinhala;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        TextView appVersion = findViewById(R.id.app_version);

        // Get the Intent
        Intent intent = getIntent();

        // Retrieve the integer extra from the Intent
        int receivedValue = intent.getIntExtra("intKey", 1);

        if (receivedValue == 1){
            //help
            LinearLayout instructionsLinearLayout = findViewById(R.id.instructions_linear_layout);
            instructionsLinearLayout.setVisibility(View.VISIBLE);
        } else if (receivedValue == 2) {
            //about
            LinearLayout aboutLinearLayout = findViewById(R.id.about_linear_layout);
            aboutLinearLayout.setVisibility(View.VISIBLE);

            TextView privacyPolicyLink = findViewById(R.id.privacy_policy_text_view);
            privacyPolicyLink.setOnClickListener(v -> {
                String url = "http://www.slmobiles.com";
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent1);
            });

            try {
                // Get package info
                PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

                // Get version name
                String versionName = packageInfo.versionName;

                String temp =  "App Version : " + versionName;
                // Set the version info to the TextView
                appVersion.setText(temp);

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }


        }
    }
}