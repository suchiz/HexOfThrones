package co.suchiz.hexofthrones;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private final static String SHARED_REF = "SETTINGS";
    private final static String MUTE_SETTING = "MUTE";

    private Button classicPlayButton;
    private Button plusButton;
    private Button minusButton;
    private Button settinButton;
    private TextView levelTextView;
    private Button customPlayButton;
    private Button goButton;
    private LinearLayout levelLinearLayout;
    private boolean tapped = false;
    private int customLevel = 4;
    private SoundManager soundManager = SoundManager.getSoundManagerInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        soundManager.setActivity(this);
        soundManager.init();
        loadSharedRef();

        settinButton = findViewById(R.id.settingMainButton);
        goButton = findViewById(R.id.goButton);
        plusButton = findViewById(R.id.plusButton);
        minusButton = findViewById(R.id.minusButton);
        levelTextView = findViewById(R.id.levelTextView);
        levelLinearLayout = findViewById(R.id.levelLayout);
        classicPlayButton = findViewById(R.id.classicPlayButton);
        customPlayButton = findViewById(R.id.customPlayButton);
        levelLinearLayout.setVisibility(View.INVISIBLE);
        goButton.setVisibility(View.INVISIBLE);

        initListeners();
    }

    public void initListeners(){
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundManager.playTap();
                Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                intent.putExtra("SIZE", customLevel);
                startActivity(intent);
            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundManager.playTap();
                customLevel++;
                if (customLevel > 11) {
                    customLevel = 4;
                    levelTextView.setText(customLevel+"");
                } else {
                    levelTextView.setText(customLevel+"");
                }
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundManager.playTap();
                customLevel--;
                if (customLevel < 4) {
                    customLevel = 11;
                    levelTextView.setText(customLevel+"");
                } else {
                    levelTextView.setText(customLevel+"");
                }
            }
        });

        customPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundManager.playMenu();
                if (!tapped) {
                    classicPlayButton.setVisibility(View.INVISIBLE);
                    levelLinearLayout.setVisibility(View.VISIBLE);
                    goButton.setVisibility(View.VISIBLE);
                    tapped = true;
                } else {
                    classicPlayButton.setVisibility(View.VISIBLE);
                    levelLinearLayout.setVisibility(View.INVISIBLE);
                    goButton.setVisibility(View.INVISIBLE);
                    tapped = false;
                }
            }
        });

        classicPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundManager.playMenu();
                startActivity(new Intent(MainActivity.this, PlayActivity.class));
            }
        });

        settinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundManager.playTap();
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                intent.putExtra("ACTIVITY", "MAIN");
                startActivity(intent);
            }
        });
    }


    private void saveSharedRef(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_REF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(MUTE_SETTING, soundManager.getMute());
        editor.apply();
    }

    private void loadSharedRef(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_REF, MODE_PRIVATE);
        soundManager.setMute(sharedPreferences.getBoolean(MUTE_SETTING, false));
    }

    @Override
    public void onBackPressed() {
        if(tapped)
            customPlayButton.performClick();
        else
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Quit game for good")
                    .setMessage("Are you sure to quit the game ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveSharedRef();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundManager.release();
    }
}
