package co.suchiz.hexofthrones;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;

public class Board {
    private Activity context;
    private int SIZE;
    private Hexagone[][] board;
    private int turn = 0;
    private Status firstPlayer = Status.LIVING;
    private Status secondPlayer = Status.DEAD;
    private Status currentPlayer = firstPlayer;
    private Hexagone lastMove;
    private ArrayList<Hexagone> viewWinner = new ArrayList<>();

    public Board(Activity context, int SIZE) {
        this.context = context;
        this.SIZE = SIZE;
        this.board = new Hexagone[SIZE][SIZE];
    }

    public void createNewGame(){
        this.turn = 0;
        lastMove = null;
        viewWinner = new ArrayList<>();
        for (int x = 0; x < SIZE; ++x)
            for (int y = 0; y < SIZE; ++y)
                board[x][y] = new Hexagone(context, x, y, Status.EMPTY);
    }

    public Status playTurn(Hexagone hexToPlay){
        hexToPlay.setStatus(currentPlayer);
        board[hexToPlay.getX_ind()][hexToPlay.getY_ind()] = hexToPlay;
        lastMove = hexToPlay;
        turn++;
        updateCurrentPlayer();
        return hexToPlay.getStatus();
    }

    public void updateCurrentPlayer(){
        if (turn % 2 == 0)
            currentPlayer = firstPlayer;
        else
            currentPlayer = secondPlayer;
    }

    public boolean isValid(Hexagone hexToCheck){
        return hexToCheck.getStatus() == Status.EMPTY;
    }

    public boolean checkWinner(){
        boolean hasWinner = false;
        ArrayList<Hexagone> livingPawns = new ArrayList<>();
        for (int y = 0; y < SIZE; y++)
            if (board[0][y].getStatus() == Status.LIVING)
                livingPawns.add(board[0][y]);
        for (int i = 0; i < livingPawns.size() && !hasWinner; i++)
            hasWinner = recursiveCheckWinner(livingPawns.get(i), new ArrayList<Hexagone>(), Status.LIVING);

        if (!hasWinner){
            ArrayList<Hexagone> deadPawns = new ArrayList<>();
            for (int x = 0; x < SIZE; x++)
                if(board[x][0].getStatus() == Status.DEAD)
                    deadPawns.add(board[x][0]);
            for (int i = 0; i < deadPawns.size() && !hasWinner; i++)
                hasWinner = recursiveCheckWinner(deadPawns.get(i), new ArrayList<Hexagone>(), Status.DEAD);
        }
        return hasWinner;
    }

    private boolean recursiveCheckWinner(Hexagone currentHex, ArrayList<Hexagone> viewList, Status status){
        boolean hasWinner = false;
        if (!viewList.contains(currentHex)){
            viewList.add(currentHex);
            for (int i = -1; i <= 1 && !hasWinner; i++)
                for (int j = -1; j <= 1 && !hasWinner; j++)
                    if (isValidNeighbour(currentHex, i, j) && !viewList.contains(board[currentHex.getX_ind()+i][currentHex.getY_ind()+j]))
                        if (status == Status.DEAD)
                            if (currentHex.getY_ind()+j == SIZE-1) {
                                viewList.add(board[currentHex.getX_ind()][currentHex.getY_ind()+j]);
                                viewWinner = viewList;
                                hasWinner = true;
                            }else
                                hasWinner = recursiveCheckWinner(board[currentHex.getX_ind() + i][currentHex.getY_ind() + j], viewList, status);
                        else
                            if (currentHex.getX_ind()+i == SIZE-1) {
                                viewList.add(board[currentHex.getX_ind()+i][currentHex.getY_ind()]);
                                viewWinner = viewList;
                                hasWinner = true;
                            }else
                                hasWinner = recursiveCheckWinner(board[currentHex.getX_ind() + i][currentHex.getY_ind() + j], viewList, status);
        }
        return hasWinner;
    }


    private boolean isValidNeighbour(Hexagone currentHex, int i, int j){
        if (i != j)
            if (currentHex.getX_ind() + i < SIZE && currentHex.getX_ind() + i >= 0)
                if (currentHex.getY_ind() + j < SIZE && currentHex.getY_ind() + j >= 0)
                    if (board[currentHex.getX_ind()+i][currentHex.getY_ind()+j].getStatus() == currentHex.getStatus())
                        return true;
        return false;
    }


    public int getSIZE() {
        return SIZE;
    }

    public Activity getContext() {
        return context;
    }

    public Hexagone[][] getBoard() {
        return board;
    }

    public Hexagone getLastMove() {
        return lastMove;
    }

    public void setLastMove(Hexagone lastMove) {
        this.lastMove = lastMove;
    }

    public void decreaseTurn(){
        turn--;
    }


    public void setFirstPlayer(Status firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public void setSecondPlayer(Status secondPlayer) {
        this.secondPlayer = secondPlayer;
    }

    public Status getCurrentPlayer() {
        return currentPlayer;
    }

    public ArrayList<Hexagone> getViewWinner() {
        return viewWinner;
    }
}
