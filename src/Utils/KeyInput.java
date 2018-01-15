package Utils;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import GUI.MainFrame;

/**
 * Created by shaha on 06/01/2018.
 */
public class KeyInput extends KeyAdapter {
    MainFrame app;

    public KeyInput(MainFrame app){
        this.app = app;
    }

    public void keyPressed(KeyEvent e){
        app.keyPressed(e);
    }

    public void keyReleased(KeyEvent e){
        app.keyReleased(e);
    }
}
