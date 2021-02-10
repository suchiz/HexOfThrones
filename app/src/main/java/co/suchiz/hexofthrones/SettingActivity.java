package co.suchiz.hexofthrones;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingActivity extends AppCompatActivity {

    private Button infoButton;
    private Button restartButton;
    private Button soundButton;
    private TextView infoTV;
    private RelativeLayout settingRelativeLayout;
    private SoundManager soundManager = SoundManager.getSoundManagerInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Intent receivedIntent = this.getIntent();
        final String activity = receivedIntent.getStringExtra("ACTIVITY");



        settingRelativeLayout = findViewById(R.id.settingLayout);
        infoButton = findViewById(R.id.infoButton);
        restartButton = findViewById(R.id.restartButton);
        soundButton = findViewById(R.id.soundButton);
        infoTV = findViewById(R.id.rulesTV);
        infoTV.setVisibility(View.INVISIBLE);

        if (!soundManager.getMute())
            soundButton.setBackgroundResource(R.drawable.sound_highlight);
        else
            soundButton.setBackgroundResource(R.drawable.sound_default);

        if (activity != null){
            restartButton.setVisibility(View.INVISIBLE);
            infoButton.setVisibility(View.INVISIBLE);
        }

        initListeners();
    }

    private void initListeners() {
        settingRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingActivity.this.finish();
            }
        });

        infoButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        soundManager.playTap();
                        infoButton.setPressed(true);
                        infoTV.setVisibility(View.VISIBLE);
                        return true;
                    case MotionEvent.ACTION_UP:
                        infoButton.setPressed(false);
                        infoTV.setVisibility(View.INVISIBLE);
                        return true;
                }
                return false;
            }
        });

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundManager.playTap();
                new AlertDialog.Builder(SettingActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Restart current game")
                        .setMessage("The current board will be lost")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.putExtra("RESTART", true);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

        soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundManager.setMute(!soundManager.getMute());
                soundManager.playTap();

                if (!soundManager.getMute())
                    soundButton.setBackgroundResource(R.drawable.sound_highlight);
                else
                    soundButton.setBackgroundResource(R.drawable.sound_default);
            }
        });
    }


}
