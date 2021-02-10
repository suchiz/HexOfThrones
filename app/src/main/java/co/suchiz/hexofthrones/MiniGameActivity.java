package co.suchiz.hexofthrones;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MiniGameActivity extends AppCompatActivity {
    private final static int SIZE = 4;
    private RelativeLayout player1Layout;
    private RelativeLayout player2Layout;
    private TextView winnerTV;

    private int SEMIHEX_SIZE;
    private int HEX_SIZE;
    private int winner = -1;
    private CountDownTimer countDownTimer;
    private SoundManager soundManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mini_game);

        Display display = getWindowManager().getDefaultDisplay();
        final DisplayMetrics outMetrics = new DisplayMetrics ();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float dp = 12f;
        float fpixels = metrics.density * dp;
        final int pixels = (int) (fpixels + 0.5f);
        display.getMetrics(outMetrics);

        soundManager = SoundManager.getSoundManagerInstance();
        SEMIHEX_SIZE = outMetrics.widthPixels/(SIZE*2+SIZE-1);
        HEX_SIZE = 2*SEMIHEX_SIZE;

        player1Layout = findViewById(R.id.player1Layout);
        player2Layout = findViewById(R.id.player2Layout);
        winnerTV = findViewById(R.id.winnerMiniGameTV);
        winnerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (winner != -1) {
                    soundManager.playMenu();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("player", winner);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            }
        });

        final PlayerMiniGame p1 = new PlayerMiniGame(this, SIZE, player1Layout, Status.LIVING);
        final PlayerMiniGame p2 = new PlayerMiniGame(this, SIZE, player2Layout, Status.DEAD);
        final TextView countTV = findViewById(R.id.countdown1TV);
        final TextView count2TV = findViewById(R.id.countdown2TV);

        countDownTimer = new CountDownTimer(3*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                soundManager.playMenu();
                countTV.setText(""+((millisUntilFinished/1000)+1));
                count2TV.setText(""+((millisUntilFinished/1000)+1));
            }

            @Override
            public void onFinish() {
                p1.init(HEX_SIZE, SEMIHEX_SIZE);
                p2.init(HEX_SIZE, SEMIHEX_SIZE);
                soundManager.playStart();
                Typeface got = getResources().getFont(R.font.game_of_thrones);
                count2TV.setTypeface(got);
                countTV.setTypeface(got);
                count2TV.setText("DEAD");
                countTV.setText("LIVING");
                count2TV.setTextSize(pixels);
                countTV.setTextSize(pixels);
                findViewById(R.id.info1TV).setVisibility(View.INVISIBLE);
                findViewById(R.id.info2TV).setVisibility(View.INVISIBLE);
                winnerTV.setVisibility(View.INVISIBLE);
            }
        }.start();

    }

    public void gameOver(Status player) {
        switch (player){
            case LIVING: winnerTV.setTextColor(winnerTV.getResources().getColor(R.color.yellow)); break;
            case DEAD: winnerTV.setTextColor(winnerTV.getResources().getColor(R.color.blue)); break;
        }
        winnerTV.setText(player.toString() + " WILL START");
        winnerTV.setVisibility(View.VISIBLE);
        player1Layout.setVisibility(View.INVISIBLE);
        player2Layout.setVisibility(View.INVISIBLE);
        winner = player.ordinal();
    }

    @Override
    public void onBackPressed() {
        countDownTimer.cancel();
        super.onBackPressed();
    }
}
