package com.yogadimas.homecontroller.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.yogadimas.homecontroller.BuildConfig;
import com.yogadimas.homecontroller.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private String mGetCode;
    private Firebase mFbRefLogin;
    private TextView mTvCode;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFbRefLogin = new Firebase(BuildConfig.BASE_URL_FIREBASE_QR_CODE);

        mTvCode = findViewById(R.id.tv_code);
        View mBtnScan = findViewById(R.id.btn_scan);

        sharedPreferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        String firstTime = sharedPreferences.getString("FirstTimeInstall", "");
        if (firstTime.equals("Yes")) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        mBtnScan.setOnClickListener(this);
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
                    Toast.makeText(LoginActivity.this,
                            R.string.login_contact_us_please_wait_a_moment, Toast.LENGTH_SHORT).show();
                    startActivity(Intent.createChooser(emailIntent, getString(R.string.login_contact_us_send_email)));
                    Log.i("Finished sending email", "");
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(LoginActivity.this,
                            R.string.login_contact_us_catch_no_email, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_scan) {
            initIntentIntegrator();
        }
    }

    private void initIntentIntegrator() {

        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setCameraId(0);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.initiateScan();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, R.string.login_no_result_found, Toast.LENGTH_SHORT).show();
            } else {

                try {

                    JSONObject object = new JSONObject(result.getContents());

                    mTvCode.setVisibility(View.INVISIBLE);
                    mTvCode.setText(object.getString(BuildConfig.KEY_QR_CODE));
                    mGetCode = mTvCode.getText().toString();

                    mFbRefLogin.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String mCode = dataSnapshot.getValue(String.class);

                            if (mGetCode.equals(mCode)) {
                                mTvCode.setVisibility(View.VISIBLE);
                                mTvCode.setText(R.string.login_main_connecting);

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("FirstTimeInstall", "Yes");
                                editor.apply();

                                Intent goToMainActivity = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(goToMainActivity);
                                finish();


                            } else {
                                mTvCode.setVisibility(View.VISIBLE);
                                mTvCode.setText(R.string.login_qr_code_doesnt_match);
                            }
                        }


                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            Toast.makeText(LoginActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();

                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



}