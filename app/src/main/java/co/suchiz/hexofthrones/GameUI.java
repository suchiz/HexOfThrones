package co.suchiz.hexofthrones;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GameUI {
    private int SCREEN_HEIGHT;
    private int SCREEN_WIDTH;
    private int HEX_SIZE;
    private int SEMIHEX_SIZE;
    private int SIZE;
    private int NEW_ORIGIN;
    private int HEX_WIDTH;

    private RelativeLayout mainLayout;
    private Board board;
    private Button undoButton;
    private Button lastMoveButton;
    private TextView currentPlayer;
    private SoundManager soundManager = SoundManager.getSoundManagerInstance();

    public GameUI(RelativeLayout mainLayout, int heightPixels, int widthPixels, Board board) {
        this.mainLayout = mainLayout;
        this.board = board;
        this.SCREEN_HEIGHT = heightPixels;
        this.SCREEN_WIDTH = widthPixels;
        this.SIZE = board.getSIZE();
        this.SEMIHEX_SIZE = SCREEN_WIDTH/(SIZE*2+SIZE-1);
        this.HEX_SIZE = 2*SEMIHEX_SIZE;
        this.NEW_ORIGIN = SCREEN_HEIGHT/2 - (SIZE/2)*HEX_SIZE;
        this.HEX_WIDTH = HEX_SIZE-(HEX_SIZE/9);
        currentPlayer = mainLayout.findViewById(R.id.currentPlayerTV);
        lastMoveButton = mainLayout.findViewById(R.id.lastMoveButton);
        undoButton = mainLayout.findViewById(R.id.undoButton);
        initButtons();
    }

    private void initButtons(){
        lastMoveButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        soundManager.playTap();
                        lastMoveButton.setPressed(true);
                        if(board.getLastMove() != null)
                            switch (board.getLastMove().getStatus()){
                                case LIVING:
                                    board.getLastMove().setBackgroundResource(R.drawable.yellowhex_highlight);
                                    board.getLastMove().resize(HEX_WIDTH + SEMIHEX_SIZE / 4, HEX_SIZE + SEMIHEX_SIZE / 4);break;
                                case DEAD:
                                    board.getLastMove().setBackgroundResource(R.drawable.bluehex_highlight);
                                    board.getLastMove().resize(HEX_WIDTH + SEMIHEX_SIZE / 4, HEX_SIZE + SEMIHEX_SIZE / 4);break;
                            }
                        return true;
                    case MotionEvent.ACTION_UP:
                        lastMoveButton.setPressed(false);
                        if(board.getLastMove() != null)
                            switch (board.getLastMove().getStatus()){
                                case LIVING:
                                    board.getLastMove().setBackgroundResource(R.drawable.yellowhex);
                                    board.getLastMove().resize(HEX_WIDTH, HEX_SIZE); break;
                                case DEAD:
                                    board.getLastMove().resize(HEX_WIDTH, HEX_SIZE);
                                    board.getLastMove().setBackgroundResource(R.drawable.bluehex); break;
                            }
                        return true;
                }
                return false;
            }
        });

        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundManager.playTap();
                if (board.getLastMove() != null){
                    Hexagone lastMove = board.getLastMove();
                    if (lastMove.getX_ind() == 0 && lastMove.getY_ind() == 0)
                        board.getBoard()[lastMove.getX_ind()][lastMove.getY_ind()].setBackgroundResource(R.drawable.topleft_hex);
                    else if (lastMove.getX_ind() == 0 && lastMove.getY_ind() == SIZE-1)
                        board.getBoard()[lastMove.getX_ind()][lastMove.getY_ind()].setBackgroundResource(R.drawable.topright_hex);
                    else if (lastMove.getX_ind() == SIZE-1 && lastMove.getY_ind() == SIZE-1)
                        board.getBoard()[lastMove.getX_ind()][lastMove.getY_ind()].setBackgroundResource(R.drawable.botright_hex);
                    else if (lastMove.getX_ind() == SIZE-1 && lastMove.getY_ind() == 0)
                        board.getBoard()[lastMove.getX_ind()][lastMove.getY_ind()].setBackgroundResource(R.drawable.botleft_hex);
                    else if (lastMove.getX_ind() == 0)
                        board.getBoard()[lastMove.getX_ind()][lastMove.getY_ind()].setBackgroundResource(R.drawable.top_hex);
                    else if (lastMove.getX_ind() == SIZE-1)
                        board.getBoard()[lastMove.getX_ind()][lastMove.getY_ind()].setBackgroundResource(R.drawable.bot_hex);
                    else if (lastMove.getY_ind() == 0)
                        board.getBoard()[lastMove.getX_ind()][lastMove.getY_ind()].setBackgroundResource(R.drawable.left_hex);
                    else if (lastMove.getY_ind() == SIZE-1)
                        board.getBoard()[lastMove.getX_ind()][lastMove.getY_ind()].setBackgroundResource(R.drawable.right_hex);
                    else
                        board.getBoard()[lastMove.getX_ind()][lastMove.getY_ind()].setBackgroundResource(R.drawable.middle_hex);
                    board.getBoard()[lastMove.getX_ind()][lastMove.getY_ind()].setStatus(Status.EMPTY);
                    board.decreaseTurn();
                    board.setLastMove(null);
                    board.updateCurrentPlayer();
                    displayCurrentPlayer();
                }
            }
        });
    }

    public void displayBoard(){
        RelativeLayout ly = mainLayout.findViewById(R.id.toolsLayout);
        ly.setY(NEW_ORIGIN + (SIZE)*HEX_SIZE);
        displayCurrentPlayer();
        for (int x = 0; x < SIZE; ++x){
            for (int y = 0; y < SIZE; ++y){
                final Hexagone hexToAdd = board.getBoard()[x][y];
                if (x == 0 && y == 0) {
                    hexToAdd.setBackgroundResource(R.drawable.topleft_hex);
                    hexToAdd.setId(R.id.topleft);
                }else if (x == 0 && y == SIZE-1)
                    hexToAdd.setBackgroundResource(R.drawable.topright_hex);
                else if (x == SIZE-1 && y == SIZE-1) {
                    hexToAdd.setBackgroundResource(R.drawable.botright_hex);
                    hexToAdd.setId(R.id.botright);
                }else if (x == SIZE-1 && y == 0)
                    hexToAdd.setBackgroundResource(R.drawable.botleft_hex);
                else if (x == 0)
                    hexToAdd.setBackgroundResource(R.drawable.top_hex);
                else if (x == SIZE-1)
                    hexToAdd.setBackgroundResource(R.drawable.bot_hex);
                else if (y == 0)
                    hexToAdd.setBackgroundResource(R.drawable.left_hex);
                else if (y == SIZE-1)
                    hexToAdd.setBackgroundResource(R.drawable.right_hex);
                else
                    hexToAdd.setBackgroundResource(R.drawable.middle_hex);
                hexToAdd.setY(NEW_ORIGIN + x*HEX_SIZE);
                hexToAdd.setX(x*SEMIHEX_SIZE + y*HEX_SIZE);
                mainLayout.addView(hexToAdd);
                hexToAdd.requestLayout();
                hexToAdd.getLayoutParams().height = HEX_SIZE;
                hexToAdd.getLayoutParams().width = HEX_WIDTH;

                hexToAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        soundManager.playTap();
                        Status status = Status.EMPTY;
                        if (board.isValid(hexToAdd))
                           status = board.playTurn(hexToAdd);
                        switch (status){
                            case LIVING: hexToAdd.setBackgroundResource(R.drawable.yellowhex); break;
                            case DEAD: hexToAdd.setBackgroundResource(R.drawable.bluehex); break;
                        }
                        if (board.checkWinner())
                            displayWinner();
                        displayCurrentPlayer();
                    }
                });
            }
        }
    }

    private void displayWinner() {
        TextView tv = board.getContext().findViewById(R.id.winnerTextView);

        for (int x = 0; x < SIZE; ++x)
            for (int y = 0; y < SIZE; ++y)
                board.getBoard()[x][y].setEnabled(false);
        lastMoveButton.setEnabled(false);
        undoButton.setEnabled(false);
        currentPlayer.setVisibility(View.INVISIBLE);

        for (Hexagone hex: board.getViewWinner())
            switch (board.getLastMove().getStatus()){
                case LIVING:
                    hex.setBackgroundResource(R.drawable.yellowhex_highlight);
                    hex.resize(HEX_WIDTH + SEMIHEX_SIZE / 4, HEX_SIZE + SEMIHEX_SIZE / 4); break;
                case DEAD:
                    hex.setBackgroundResource(R.drawable.bluehex_highlight);
                    hex.resize(HEX_WIDTH + SEMIHEX_SIZE / 4, HEX_SIZE + SEMIHEX_SIZE / 4); break;
            }

        switch (board.getLastMove().getStatus()){
            case LIVING: tv.setTextColor(currentPlayer.getResources().getColor(R.color.yellow)); break;
            case DEAD: tv.setTextColor(currentPlayer.getResources().getColor(R.color.blue)); break;
        }
        tv.setText(board.getLastMove().getStatus().toString() + " has won the game !");

        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundManager.playMenu();
                board.getContext().finish();
            }
        });

    }

    private void displayCurrentPlayer(){
        switch (board.getCurrentPlayer()){
            case LIVING: currentPlayer.setTextColor(currentPlayer.getResources().getColor(R.color.yellow)); break;
            case DEAD: currentPlayer.setTextColor(currentPlayer.getResources().getColor(R.color.blue)); break;
        }
        currentPlayer.setText(board.getCurrentPlayer().toString() + " TURN");
    }

}
