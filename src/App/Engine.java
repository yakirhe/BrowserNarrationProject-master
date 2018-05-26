package App;

import GUI.MainFrame;
import Utils.Scrapper.AScrapper;
import Utils.SoundUtil;
import Utils.Tag;

import java.util.*;

/**
 * Created by shaha on 28/03/2018.
 * The main worker of our project
 * Is in charge of connecting all the classes that we use and
 * activate them in a logical order
 */
public class Engine {
    private static final float LISTENER_POS_X = 0, LISTENER_POS_Y = 0, LISTENER_POS_Z = 0;
    private MainFrame mainFrame;

    public static AScrapper getaScrapper() {
        return aScrapper;
    }

    public void setaScrapper(AScrapper aScrapper) {
        this.aScrapper = aScrapper;
    }

    private static AScrapper aScrapper;

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
    public void start(boolean isFirstPlay) {
        //open the GUI window
        if(isFirstPlay) {
            mainFrame = new MainFrame(this);
        }
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
        SoundUtil.playInstructions();
    }

    /**
     * This function calls the scrapper and tells him to scrape all the required details that we need
     *
     * @param scrapper
     */
    public void startScrapping(AScrapper scrapper) {
        //get the items from the home page of the specific website
        setaScrapper(scrapper);
        Map<String, List<Tag>> items = scrapper.getItems();
        SoundUtil.setDictionaryTags(items);

        //sound initialization
        AudioMaster.init();
        AudioMaster.setListenerData(LISTENER_POS_X, LISTENER_POS_Y, LISTENER_POS_Z);

        //set the key listener to the scrapper listener
        mainFrame.setScrapperListener();
    }

}
