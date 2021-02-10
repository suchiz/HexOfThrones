package co.suchiz.hexofthrones;

import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Random;

public class PlayerMiniGame {
    private Status player;
    private Board board;
    private int SIZE;
    private RelativeLayout playerLayout;
    private ArrayList<Pair> plist = new ArrayList<>();
    private MiniGameActivity activity;
    private SoundManager soundManager = SoundManager.getSoundManagerInstance();

    public PlayerMiniGame(MiniGameActivity activity, int size, RelativeLayout playLayout, Status player) {
        this.player = player;
        this.activity = activity;
        this.playerLayout = playLayout;
        this.SIZE = size;

        board = new Board(activity, size);
        board.createNewGame();
        for (int i = 0; i < 10; i++)
            plist.add(new Pair(new Random().nextInt(SIZE), new Random().nextInt(SIZE)));
    }

    public void init(int HEX_SIZE, int SEMIHEX_SIZE){
        for (int x = 0; x < SIZE; ++x) {
            for (int y = 0; y < SIZE; ++y) {
                final Hexagone hexToAdd = board.getBoard()[x][y];
                hexToAdd.setBackgroundResource(R.drawable.middle_hex);
                hexToAdd.setY(x * HEX_SIZE + HEX_SIZE);
                hexToAdd.setX((x * SEMIHEX_SIZE + y * HEX_SIZE));
                playerLayout.addView(hexToAdd);
                hexToAdd.requestLayout();
                hexToAdd.getLayoutParams().height = HEX_SIZE;
                hexToAdd.getLayoutParams().width = HEX_SIZE - (HEX_SIZE / 9);
            }
        }
        next();
    }

    public void next(){
        if (!plist.isEmpty()) {
            final Hexagone hex = board.getBoard()[plist.get(0).first][plist.get(0).second];
            switch (player){
                case LIVING: hex.setBackgroundResource(R.drawable.yellowhex);; break;
                case DEAD: hex.setBackgroundResource(R.drawable.bluehex);; break;
            }
            hex.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    soundManager.playTap();
                    plist.remove(0);
                    hex.setBackgroundResource(R.drawable.middle_hex);
                    hex.setOnClickListener(null);
                    next();
                }
            });
        } else {
            activity.gameOver(player);
        }
    }

}
