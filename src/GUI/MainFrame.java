package GUI;

import App.Engine;
import Utils.Scrapper.*;
import Utils.SoundUtil;
import Utils.UserInput.InputHandler;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by shaha on 05/01/2018.
 */
public class MainFrame extends JFrame {
    private final String TITLE = "3D Narration for web browser";
    private InputHandler.KeyHandlerSelection keyListener;
    private Engine engine;

    private InputHandler.MouseHandler mouseListener;


    public MainFrame(Engine engine) {
        setTitle(TITLE);

        //save the engine instance
        this.engine = engine;

        //Open the window on full screen
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    public void setMouseListener() {
        mouseListener = new InputHandler.MouseHandler(this);
        this.addMouseMotionListener(mouseListener);
    }

    public void selectOption() {
        //add a key listener to handle the user input
        keyListener = new InputHandler.KeyHandlerSelection(this, 1);
        this.addKeyListener(keyListener);

        //1. play the different options to the user

        //Visual presentation of the options
        JLabel label = new JLabel("<html>For The verge website press 1 <br>" +
                "For Vox news website press 2 <br>" +
                "For Football365 website press 3 <br>" +
                "To repeat the options press 4</html>", SwingConstants.CENTER);
        label.setFont(new Font("Serif", Font.BOLD, 28));

        add(label, BorderLayout.CENTER);

        //update the gui
        revalidate();
    }

    public void removeSelectionListener() {
        //remove the key listener
        this.removeKeyListener(keyListener);
    }

    /**
     * This function determine which scrapper to use
     *
     * @param option 1 - OJ Simpson website
     *               2 - VOX website
     *               3 - SkySports website
     */
    public void setScrapper(char option) throws IOException {
        AScrapper scrapper = null;
        switch (option) {
            case '1':
                //create new oj scrapper
                SoundUtil.playChoice("theverge.wav");
                scrapper = new TheveargeScrapper();
                break;
            case '2':
                SoundUtil.playChoice("vox.wav");
                scrapper = new VoxScrapper();
                break;
            case '3':
                SoundUtil.playChoice("football365.wav");
                scrapper = new Football365Scrapper();
                break;
        }

        engine.startScrapping(scrapper);
        SoundUtil.playChoice("startnavigation.wav");
    }

    public void removeListeners(){
        this.removeKeyListener(keyListener);
        this.removeMouseMotionListener(mouseListener);


    }

    public void setScrapperListener() {
        //add a key listener to handle the user input
        keyListener = new InputHandler.KeyHandlerSelection(this, 2);
        this.addKeyListener(keyListener);
    }

    public void removeMouseListener() {
        this.removeMouseMotionListener(mouseListener);
    }
}
