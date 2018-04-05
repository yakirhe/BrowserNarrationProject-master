package App;

import GUI.MainFrame;
import Utils.Scrapper.AScrapper;
import Utils.SoundUtil;

import java.util.Scanner;

/**
 * Created by shaha on 28/03/2018.
 * The main worker of our project
 * Is in charge of connecting all the classes that we use and
 * activate them in a logical order
 */
public class Engine {
    private MainFrame mainFrame;

    /**
     * This is the default constructor
     */
    public Engine() {
        System.out.println("Started Engine");
    }

    /**
     * This function is in charge of all of our logic
     * and the way and order we execute things
     */
    public void start() {
        //open the GUI window
        mainFrame = new MainFrame(this);
        //1. play the instructions
        playInstructions();
        //2. display the options and get the user input
        selectOption();
        //3. init voices
        SoundUtil.initVoices();
    }

    private void selectOption() {
        //call the GUI function to handle user input
        mainFrame.selectOption();
    }

    private void playInstructions() {
        //play the wav file of the instructions
        System.out.println("---------------------------------------");
        System.out.println("-------------Instructions--------------");
    }

    /**
     * This function calls the scrapper and tells him to scrape all the required details that we need
     * @param scrapper
     */
    public void startScrapping(AScrapper scrapper) {
        scrapper.getItems();
    }
}
