package co.suchiz.hexofthrones;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;


public class PlayActivity extends AppCompatActivity {
    private final static int REQUEST_RESTART = 444;
    private final static int REQUEST_PLAYER = 555;
    private RelativeLayout mainLayout;
    private Board board;
    private GameUI gameUI;
    private Button settingButton;
    private SoundManager soundManager = SoundManager.getSoundManagerInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);

        final Intent receivedIntent = this.getIntent();
        final int size = receivedIntent.getIntExtra("SIZE", 9);
        mainLayout = findViewById(R.id.mainLayout);
        board = new Board(this, size);
        gameUI = new GameUI(mainLayout, outMetrics.heightPixels, outMetrics.widthPixels, board);
        board.createNewGame();
        settingButton = findViewById(R.id.settingPlayButton);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundManager.playTap();
                startActivityForResult(new Intent(PlayActivity.this, SettingActivity.class), REQUEST_RESTART);
            }
        });
        startActivityForResult(new Intent(this, MiniGameActivity.class), REQUEST_PLAYER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PLAYER) {
            if(resultCode == RESULT_OK){
                int result = data.getIntExtra("player", 0);
                if (result == 1) {
                    board.setFirstPlayer(Status.LIVING);
                    board.setSecondPlayer(Status.DEAD);
                }else {
                    board.setFirstPlayer(Status.DEAD);
                    board.setSecondPlayer(Status.LIVING);
                }
                board.updateCurrentPlayer();
                gameUI.displayBoard();
            } else {
                finish();
            }
        } else if (requestCode == REQUEST_RESTART){
            if(resultCode == RESULT_OK){
                Intent currentIntent = getIntent();
                finish();
                startActivity(currentIntent);
            }
        }
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Quit current game")
                .setMessage("The current board will be lost")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

}
