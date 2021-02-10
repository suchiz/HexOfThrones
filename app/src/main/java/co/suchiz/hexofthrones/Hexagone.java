package co.suchiz.hexofthrones;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;

public class Hexagone extends AppCompatButton {
    private int x_ind;
    private int y_ind;
    private Status status;

    public Hexagone(Context context, int x_ind, int y_ind, Status status) {
        super(context);
        this.x_ind = x_ind;
        this.y_ind = y_ind;
        this.status = status;
    }

    public void resize(int w, int h){
        this.requestLayout();
        this.getLayoutParams().width = w;
        this.getLayoutParams().height = h;
    }


    @Override
    public String toString() {
        return "("+ x_ind + "," + y_ind + ")";
    }

    public int getX_ind() {
        return x_ind;
    }

    public int getY_ind() {
        return y_ind;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
