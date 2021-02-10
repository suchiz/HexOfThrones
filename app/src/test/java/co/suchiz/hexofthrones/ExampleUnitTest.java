package co.suchiz.hexofthrones;

import android.app.Activity;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private Board b;
    @BeforeClass
    public static void init(){
        Activity activity = new Activity();
        Board b = new Board(activity, 7);
    }
    @Test (expected = NullPointerException.class)
    public void boardNotInitialized(){
        b.getBoard();
    }
    @Test (expected = NullPointerException.class)
    public void boardOutOfBound(){
        b.createNewGame();
        Hexagone hex = b.getBoard()[7][9];
    }
}