package GUI;

import Utils.KeyInput;

import javax.swing.*;
import App.*;
import Utils.SoundUtil;

import java.awt.event.KeyEvent;

/**
 * Created by shaha on 05/01/2018.
 */
public class MainFrame extends JFrame {
    private final String TITLE = "3D Narration for web browser";
    private boolean keyIsPressed;

    public MainFrame(){
        setTitle(TITLE);
        setSize(800,600);
        setVisible(true);
    }

    /**
     * init some basic settings
     */
    public void init() {
        //add key listener
        keyIsPressed = false;
        this.addKeyListener(new KeyInput(this));
    }

    public void keyPressed(KeyEvent e) {
        if(!keyIsPressed) {
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    System.out.println("You pressed left");
                    break;
                case KeyEvent.VK_RIGHT:
                    System.out.println("You pressed right");
                    SoundUtil.rotateRight();
                    SoundUtil.updateSources();
                    SoundUtil.playTags();
                    break;
                case KeyEvent.VK_1:
                    SoundUtil.rotateLeft();
                    SoundUtil.updateSources();
                    SoundUtil.playTags();
                    break;
                case KeyEvent.VK_P:
                    if(SoundUtil.getIsReady()){
                        //play sound
                        SoundUtil.playTags();
                    }
                    break;
            }
            keyIsPressed = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        System.out.println("Released a key "+e.getKeyChar());
        keyIsPressed = false;
    }
}
