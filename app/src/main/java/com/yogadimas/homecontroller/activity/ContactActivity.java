package com.yogadimas.homecontroller.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yogadimas.homecontroller.BuildConfig;
import com.yogadimas.homecontroller.R;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        View mView = findViewById(R.id.btn_email);

        mView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("IntentReset")
            @Override
            public void onClick(View v) {
                String[] TO = {BuildConfig.BASE_URL_EMAIL_US};
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");

                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hi Home Controller!");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");

                try {
                    Toast.makeText(ContactActivity.this,
                            R.string.login_contact_us_please_wait_a_moment, Toast.LENGTH_SHORT).show();
                    startActivity(Intent.createChooser(emailIntent, getString(R.string.login_contact_us_send_email)));
                    Log.i("Finished sending email", "");
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ContactActivity.this,
                            R.string.login_contact_us_catch_no_email, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}