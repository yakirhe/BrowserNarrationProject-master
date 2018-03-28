package Utils.Keys;

import GUI.MainFrame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by shaha on 28/03/
 * This class will be in charge of all the key events in our project
 */
public class KeyHandler {
    public static class KeyHandlerSelection implements KeyListener {
        private final MainFrame app;

        public KeyHandlerSelection(MainFrame app) {
            this.app = app;
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            //handle the user selection
            char c = e.getKeyChar();
            System.out.println("The user pressed " + c); //log for our use
            if (c == '1' || c == '2' || c == '3') {
                //remove the key listener from the app
                app.removeSelectionListener();

                //set the selected choice to use the appropriate scrapper
                app.setScrapper(c);
            } else {
                System.out.println("The user have to choose again"); //log for our use
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }
}
