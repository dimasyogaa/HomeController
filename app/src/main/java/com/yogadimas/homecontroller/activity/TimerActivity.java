package com.yogadimas.homecontroller.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.muddzdev.styleabletoast.StyleableToast;
import com.yogadimas.homecontroller.MyService;
import com.yogadimas.homecontroller.R;
import com.yogadimas.homecontroller.TimePickerFragment;
import com.yogadimas.homecontroller.receiver.switch1.MyReceiverOff1;
import com.yogadimas.homecontroller.receiver.switch1.MyReceiverOn1;
import com.yogadimas.homecontroller.receiver.switch2.MyReceiverOff2;
import com.yogadimas.homecontroller.receiver.switch2.MyReceiverOn2;
import com.yogadimas.homecontroller.receiver.switch3.MyReceiverOff3;
import com.yogadimas.homecontroller.receiver.switch3.MyReceiverOn3;
import com.yogadimas.homecontroller.receiver.switch4.MyReceiverOff4;
import com.yogadimas.homecontroller.receiver.switch4.MyReceiverOn4;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TimerActivity extends AppCompatActivity implements View.OnClickListener, TimePickerFragment.DialogTimeListener {
    //Switch
    public static final String SWITCH_TIMER = "switch_timer";
    public static final String SWITCH1 = "switch1";
    public static final String SWITCH2 = "switch2";
    public static final String SWITCH3 = "switch3";
    public static final String SWITCH4 = "switch4";
    public static final String SWITCH_TIMER_KEY = "switch_timer_key";
    public static final String SWITCH1_KEY = "switch1_key";
    public static final String SWITCH2_KEY = "switch2_key";
    public static final String SWITCH3_KEY = "switch3_key";
    public static final String SWITCH4_KEY = "switch4_key";
    public static final String SWITCH_TIMER_VALUE = "switch_timer_value";
    public static final String SWITCH1_VALUE = "switch1_value";
    public static final String SWITCH2_VALUE = "switch2_value";
    public static final String SWITCH3_VALUE = "switch3_value";
    public static final String SWITCH4_VALUE = "switch4_value";
    // TextView
    public static final String ON1 = "on1";
    public static final String OFF1 = "on1";
    public static final String ON2 = "on2";
    public static final String OFF2 = "off2";
    public static final String ON3 = "on3";
    public static final String OFF3 = "off3";
    public static final String ON4 = "on4";
    public static final String OFF4 = "off4";
    public static final String ON1_KEY = "on1_key";
    public static final String OFF1_KEY = "off1_key";
    public static final String ON2_KEY = "on2_key";
    public static final String OFF2_KEY = "off2_key";
    public static final String ON3_KEY = "on3_key";
    public static final String OFF3_KEY = "off3_key";
    public static final String ON4_KEY = "on4_key";
    public static final String OFF4_KEY = "off4_key";
    //TimePicker
    private final static String TIME_PICKER_ON_1 = "time_on1";
    private final static String TIME_PICKER_OFF_1 = "time_off1";
    private final static String TIME_PICKER_ON_2 = "time_on2";
    private final static String TIME_PICKER_OFF_2 = "time_off2";
    private final static String TIME_PICKER_ON_3 = "time_on3";
    private final static String TIME_PICKER_OFF_3 = "time_off3";
    private final static String TIME_PICKER_ON_4 = "time_on4";
    private final static String TIME_PICKER_OFF_4 = "time_off4";
    private final Runnable mRunnable;
    private TimePickerFragment timePickerFragmentRepeat;

    //Switch Timer
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch mSwitchTimer;
    private TextView mTvStateSwitchTimer;
    private SharedPreferences mSpSwitchTimer;
    private ConstraintLayout mLySwitchAllTimer;

    //Switch 1
    private SharedPreferences mSpSwitch1, mSaveOn1, mSaveOff1;
    private MyReceiverOn1 receiverOn1;
    private MyReceiverOff1 receiverOff1;
    private View mBtnClearOn1, mBtnClearOff1, mBtnSwitchOn1, mBtnSwitchOff1;
    private TextView mTvSwitchOn1, mTvSwitchOff1, mTvStateSwitch1;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch mSwitch1;

    //Switch 2
    private SharedPreferences mSpSwitch2, mSaveOn2, mSaveOff2;
    private MyReceiverOn2 receiverOn2;
    private MyReceiverOff2 receiverOff2;
    private View mBtnClearOn2, mBtnClearOff2, mBtnSwitchOn2, mBtnSwitchOff2;
    private TextView mTvSwitchOn2, mTvSwitchOff2, mTvStateSwitch2;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch mSwitch2;

    //Switch 3
    private SharedPreferences mSpSwitch3, mSaveOn3, mSaveOff3;
    private MyReceiverOn3 receiverOn3;
    private MyReceiverOff3 receiverOff3;
    private View mBtnClearOn3, mBtnClearOff3, mBtnSwitchOn3, mBtnSwitchOff3;
    private TextView mTvSwitchOn3, mTvSwitchOff3, mTvStateSwitch3;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch mSwitch3;

    //Switch 4
    private SharedPreferences mSpSwitch4, mSaveOn4, mSaveOff4;
    private MyReceiverOn4 receiverOn4;
    private MyReceiverOff4 receiverOff4;
    private View mBtnClearOn4, mBtnClearOff4, mBtnSwitchOn4, mBtnSwitchOff4;
    private TextView mTvSwitchOn4, mTvSwitchOff4, mTvStateSwitch4;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch mSwitch4;


    private long mLastClickTime = 0;
    private Handler mHandler;

    {
        mRunnable = new Runnable() {
            @SuppressLint("SetTextI18n")
            public void run() {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    mBtnSwitchOff1.setEnabled(true);
                                    mBtnSwitchOn1.setEnabled(true);
                                    mBtnSwitchOff2.setEnabled(true);
                                    mBtnSwitchOn2.setEnabled(true);
                                    mBtnSwitchOff3.setEnabled(true);
                                    mBtnSwitchOn3.setEnabled(true);
                                    mBtnSwitchOff4.setEnabled(true);
                                    mBtnSwitchOn4.setEnabled(true);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();


                TimerActivity.this.mHandler.postDelayed(mRunnable, 1000);
            }

        };


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        this.mHandler = new Handler();
        this.mHandler.postDelayed(mRunnable, 1000);

        //Time picker frament
        timePickerFragmentRepeat = new TimePickerFragment();

        //Back
        ImageView mBtnBack = findViewById(R.id.btn_back);

        //Switch Timer
        mSwitchTimer = findViewById(R.id.sw_timer);
        mTvStateSwitchTimer = findViewById(R.id.tv_state_switch_timer);
        mLySwitchAllTimer = findViewById(R.id.ly_switch_all);

        //Switch 1
        mBtnSwitchOn1 = findViewById(R.id.btn_switch_on_1);
        mBtnSwitchOff1 = findViewById(R.id.btn_switch_off_1);
        mTvSwitchOn1 = findViewById(R.id.tv_switch_on_1);
        mTvSwitchOff1 = findViewById(R.id.tv_switch_off_1);
        mTvStateSwitch1 = findViewById(R.id.tv_state_switch1);
        mBtnClearOn1 = findViewById(R.id.btn_clear_on_1);
        mBtnClearOff1 = findViewById(R.id.btn_clear_off_1);
        mSwitch1 = findViewById(R.id.sw_1);

        //Switch 2
        mBtnSwitchOn2 = findViewById(R.id.btn_switch_on_2);
        mBtnSwitchOff2 = findViewById(R.id.btn_switch_off_2);
        mTvSwitchOn2 = findViewById(R.id.tv_switch_on_2);
        mTvSwitchOff2 = findViewById(R.id.tv_switch_off_2);
        mTvStateSwitch2 = findViewById(R.id.tv_state_switch2);
        mBtnClearOn2 = findViewById(R.id.btn_clear_on_2);
        mBtnClearOff2 = findViewById(R.id.btn_clear_off_2);
        mSwitch2 = findViewById(R.id.sw_2);

        //Switch 3
        mBtnSwitchOn3 = findViewById(R.id.btn_switch_on_3);
        mBtnSwitchOff3 = findViewById(R.id.btn_switch_off_3);
        mTvSwitchOn3 = findViewById(R.id.tv_switch_on_3);
        mTvSwitchOff3 = findViewById(R.id.tv_switch_off_3);
        mTvStateSwitch3 = findViewById(R.id.tv_state_switch3);
        mBtnClearOn3 = findViewById(R.id.btn_clear_on_3);
        mBtnClearOff3 = findViewById(R.id.btn_clear_off_3);
        mSwitch3 = findViewById(R.id.sw_3);

        //Switch 4
        mBtnSwitchOn4 = findViewById(R.id.btn_switch_on_4);
        mBtnSwitchOff4 = findViewById(R.id.btn_switch_off_4);
        mTvSwitchOn4 = findViewById(R.id.tv_switch_on_4);
        mTvSwitchOff4 = findViewById(R.id.tv_switch_off_4);
        mTvStateSwitch4 = findViewById(R.id.tv_state_switch4);
        mBtnClearOn4 = findViewById(R.id.btn_clear_on_4);
        mBtnClearOff4 = findViewById(R.id.btn_clear_off_4);
        mSwitch4 = findViewById(R.id.sw_4);

        //Add trigger listener
        //Back
        mBtnBack.setOnClickListener(this);

        //Switch 1
        mBtnSwitchOn1.setOnClickListener(this);
        mBtnSwitchOff1.setOnClickListener(this);
        mBtnClearOn1.setOnClickListener(this);
        mBtnClearOff1.setOnClickListener(this);
        //Switch 2
        mBtnSwitchOn2.setOnClickListener(this);
        mBtnSwitchOff2.setOnClickListener(this);
        mBtnClearOn2.setOnClickListener(this);
        mBtnClearOff2.setOnClickListener(this);
        //Switch 3
        mBtnSwitchOn3.setOnClickListener(this);
        mBtnSwitchOff3.setOnClickListener(this);
        mBtnClearOn3.setOnClickListener(this);
        mBtnClearOff3.setOnClickListener(this);
        //Switch 4
        mBtnSwitchOn4.setOnClickListener(this);
        mBtnSwitchOff4.setOnClickListener(this);
        mBtnClearOn4.setOnClickListener(this);
        mBtnClearOff4.setOnClickListener(this);

        //BroadcastReceiver
        //Switch 1
        receiverOn1 = new MyReceiverOn1();
        receiverOff1 = new MyReceiverOff1();
        //Switch 2
        receiverOn2 = new MyReceiverOn2();
        receiverOff2 = new MyReceiverOff2();
        //Switch 3
        receiverOn3 = new MyReceiverOn3();
        receiverOff3 = new MyReceiverOff3();
        //Switch 4
        receiverOn4 = new MyReceiverOn4();
        receiverOff4 = new MyReceiverOff4();

        //SharedPreference-Switch
        mSpSwitchTimer = getSharedPreferences(SWITCH_TIMER,
                MODE_PRIVATE);
        mSpSwitch1 = getSharedPreferences(SWITCH1,
                MODE_PRIVATE);
        mSpSwitch2 = getSharedPreferences(SWITCH2,
                MODE_PRIVATE);
        mSpSwitch3 = getSharedPreferences(SWITCH3,
                MODE_PRIVATE);
        mSpSwitch4 = getSharedPreferences(SWITCH4,
                MODE_PRIVATE);

        //SharedPreference-TextView
        mSaveOn1 = getSharedPreferences(ON1, MODE_PRIVATE);
        mSaveOff1 = getSharedPreferences(OFF1, MODE_PRIVATE);
        mSaveOn2 = getSharedPreferences(ON2, MODE_PRIVATE);
        mSaveOff2 = getSharedPreferences(OFF2, MODE_PRIVATE);
        mSaveOn3 = getSharedPreferences(ON3, MODE_PRIVATE);
        mSaveOff3 = getSharedPreferences(OFF3, MODE_PRIVATE);
        mSaveOn4 = getSharedPreferences(ON4, MODE_PRIVATE);
        mSaveOff4 = getSharedPreferences(OFF4, MODE_PRIVATE);

        //method
        saveTimePicker();
        stateAllSwitch();

        mSwitchTimer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    startServiceTimer();
                    setTextStateSwitchOn(mTvStateSwitchTimer);
                    SharedPreferences.Editor editorSwitchOnTimer = mSpSwitchTimer.edit();
                    editorSwitchOnTimer.putString(SWITCH_TIMER_KEY, SWITCH_TIMER_VALUE);
                    editorSwitchOnTimer.apply();
                    mLySwitchAllTimer.setVisibility(View.VISIBLE);
                } else {
                    cancelSwitch1();
                    cancelSwitch2();
                    cancelSwitch3();
                    cancelSwitch4();
                    stopServiceTimer();
                    setTextStateSwitchOff(mTvStateSwitchTimer);
                    mSwitch1.setChecked(false);
                    mSwitch2.setChecked(false);
                    mSwitch3.setChecked(false);
                    mSwitch4.setChecked(false);
                    SharedPreferences.Editor editorSwitchOffTimer = mSpSwitchTimer.edit();
                    editorSwitchOffTimer.remove(SWITCH_TIMER_KEY);
                    editorSwitchOffTimer.apply();
                    mLySwitchAllTimer.setVisibility(View.GONE);
                }
            }
        });


        mSwitch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String getTvSwitchOff1 = mTvSwitchOff1.getText().toString();
                String getTvSwitchOn1 = mTvSwitchOn1.getText().toString();
                if (buttonView.isChecked()) {
                    if (getTvSwitchOff1.equals("") && getTvSwitchOn1.equals("")) {
                        StyleableToast.makeText(TimerActivity.this, getString(R.string.msg_timer_empty),
                                Toast.LENGTH_LONG, R.style.ToastTheme).show();
                        mSwitch1.setChecked(false);
                        timePickerFragmentRepeat.show(getSupportFragmentManager(), TIME_PICKER_ON_1);
                    } else {

                        setTimeSwitchOn1();
                        setTimeSwitchOff1();
                        setTextStateSwitchOn(mTvStateSwitch1);
                        StyleableToast.makeText(TimerActivity.this, getString(R.string.msg_timer_switch_1),
                                Toast.LENGTH_LONG, R.style.ToastTheme).show();
                    }
                } else {
                    cancelSwitch1();
                    setTextStateSwitchOff(mTvStateSwitch1);
                    StyleableToast.makeText(TimerActivity.this, getString(R.string.msg_timer_switch_1_disabled),
                            Toast.LENGTH_LONG, R.style.ToastTheme).show();
                }
            }
        });
        mSwitch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String getTvSwitchOff2 = mTvSwitchOff2.getText().toString();
                String getTvSwitchOn2 = mTvSwitchOn2.getText().toString();
                if (buttonView.isChecked()) {
                    if (getTvSwitchOff2.equals("") && getTvSwitchOn2.equals("")) {
                        StyleableToast.makeText(TimerActivity.this, getString(R.string.msg_timer_empty),
                                Toast.LENGTH_LONG, R.style.ToastTheme).show();
                        mSwitch2.setChecked(false);
                        timePickerFragmentRepeat.show(getSupportFragmentManager(), TIME_PICKER_ON_2);
                    } else {
                        setTimeSwitchOn2();
                        setTimeSwitchOff2();
                        setTextStateSwitchOn(mTvStateSwitch2);
                        StyleableToast.makeText(TimerActivity.this, getString(R.string.msg_timer_switch_2),
                                Toast.LENGTH_LONG, R.style.ToastTheme).show();
                    }
                } else {
                    cancelSwitch2();
                    setTextStateSwitchOff(mTvStateSwitch2);
                    StyleableToast.makeText(TimerActivity.this, getString(R.string.msg_timer_switch_2_disabled),
                            Toast.LENGTH_LONG, R.style.ToastTheme).show();
                }
            }
        });


        mSwitch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String getTvSwitchOff3 = mTvSwitchOff3.getText().toString();
                String getTvSwitchOn3 = mTvSwitchOn3.getText().toString();
                if (buttonView.isChecked()) {
                    if (getTvSwitchOff3.equals("") && getTvSwitchOn3.equals("")) {
                        StyleableToast.makeText(TimerActivity.this, getString(R.string.msg_timer_empty),
                                Toast.LENGTH_LONG, R.style.ToastTheme).show();
                        mSwitch3.setChecked(false);
                        timePickerFragmentRepeat.show(getSupportFragmentManager(), TIME_PICKER_ON_3);
                    } else {
                        setTimeSwitchOn3();
                        setTimeSwitchOff3();
                        setTextStateSwitchOn(mTvStateSwitch3);
                        StyleableToast.makeText(TimerActivity.this, getString(R.string.msg_timer_switch_3),
                                Toast.LENGTH_LONG, R.style.ToastTheme).show();
                    }
                } else {
                    cancelSwitch3();
                    setTextStateSwitchOff(mTvStateSwitch3);
                    StyleableToast.makeText(TimerActivity.this, getString(R.string.msg_timer_switch_3_disabled),
                            Toast.LENGTH_LONG, R.style.ToastTheme).show();
                }
            }
        });
        mSwitch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String getTvSwitchOff4 = mTvSwitchOff4.getText().toString();
                String getTvSwitchOn4 = mTvSwitchOn4.getText().toString();
                if (buttonView.isChecked()) {
                    if (getTvSwitchOff4.equals("") && getTvSwitchOn4.equals("")) {
                        StyleableToast.makeText(TimerActivity.this, getString(R.string.msg_timer_empty),
                                Toast.LENGTH_LONG, R.style.ToastTheme).show();
                        mSwitch4.setChecked(false);
                        timePickerFragmentRepeat.show(getSupportFragmentManager(), TIME_PICKER_ON_4);
                    } else {
                        setTimeSwitchOn4();
                        setTimeSwitchOff4();
                        setTextStateSwitchOn(mTvStateSwitch4);
                        StyleableToast.makeText(TimerActivity.this, getString(R.string.msg_timer_switch_4),
                                Toast.LENGTH_LONG, R.style.ToastTheme).show();
                    }
                } else {
                    cancelSwitch4();
                    setTextStateSwitchOff(mTvStateSwitch4);
                    StyleableToast.makeText(TimerActivity.this, getString(R.string.msg_timer_switch_4_disabled),
                            Toast.LENGTH_LONG, R.style.ToastTheme).show();
                }
            }
        });
    }

    private void startServiceTimer() {
        startService(new Intent(this, MyService.class));
    }

    private void stopServiceTimer() {
        stopService(new Intent(this, MyService.class));
    }

    private void visibilityBtnClear(View viewOn, View viewOff,
                                    TextView textViewOn, TextView textViewOff) {
        viewOn.setVisibility(View.VISIBLE);
        viewOff.setVisibility(View.VISIBLE);
        if (textViewOn.getText().toString().isEmpty()) {
            viewOn.setVisibility(View.INVISIBLE);
        }
        if (textViewOff.getText().toString().isEmpty()) {
            viewOff.setVisibility(View.INVISIBLE);
        }
    }

    private void stateBtnClear() {
        //Switch 1
        visibilityBtnClear(mBtnClearOn1, mBtnClearOff1, mTvSwitchOn1, mTvSwitchOff1);
        //Switch 2
        visibilityBtnClear(mBtnClearOn2, mBtnClearOff2, mTvSwitchOn2, mTvSwitchOff2);
        //Switch 3
        visibilityBtnClear(mBtnClearOn3, mBtnClearOff3, mTvSwitchOn3, mTvSwitchOff3);
        //Switch 4
        visibilityBtnClear(mBtnClearOn4, mBtnClearOff4, mTvSwitchOn4, mTvSwitchOff4);
    }

    private void saveTimePickerSwitch(SharedPreferences spOn, String keyOn, TextView textViewOn,
                                      SharedPreferences spOff, String keyOff, TextView textViewOff) {
        String mSaveTimeOn = spOn.getString(keyOn, "");
        textViewOn.setText(mSaveTimeOn);
        String mSaveTimeOff = spOff.getString(keyOff, "");
        textViewOff.setText(mSaveTimeOff);
        stateBtnClear();
    }

    private void saveTimePicker() {
        //Switch 1
        saveTimePickerSwitch(mSaveOn1, ON1_KEY, mTvSwitchOn1, mSaveOff1, OFF1_KEY, mTvSwitchOff1);
        //Switch 2
        saveTimePickerSwitch(mSaveOn2, ON2_KEY, mTvSwitchOn2, mSaveOff2, OFF2_KEY, mTvSwitchOff2);
        //Switch 3
        saveTimePickerSwitch(mSaveOn3, ON3_KEY, mTvSwitchOn3, mSaveOff3, OFF3_KEY, mTvSwitchOff3);
        //Switch 4
        saveTimePickerSwitch(mSaveOn4, ON4_KEY, mTvSwitchOn4, mSaveOff4, OFF4_KEY, mTvSwitchOff4);
    }

    private void stateSwitch(@SuppressLint("UseSwitchCompatOrMaterialCode") Switch sw,
                             SharedPreferences spSwitch, String keySwitchOn, TextView textViewStateSwitch) {
        sw.setChecked(false);
        if (spSwitch.getString(keySwitchOn, null) != null) {
            sw.setChecked(true);
            setTextStateSwitchOn(textViewStateSwitch);
        } else {
            sw.setChecked(false);
            setTextStateSwitchOff(textViewStateSwitch);
        }
    }

    private void stateAllSwitch() {
        //Switch Timer
        mSwitchTimer.setChecked(false);
        if (mSpSwitchTimer.getString(SWITCH_TIMER_KEY, null) != null) {
            mSwitchTimer.setChecked(true);
            setTextStateSwitchOn(mTvStateSwitchTimer);
            mLySwitchAllTimer.setVisibility(View.VISIBLE);
        } else {
            mSwitchTimer.setChecked(false);
            setTextStateSwitchOff(mTvStateSwitchTimer);
            mLySwitchAllTimer.setVisibility(View.GONE);

        }
        //Switch 1
        stateSwitch(mSwitch1, mSpSwitch1, SWITCH1_KEY, mTvStateSwitch1);
        //Switch 2
        stateSwitch(mSwitch2, mSpSwitch2, SWITCH2_KEY, mTvStateSwitch2);
        //Switch 3
        stateSwitch(mSwitch3, mSpSwitch3, SWITCH3_KEY, mTvStateSwitch3);
        //Switch 4
        stateSwitch(mSwitch4, mSpSwitch4, SWITCH4_KEY, mTvStateSwitch4);
    }

    private void onClickBtnTimePicker(@SuppressLint("UseSwitchCompatOrMaterialCode") Switch sw, String stringTimePicker) {
        avoidMultipleClick();
        sw.setChecked(false);
        timePickerFragmentRepeat.show(getSupportFragmentManager(), stringTimePicker);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        avoidMultipleClick();
        switch (v.getId()) {
            //Back
            case R.id.btn_back:
                Intent goToMainActivity = new Intent(TimerActivity.this, MainActivity.class );
                startActivity(goToMainActivity);
                finish();
                break;
            //Switch 1
            case R.id.btn_switch_on_1:
                notCheckedSwitch1();
                onClickBtnTimePicker(mSwitch1, TIME_PICKER_ON_1);
                break;
            case R.id.btn_switch_off_1:
                notCheckedSwitch1();
                onClickBtnTimePicker(mSwitch1, TIME_PICKER_OFF_1);
                break;
            case R.id.btn_clear_on_1:
                notCheckedSwitch1();
                mTvSwitchOn1.setText("");
                String getTvEmptyOn1 = mTvSwitchOn1.getText().toString();
                addValueTimeOn1(getTvEmptyOn1);
                stateBtnClear();
                break;
            case R.id.btn_clear_off_1:
                notCheckedSwitch1();
                mTvSwitchOff1.setText("");
                String getTvEmptyOff1 = mTvSwitchOff1.getText().toString();
                addValueTimeOff1(getTvEmptyOff1);
                stateBtnClear();
                break;

            //Switch 2
            case R.id.btn_switch_on_2:
                notCheckedSwitch2();
                onClickBtnTimePicker(mSwitch2, TIME_PICKER_ON_2);
                break;
            case R.id.btn_switch_off_2:
                notCheckedSwitch2();
                onClickBtnTimePicker(mSwitch2, TIME_PICKER_OFF_2);
                break;
            case R.id.btn_clear_on_2:
                notCheckedSwitch2();
                mTvSwitchOn2.setText("");
                String getTvEmptyOn2 = mTvSwitchOn2.getText().toString();
                addValueTimeOn2(getTvEmptyOn2);
                stateBtnClear();
                break;
            case R.id.btn_clear_off_2:
                notCheckedSwitch2();
                mTvSwitchOff2.setText("");
                String getTvEmptyOff2 = mTvSwitchOff2.getText().toString();
                addValueTimeOff2(getTvEmptyOff2);
                stateBtnClear();
                break;

            //Switch 3
            case R.id.btn_switch_on_3:
                notCheckedSwitch3();
                onClickBtnTimePicker(mSwitch3, TIME_PICKER_ON_3);
                break;
            case R.id.btn_switch_off_3:
                notCheckedSwitch3();
                onClickBtnTimePicker(mSwitch3, TIME_PICKER_OFF_3);
                break;
            case R.id.btn_clear_on_3:
                notCheckedSwitch3();
                mTvSwitchOn3.setText("");
                String getTvEmptyOn3 = mTvSwitchOn3.getText().toString();
                addValueTimeOn3(getTvEmptyOn3);
                stateBtnClear();
                break;
            case R.id.btn_clear_off_3:
                notCheckedSwitch3();
                mTvSwitchOff3.setText("");
                String getTvEmptyOff3 = mTvSwitchOff3.getText().toString();
                addValueTimeOff3(getTvEmptyOff3);
                stateBtnClear();
                break;

            //Switch 4
            case R.id.btn_switch_on_4:
                notCheckedSwitch4();
                onClickBtnTimePicker(mSwitch4, TIME_PICKER_ON_4);
                break;
            case R.id.btn_switch_off_4:
                notCheckedSwitch4();
                onClickBtnTimePicker(mSwitch4, TIME_PICKER_OFF_4);
                break;
            case R.id.btn_clear_on_4:
                notCheckedSwitch4();
                mTvSwitchOn4.setText("");
                String getTvEmptyOn4 = mTvSwitchOn4.getText().toString();
                addValueTimeOn4(getTvEmptyOn4);
                stateBtnClear();
                break;
            case R.id.btn_clear_off_4:
                notCheckedSwitch4();
                mTvSwitchOff4.setText("");
                String getTvEmptyOff4 = mTvSwitchOff4.getText().toString();
                addValueTimeOff4(getTvEmptyOff4);
                stateBtnClear();
                break;
            default:
                break;
        }
    }

    private void notCheckedSwitch1() {
        SharedPreferences.Editor editorSwitch1 = mSpSwitch1.edit();
        editorSwitch1.remove(SWITCH1_KEY);
        editorSwitch1.apply();
        mSwitch1.setChecked(false);
    }
    private void notCheckedSwitch2() {
        SharedPreferences.Editor editorSwitch2 = mSpSwitch2.edit();
        editorSwitch2.remove(SWITCH2_KEY);
        editorSwitch2.apply();
        mSwitch2.setChecked(false);
    }
    private void notCheckedSwitch3() {
        SharedPreferences.Editor editorSwitch3 = mSpSwitch3.edit();
        editorSwitch3.remove(SWITCH3_KEY);
        editorSwitch3.apply();
        mSwitch3.setChecked(false);
    }
    private void notCheckedSwitch4() {
        SharedPreferences.Editor editorSwitch4 = mSpSwitch4.edit();
        editorSwitch4.remove(SWITCH4_KEY);
        editorSwitch4.apply();
        mSwitch4.setChecked(false);
    }


    @Override
    public void onDialogTimeSet(String tag, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        switch (tag) {
            //Switch 1
            case TIME_PICKER_ON_1:
                mTvSwitchOn1.setText(dateFormat.format(calendar.getTime()));
                String getTvSwitchOn1 = mTvSwitchOn1.getText().toString();
                addValueTimeOn1(getTvSwitchOn1);
                stateBtnClear();
                break;
            case TIME_PICKER_OFF_1:
                mTvSwitchOff1.setText(dateFormat.format(calendar.getTime()));
                String getTvSwitchOff1 = mTvSwitchOff1.getText().toString();
                addValueTimeOff1(getTvSwitchOff1);
                stateBtnClear();
                break;

            //Switch 2
            case TIME_PICKER_ON_2:
                mTvSwitchOn2.setText(dateFormat.format(calendar.getTime()));
                String getTvSwitchOn2 = mTvSwitchOn2.getText().toString();
                addValueTimeOn2(getTvSwitchOn2);
                stateBtnClear();
                break;
            case TIME_PICKER_OFF_2:
                mTvSwitchOff2.setText(dateFormat.format(calendar.getTime()));
                String getTvSwitchOff2 = mTvSwitchOff2.getText().toString();
                addValueTimeOff2(getTvSwitchOff2);
                stateBtnClear();
                break;

            //Switch 3
            case TIME_PICKER_ON_3:
                mTvSwitchOn3.setText(dateFormat.format(calendar.getTime()));
                String getTvSwitchOn3 = mTvSwitchOn3.getText().toString();
                addValueTimeOn3(getTvSwitchOn3);
                stateBtnClear();
                break;
            case TIME_PICKER_OFF_3:
                mTvSwitchOff3.setText(dateFormat.format(calendar.getTime()));
                String getTvSwitchOff3 = mTvSwitchOff3.getText().toString();
                addValueTimeOff3(getTvSwitchOff3);
                stateBtnClear();
                break;

            //Switch 4
            case TIME_PICKER_ON_4:
                mTvSwitchOn4.setText(dateFormat.format(calendar.getTime()));
                String getTvSwitchOn4 = mTvSwitchOn4.getText().toString();
                addValueTimeOn4(getTvSwitchOn4);
                stateBtnClear();
                break;
            case TIME_PICKER_OFF_4:
                mTvSwitchOff4.setText(dateFormat.format(calendar.getTime()));
                String getTvSwitchOff4 = mTvSwitchOff4.getText().toString();
                addValueTimeOff4(getTvSwitchOff4);
                stateBtnClear();
                break;
            default:
                break;
        }
    }

    private void avoidMultipleClick() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 5) {
            mBtnSwitchOn1.setEnabled(false);
            mBtnSwitchOff1.setEnabled(false);
            mBtnSwitchOn2.setEnabled(false);
            mBtnSwitchOff2.setEnabled(false);
            mBtnSwitchOn3.setEnabled(false);
            mBtnSwitchOff3.setEnabled(false);
            mBtnSwitchOn4.setEnabled(false);
            mBtnSwitchOff4.setEnabled(false);
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();


    }

    private void addValueTimeOn1(String s) {
        SharedPreferences.Editor editorTimePickerOn1 = mSaveOn1.edit();
        editorTimePickerOn1.putString(ON1_KEY, s);
        editorTimePickerOn1.apply();
    }

    private void addValueTimeOff1(String s) {
        SharedPreferences.Editor editorTimePickerOff1 = mSaveOff1.edit();
        editorTimePickerOff1.putString(OFF1_KEY, s);
        editorTimePickerOff1.apply();
    }

    private void addValueTimeOn2(String s) {
        SharedPreferences.Editor editorTimePickerOn2 = mSaveOn2.edit();
        editorTimePickerOn2.putString(ON2_KEY, s);
        editorTimePickerOn2.apply();
    }

    private void addValueTimeOff2(String s) {
        SharedPreferences.Editor editorTimePickerOff2 = mSaveOff2.edit();
        editorTimePickerOff2.putString(OFF2_KEY, s);
        editorTimePickerOff2.apply();
    }

    private void addValueTimeOn3(String s) {
        SharedPreferences.Editor editorTimePickerOn3 = mSaveOn3.edit();
        editorTimePickerOn3.putString(ON3_KEY, s);
        editorTimePickerOn3.apply();
    }

    private void addValueTimeOff3(String s) {
        SharedPreferences.Editor editorTimePickerOff3 = mSaveOff3.edit();
        editorTimePickerOff3.putString(OFF3_KEY, s);
        editorTimePickerOff3.apply();
    }

    private void addValueTimeOn4(String s) {
        SharedPreferences.Editor editorTimePickerOn4 = mSaveOn4.edit();
        editorTimePickerOn4.putString(ON4_KEY, s);
        editorTimePickerOn4.apply();
    }

    private void addValueTimeOff4(String s) {
        SharedPreferences.Editor editorTimePickerOff4 = mSaveOff4.edit();
        editorTimePickerOff4.putString(OFF4_KEY, s);
        editorTimePickerOff4.apply();
    }


    private void setTextStateSwitchOn(TextView textView) {
        textView.setText(R.string.main_on);
        textView.setTextColor(this.getResources().getColor(R.color.colorPrimaryDark));

    }

    private void setTextStateSwitchOff(TextView textView) {
        textView.setText(R.string.main_off);
        textView.setTextColor(this.getResources().getColor(R.color.colorDefault));


    }


    private void setTimeSwitchOn1() {
        String onAt1 = mTvSwitchOn1.getText().toString();
        receiverOn1.setTimerOn1(this, onAt1);
        SharedPreferences.Editor editorSwitchOn1 = mSpSwitch1.edit();
        editorSwitchOn1.putString(SWITCH1_KEY, SWITCH1_VALUE);
        editorSwitchOn1.apply();
    }

    private void setTimeSwitchOff1() {
        String offAt1 = mTvSwitchOff1.getText().toString();
        receiverOff1.setTimerOff1(this, offAt1);
        SharedPreferences.Editor editorSwitchOff1 = mSpSwitch1.edit();
        editorSwitchOff1.putString(SWITCH1_KEY, SWITCH1_VALUE);
        editorSwitchOff1.apply();

    }

    private void setTimeSwitchOn2() {
        String onAt2 = mTvSwitchOn2.getText().toString();
        receiverOn2.setTimerOn2(this, onAt2);
        SharedPreferences.Editor editorSwitchOn2 = mSpSwitch2.edit();
        editorSwitchOn2.putString(SWITCH2_KEY, SWITCH2_VALUE);
        editorSwitchOn2.apply();
    }

    private void setTimeSwitchOff2() {
        String offAt2 = mTvSwitchOff2.getText().toString();
        receiverOff2.setTimerOff2(this, offAt2);
        SharedPreferences.Editor editorSwitchOff2 = mSpSwitch2.edit();
        editorSwitchOff2.putString(SWITCH2_KEY, SWITCH2_VALUE);
        editorSwitchOff2.apply();

    }

    private void setTimeSwitchOn3() {
        String onAt3 = mTvSwitchOn3.getText().toString();
        receiverOn3.setTimerOn3(this, onAt3);
        SharedPreferences.Editor editorSwitchOn3 = mSpSwitch3.edit();
        editorSwitchOn3.putString(SWITCH3_KEY, SWITCH3_VALUE);
        editorSwitchOn3.apply();
    }

    private void setTimeSwitchOff3() {
        String offAt3 = mTvSwitchOff3.getText().toString();
        receiverOff3.setTimerOff3(this, offAt3);
        SharedPreferences.Editor editorSwitchOff3 = mSpSwitch3.edit();
        editorSwitchOff3.putString(SWITCH3_KEY, SWITCH3_VALUE);
        editorSwitchOff3.apply();

    }

    private void setTimeSwitchOn4() {
        String onAt4 = mTvSwitchOn4.getText().toString();
        receiverOn4.setTimerOn4(this, onAt4);
        SharedPreferences.Editor editorSwitchOn4 = mSpSwitch4.edit();
        editorSwitchOn4.putString(SWITCH4_KEY, SWITCH4_VALUE);
        editorSwitchOn4.apply();
    }

    private void setTimeSwitchOff4() {
        String offAt4 = mTvSwitchOff4.getText().toString();
        receiverOff4.setTimerOff4(this, offAt4);
        SharedPreferences.Editor editorSwitchOff4 = mSpSwitch4.edit();
        editorSwitchOff4.putString(SWITCH4_KEY, SWITCH4_VALUE);
        editorSwitchOff4.apply();

    }

    private void cancelSwitch1() {
        receiverOn1.cancelTimer(this);
        receiverOff1.cancelTimer(this);
        SharedPreferences.Editor editorSwitch1 = mSpSwitch1.edit();
        editorSwitch1.remove(SWITCH1_KEY);
        editorSwitch1.apply();
    }

    private void cancelSwitch2() {
        receiverOn2.cancelTimer(this);
        receiverOff2.cancelTimer(this);
        SharedPreferences.Editor editorSwitch2 = mSpSwitch2.edit();
        editorSwitch2.remove(SWITCH2_KEY);
        editorSwitch2.apply();
    }

    private void cancelSwitch3() {
        receiverOn3.cancelTimer(this);
        receiverOff3.cancelTimer(this);
        SharedPreferences.Editor editorSwitch3 = mSpSwitch3.edit();
        editorSwitch3.remove(SWITCH3_KEY);
        editorSwitch3.apply();
    }

    private void cancelSwitch4() {
        receiverOn4.cancelTimer(this);
        receiverOff4.cancelTimer(this);
        SharedPreferences.Editor editorSwitch4 = mSpSwitch4.edit();
        editorSwitch4.remove(SWITCH4_KEY);
        editorSwitch4.apply();
    }


}