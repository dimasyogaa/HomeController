package com.yogadimas.homecontroller.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.muddzdev.styleabletoast.StyleableToast;
import com.stealthcopter.networktools.Ping;
import com.stealthcopter.networktools.ping.PingResult;
import com.stealthcopter.networktools.ping.PingStats;
import com.yogadimas.homecontroller.BuildConfig;
import com.yogadimas.homecontroller.R;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final Runnable mRunnable;
    String TAG = MainActivity.class.getSimpleName();
    private ValueEventListener mListener;
    private FirebaseDatabase mDatabase;
    private Firebase mFbRefAll;
    private ProgressDialog Dialog;
    private ToggleButton mTbRelay1, mTbRelay2, mTbRelay3, mTbRelay4, mTbAllRelay, mBtnMenu;
    private TextView mTvVoice, mTvRelay1, mTvRelay2, mTvRelay3, mTvRelay4, mTvRelayAll, mTvTestConn, mTvPing;
    private View mNueFlat1;
    private View mNeuPressed1;
    private View mNueFlat2;
    private View mNeuPressed2;
    private View mNueFlat3;
    private View mNeuPressed3;
    private View mNueFlat4;
    private View mNeuPressed4;
    private View mNueFlatAll;
    private View mNeuPressedAll;
    private View ly_disconnected;
    private Handler mHandler;
    private ImageView mIvTestConn, mIvTestDevice, mIvMenu;
    private long mLastClickTime = 0;

    // Update
    {
        mRunnable = new Runnable() {
            @SuppressLint("SetTextI18n")
            public void run() {
                if (isConnected()) {
                    ly_disconnected.setVisibility(View.GONE);
                    enableActivity(true);

                    Dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            String getTvRelay1 = mTvRelay1.getText().toString();
                            if (getTvRelay1.isEmpty()) {
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(getIntent());
                                overridePendingTransition(0, 0);
                            }

                        }
                    });
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                doPing();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                } else {
                    viewDisconnect();
                }
                mTvPing.setText("");
                MainActivity.this.mHandler.postDelayed(mRunnable, 3000);
            }

        };


    }

    private void appendResultsText(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTvPing.append(text + "\n");
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private void doPing() {
        try {
            String ipAddress = "8.8.8.8";
            if (TextUtils.isEmpty(ipAddress)) {
                appendResultsText("Invalid Ip Address");
                return;
            }

            // Perform a single synchronous ping
            PingResult pingResult;
            try {
                pingResult = Ping.onAddress(ipAddress).setTimeOutMillis(700).doPing();
            } catch (UnknownHostException e) {
                e.printStackTrace();
                appendResultsText(e.getMessage());
                return;
            }

            appendResultsText("Pinging Address: " + pingResult.getAddress().getHostAddress());
            appendResultsText("HostName: " + pingResult.getAddress().getHostName());
            appendResultsText(String.format("%.2f ms", pingResult.getTimeTaken()));

            try {
                Ping.onAddress(ipAddress).setTimeOutMillis(700).setTimes(5).doPing(new Ping.PingListener() {
                    @Override
                    public void onResult(PingResult pingResult) {
                        if (pingResult.isReachable) {
                            try {
                                appendResultsText(String.format("%.2f ms", pingResult.getTimeTaken()));
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            mDatabase.getReference(BuildConfig.CONNECT_APP).setValue(1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        viewConnect();

                                                        Dialog.dismiss();
                                                    }
                                                }
                                            });
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            try {
                                appendResultsText(getString(R.string.timeout));
                                runOnUiThread(new Runnable() {

                                    @SuppressLint("UseCompatLoadingForDrawables")
                                    @Override
                                    public void run() {
                                        try {
                                            mIvTestConn.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_disconnect));
                                            mTvTestConn.setText(R.string.main_help_disconnected);


                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onFinished(PingStats pingStats) {
                        appendResultsText(String.format("Pings: %d, Packets lost: %d",
                                pingStats.getNoPings(), pingStats.getPacketsLost()));
                        appendResultsText(String.format("Min/Avg/Max Time: %.2f/%.2f/%.2f ms",
                                pingStats.getMinTimeTaken(), pingStats.getAverageTimeTaken(), pingStats.getMaxTimeTaken()));

                    }

                    @Override
                    public void onError(Exception e) {
                        // TODO: STUB METHOD
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (ExceptionInInitializerError | Exception e) {
            e.printStackTrace();
        }

    }

    private void enableActivity(Boolean isEnabled) {
        if (!isEnabled) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    //     Proses loading menghubungkan ke firebase
    private void dialogCheck() {
        Dialog.setMessage(getString(R.string.login_main_connecting));
        Dialog.setCancelable(false);
        Dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // menghubungkan firebase database agar bisa diproses pada java class

        mDatabase = FirebaseDatabase.getInstance();

        //menghubungkan id pada xml agar bisa diproses pada java class
        mTbRelay1 = findViewById(R.id.tb_relay1);
        mTbRelay2 = findViewById(R.id.tb_relay2);
        mTbRelay3 = findViewById(R.id.tb_relay3);
        mTbRelay4 = findViewById(R.id.tb_relay4);
        mTbAllRelay = findViewById(R.id.tb_all_relay);

        mTvVoice = findViewById(R.id.tv_voice);
        mTvRelay1 = findViewById(R.id.tv_relay_1);
        mTvRelay2 = findViewById(R.id.tv_relay_2);
        mTvRelay3 = findViewById(R.id.tv_relay_3);
        mTvRelay4 = findViewById(R.id.tv_relay_4);
        mTvRelayAll = findViewById(R.id.tv_relay_all);

        mNueFlat1 = findViewById(R.id.nue_flat_1);
        mNeuPressed1 = findViewById(R.id.neu_pressed_1);
        mNueFlat2 = findViewById(R.id.nue_flat_2);
        mNeuPressed2 = findViewById(R.id.neu_pressed_2);
        mNueFlat3 = findViewById(R.id.nue_flat_3);
        mNeuPressed3 = findViewById(R.id.neu_pressed_3);
        mNueFlat4 = findViewById(R.id.nue_flat_4);
        mNeuPressed4 = findViewById(R.id.neu_pressed_4);
        mNueFlatAll = findViewById(R.id.nue_flat_all);
        mNeuPressedAll = findViewById(R.id.neu_pressed_all);

        mTvTestConn = findViewById(R.id.tv_test_conn);
        mIvTestConn = findViewById(R.id.iv_test_conn);
        mIvTestDevice = findViewById(R.id.iv_test_dev);

        mIvMenu = findViewById(R.id.iv_menu);
        mBtnMenu = findViewById(R.id.btn_menu);

        View mBtnVoiceMode = findViewById(R.id.btn_voice_mode);

        ly_disconnected = findViewById(R.id.help_ly_disconnected);

        mTvPing = findViewById(R.id.tv_ping);

        mFbRefAll = new Firebase(BuildConfig.BASE_URL_FIREBASE);

        mTbRelay1.setOnClickListener(this);
        mTbRelay2.setOnClickListener(this);
        mTbRelay3.setOnClickListener(this);
        mTbRelay4.setOnClickListener(this);
        mTbAllRelay.setOnClickListener(this);

        mBtnMenu.setOnClickListener(this);


        mBtnVoiceMode.setOnClickListener(this);

        Dialog = new ProgressDialog(MainActivity.this);

        mNueFlat1.setVisibility(View.VISIBLE);
        mNeuPressed1.setVisibility(View.GONE);
        mNueFlat2.setVisibility(View.VISIBLE);
        mNeuPressed2.setVisibility(View.GONE);
        mNueFlat3.setVisibility(View.VISIBLE);
        mNeuPressed3.setVisibility(View.GONE);
        mNueFlat4.setVisibility(View.VISIBLE);
        mNeuPressed4.setVisibility(View.GONE);
        mNueFlatAll.setVisibility(View.VISIBLE);
        mNeuPressedAll.setVisibility(View.GONE);

        mIvTestDevice.setVisibility(View.INVISIBLE);


        this.mHandler = new Handler();
        this.mHandler.postDelayed(mRunnable, 3000);
        dialogCheck();
        connectApp();
        checkConnected();
        getTbAllRelay();


    }

    private void connectApp() {
        mDatabase.getReference(BuildConfig.CONNECT_APP).setValue(1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    viewConnect();
                    Dialog.dismiss();
                }
            }
        });

    }

    //test koneksi
    private void checkConnected() {
        if (isConnected()) {
            inProcess();
        } else {

            try {
                viewDisconnect();

            } catch (Exception e) {
                Log.e("Connectivity Exception", Objects.requireNonNull(e.getMessage()));
            }
        }


    }

    public boolean isConnected() {
        boolean connected;
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", Objects.requireNonNull(e.getMessage()));
        }
        return false;
    }

    // UI state koneksi
    private void viewConnect() {
        mIvTestConn.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_connect));
        mTvTestConn.setText(R.string.main_help_connected);
    }

    private void viewDisconnect() {
        mIvTestConn.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_disconnect));
        mTvTestConn.setText(R.string.main_help_disconnected);
        ly_disconnected.setVisibility(View.VISIBLE);
        enableActivity(false);
    }

    // Pengkondisian toggle di klik
    public void onClick(View v) {
        if (v.getId() == R.id.tb_relay1) {
            relay1();
        } else if (v.getId() == R.id.tb_relay2) {
            relay2();
        } else if (v.getId() == R.id.tb_relay3) {
            relay3();
        } else if (v.getId() == R.id.tb_relay4) {
            relay4();
        } else if (v.getId() == R.id.tb_all_relay) {
            setTbAllRelay();
        } else if (v.getId() == R.id.btn_menu) {
            showMenu();
        } else if (v.getId() == R.id.btn_voice_mode) {
            getSpeechInput();
        }
    }

    // menu
    private void showMenu() {
        if (mBtnMenu.isChecked()) {
            inflate();
        } else {
            mIvMenu.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_menu));
        }
    }

    private void inflate() {

        PopupMenu popup = new PopupMenu(MainActivity.this, mBtnMenu);

        popup.getMenuInflater().inflate(R.menu.menu_option, popup.getMenu());

        setTheme(R.style.MenuTheme);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_timer:
                        Intent goToTimer = new Intent(MainActivity.this, TimerActivity.class);
                        startActivity(goToTimer);
                        return true;
                    case R.id.item_language:
                        Intent goToLanguage = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                        startActivity(goToLanguage);
                        return true;
                    case R.id.item_help:
                        Intent goToHelp = new Intent(MainActivity.this, HelpActivity.class);
                        startActivity(goToHelp);
                        return true;
                    case R.id.item_contact_us:
                        Intent goToContact = new Intent(MainActivity.this, ContactActivity.class);
                        startActivity(goToContact);
                        return true;
                }
                return true;
            }
        });

        popup.setGravity(5);
        popup.show();
        mIvMenu.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_menu_clicked));
        mBtnMenu.setChecked(true);
        popup.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                mIvMenu.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_menu));
                mBtnMenu.setChecked(false);
            }
        });


    }

    //Toast method
    private void onSuccessToast(String string) {
        
    }

    private void offSuccessToast(String string) {
        StyleableToast.makeText(MainActivity.this, getString(R.string.main_toast_switch) + " " +
                string + " " + getString(R.string.main_toast_success_nonactive), Toast.LENGTH_SHORT, R.style.ToastTheme).show();
    }

    // eksekusi ke Firebase
    private void setTbAllRelay() {
        if (mTbAllRelay.isChecked()) {
            onRelay1();
            onRelay2();
            onRelay3();
            onRelay4();
        } else {
            offRelay1();
            offRelay2();
            offRelay3();
            offRelay4();

        }

    }

    private void relay1() {
        if (mTbRelay1.isChecked()) {
            onRelay1();
        } else {
            offRelay1();

        }
    }

    private void relay2() {
        if (mTbRelay2.isChecked()) {
            onRelay2();

        } else {
            offRelay2();

        }
    }

    private void relay3() {
        if (mTbRelay3.isChecked()) {
            onRelay3();

        } else {
            offRelay3();

        }
    }

    private void relay4() {
        if (mTbRelay4.isChecked()) {
            onRelay4();

        } else {
            offRelay4();

        }
    }

    private void onRelay1() {
        mTbRelay1.setEnabled(false);
        mDatabase.getReference(BuildConfig.RELAY1).setValue(1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    isChecked1();
                }
                mTbRelay1.setEnabled(true);
                getTbAllRelay();

            }
        });
    }

    private void offRelay1() {
        mTbRelay1.setEnabled(false);
        mDatabase.getReference(BuildConfig.RELAY1).setValue(0).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    notChecked1();
                }
                mTbRelay1.setEnabled(true);
                getTbAllRelay();

            }
        });

    }

    private void onRelay2() {
        mTbRelay2.setEnabled(false);
        mDatabase.getReference(BuildConfig.RELAY2).setValue(1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    isChecked2();

                }
                mTbRelay2.setEnabled(true);
                getTbAllRelay();
            }
        });
    }

    private void offRelay2() {
        mTbRelay2.setEnabled(false);
        mDatabase.getReference(BuildConfig.RELAY2).setValue(0).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    notChecked2();

                }
                mTbRelay2.setEnabled(true);
                getTbAllRelay();
            }
        });
    }

    private void onRelay3() {
        mTbRelay3.setEnabled(false);
        mDatabase.getReference(BuildConfig.RELAY3).setValue(1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    isChecked3();

                }
                mTbRelay3.setEnabled(true);
                getTbAllRelay();
            }
        });
    }

    private void offRelay3() {
        mTbRelay3.setEnabled(false);
        mDatabase.getReference(BuildConfig.RELAY3).setValue(0).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    notChecked3();

                }
                mTbRelay3.setEnabled(true);
                getTbAllRelay();
            }
        });
    }

    private void onRelay4() {
        mTbRelay4.setEnabled(false);
        mDatabase.getReference(BuildConfig.RELAY4).setValue(1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    isChecked4();
                }
                mTbRelay4.setEnabled(true);
                getTbAllRelay();
            }
        });
    }

    private void offRelay4() {
        mTbRelay4.setEnabled(false);
        mDatabase.getReference(BuildConfig.RELAY4).setValue(0).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    notChecked4();
                }
                mTbRelay4.setEnabled(true);
                getTbAllRelay();
            }
        });
    }

    // UI state toggle
    private void getTbAllRelay() {

        if (mTbRelay1.isChecked() && mTbRelay2.isChecked() && mTbRelay3.isChecked() && mTbRelay4.isChecked()) {

            isCheckedAll();
        } else {
            notCheckedAll();
        }
    }

    private void isChecked1() {
        mTvRelay1.setText(R.string.main_on);
        mTbRelay1.setChecked(true);
        mNeuPressed1.setVisibility(View.VISIBLE);
        mNueFlat1.setVisibility(View.GONE);
    }

    private void notChecked1() {
        mTvRelay1.setText(R.string.main_off);
        mTbRelay1.setChecked(false);
        mNeuPressed1.setVisibility(View.GONE);
        mNueFlat1.setVisibility(View.VISIBLE);
    }

    private void isChecked2() {
        mTvRelay2.setText(R.string.main_on);
        mTbRelay2.setChecked(true);
        mNeuPressed2.setVisibility(View.VISIBLE);
        mNueFlat2.setVisibility(View.GONE);
    }

    private void notChecked2() {
        mTvRelay2.setText(R.string.main_off);
        mTbRelay2.setChecked(false);
        mNeuPressed2.setVisibility(View.GONE);
        mNueFlat2.setVisibility(View.VISIBLE);
    }

    private void isChecked3() {
        mTvRelay3.setText(R.string.main_on);
        mTbRelay3.setChecked(true);
        mNeuPressed3.setVisibility(View.VISIBLE);
        mNueFlat3.setVisibility(View.GONE);
    }

    private void notChecked3() {
        mTvRelay3.setText(R.string.main_off);
        mTbRelay3.setChecked(false);
        mNeuPressed3.setVisibility(View.GONE);
        mNueFlat3.setVisibility(View.VISIBLE);
    }

    private void isChecked4() {
        mTvRelay4.setText(R.string.main_on);
        mTbRelay4.setChecked(true);
        mNeuPressed4.setVisibility(View.VISIBLE);
        mNueFlat4.setVisibility(View.GONE);
    }

    private void notChecked4() {
        mTvRelay4.setText(R.string.main_off);
        mTbRelay4.setChecked(false);
        mNeuPressed4.setVisibility(View.GONE);
        mNueFlat4.setVisibility(View.VISIBLE);
    }

    private void isCheckedAll() {
        mTvRelayAll.setText(R.string.main_on);
        mTbAllRelay.setChecked(true);
        mNeuPressedAll.setVisibility(View.VISIBLE);
        mNueFlatAll.setVisibility(View.GONE);
    }

    private void notCheckedAll() {
        mTvRelayAll.setText(R.string.main_off);
        mTbAllRelay.setChecked(false);
        mNeuPressedAll.setVisibility(View.GONE);
        mNueFlatAll.setVisibility(View.VISIBLE);
    }

    // Proses pengambilan data dari firebase realtime
    private void inProcess() {

        //loading koneksi ke database
        dialogCheck();

        //proses realtime menggunakan event listener
        mListener = mFbRefAll.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //setiap ada perubahan data akan diambil datanya

                //ambil nilai dari firebase
                String strRelay1 = dataSnapshot.child(BuildConfig.RELAY1).getValue(String.class);
                String strRelay2 = dataSnapshot.child(BuildConfig.RELAY2).getValue(String.class);
                String strRelay3 = dataSnapshot.child(BuildConfig.RELAY3).getValue(String.class);
                String strRelay4 = dataSnapshot.child(BuildConfig.RELAY4).getValue(String.class);
                String strConnDev = dataSnapshot.child(BuildConfig.CONNECT_DEVICE).getValue(String.class);
                String strConnFire = dataSnapshot.child(BuildConfig.CONNECT_FIREBASE).getValue(String.class);

                if (strRelay1.equals("1")) {
                    isChecked1();

                } else {
                    notChecked1();

                }

                if (strRelay2.equals("1")) {
                    isChecked2();

                } else {
                    notChecked2();

                }
                if (strRelay3.equals("1")) {
                    isChecked3();

                } else {
                    notChecked3();

                }
                if (strRelay4.equals("1")) {
                    isChecked4();
                } else {
                    notChecked4();
                }


                if (strRelay1.equals("1") && strRelay2.equals("1") && strRelay3.equals("1") && strRelay4.equals("1")) {
                    isCheckedAll();
                } else {
                    notCheckedAll();
                }

                if (strConnDev.equals("2") && strConnFire.equals("2")) {
                    mIvTestDevice.setVisibility(View.VISIBLE);
                    mDatabase.getReference(BuildConfig.CONNECT_DEVICE).setValue(0);
                    mDatabase.getReference(BuildConfig.CONNECT_FIREBASE).setValue(0);
                } else {
                    mIvTestDevice.setVisibility(View.INVISIBLE);
                }

                Dialog.dismiss();

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                throw firebaseError.toException(); // don't ignore errors

            }
        });
    }

    // Voice mode
    public void getSpeechInput() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "in");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);

        } else {
            Toast.makeText(this, R.string.main_dont_support_voice_mode, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                mTvVoice.setText(Objects.requireNonNull(result).get(0));
                mTvVoice.setVisibility(View.INVISIBLE);
                String relayVoice = mTvVoice.getText().toString().toLowerCase();
                switch (relayVoice) {
                    case "saklar 1 aktif":
                    case "sakelar 1 aktif":
                    case "saklar satu aktif":
                    case "sakelar satu aktif":
                        mDatabase.getReference(BuildConfig.RELAY1).setValue(1);
                        onSuccessToast("1");
                        break;
                    case "saklar 1 nonaktif":
                    case "sakelar 1 nonaktif":
                    case "saklar 1 non aktif":
                    case "sakelar 1 non aktif":
                    case "saklar satu nonaktif":
                    case "sakelar satu nonaktif":
                        mDatabase.getReference(BuildConfig.RELAY1).setValue(0);
                        offSuccessToast("1");
                        break;
                    case "saklar 2 aktif":
                    case "sakelar 2 aktif":
                    case "saklar dua aktif":
                    case "sakelar dua aktif":
                        mDatabase.getReference(BuildConfig.RELAY2).setValue(1);
                        onSuccessToast("2");
                        break;
                    case "saklar 2 nonaktif":
                    case "sakelar 2 nonaktif":
                    case "saklar 2 non aktif":
                    case "sakelar 2 non aktif":
                    case "saklar dua nonaktif":
                    case "sakelar dua nonaktif":
                        mDatabase.getReference(BuildConfig.RELAY2).setValue(0);
                        offSuccessToast("2");
                        break;
                    case "saklar 3 aktif":
                    case "sakelar 3 aktif":
                    case "saklar tiga aktif":
                    case "sakelar tiga aktif":
                        mDatabase.getReference(BuildConfig.RELAY3).setValue(1);
                        onSuccessToast("3");
                        break;
                    case "saklar 3 nonaktif":
                    case "sakelar 3 nonaktif":
                    case "saklar 3 non aktif":
                    case "sakelar 3 non aktif":
                    case "saklar tiga nonaktif":
                    case "sakelar tiga nonaktif":
                        mDatabase.getReference(BuildConfig.RELAY3).setValue(0);
                        offSuccessToast("3");
                        break;
                    case "saklar 4 aktif":
                    case "sakelar 4 aktif":
                    case "saklar empat aktif":
                    case "sakelar empat aktif":
                        mDatabase.getReference(BuildConfig.RELAY4).setValue(1);
                        onSuccessToast("4");
                        break;
                    case "saklar 4 nonaktif":
                    case "sakelar 4 nonaktif":
                    case "saklar 4 non aktif":
                    case "sakelar 4 non aktif":
                    case "saklar empat nonaktif":
                    case "sakelar empat nonaktif":
                        mDatabase.getReference(BuildConfig.RELAY4).setValue(0);
                        offSuccessToast("4");
                        break;
                    case "semua saklar aktif":
                    case "semua sakelar aktif":
                        getReferenceDatabase(1);
                        isCheckedAll();
                        StyleableToast.makeText(MainActivity.this, getString(R.string.main_toast_all_active), Toast.LENGTH_SHORT, R.style.ToastTheme).show();
                        break;
                    case "semua saklar nonaktif":
                    case "semua sakelar nonaktif":
                    case "semua saklar non aktif":
                    case "semua sakelar non aktif":
                        getReferenceDatabase(0);
                        notCheckedAll();
                        StyleableToast.makeText(MainActivity.this, getString(R.string.main_toast_all_nonactive), Toast.LENGTH_SHORT, R.style.ToastTheme).show();
                        break;
                    default:
                        StyleableToast.makeText(MainActivity.this, getString(R.string.main_toast_unknown_command), Toast.LENGTH_SHORT, R.style.ToastTheme).show();
                        break;
                }
            }
        }
    }

    private void getReferenceDatabase(Integer i) {
        mDatabase.getReference(BuildConfig.RELAY1).setValue(i);
        mDatabase.getReference(BuildConfig.RELAY2).setValue(i);
        mDatabase.getReference(BuildConfig.RELAY3).setValue(i);
        mDatabase.getReference(BuildConfig.RELAY4).setValue(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIvTestDevice.getVisibility() == View.VISIBLE) {
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        }
        dialogCheck();
        MainActivity.this.mHandler.postDelayed(mRunnable, 3000);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        connectApp();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Dialog.dismiss();
        mHandler.removeCallbacks(mRunnable);


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Dialog != null) {
            Dialog.dismiss();
        }

    }

}