package App;

import GUI.MainFrame;
import Utils.SoundUtil;
import Utils.Tag;
import Utils.WebUtil;
import java.util.ArrayList;

/**
 * This is the main class
 * from here we call to all the parts of our program and connect them into
 * one system
 */
public class App {
    private static final float LISTENER_POS_X = 0,LISTENER_POS_Y = 0,LISTENER_POS_Z = 0;
    private static final String URL_LINK = "http://edition.cnn.com/US/OJ/"; //the webpage url

    public static void main(String[] args){
        System.out.println("Connecting to "+ URL_LINK);
        //1. connect to the web
        WebUtil.connectToWebsite(URL_LINK);

        //2. extract the tags from the web
        ArrayList<Tag> tags = WebUtil.getItems();
        SoundUtil.setTags(tags);

        //3. create the wav files from the tagss
        AudioMaster.init();
        AudioMaster.setListenerData(LISTENER_POS_X,LISTENER_POS_Y,LISTENER_POS_Z);

        System.out.println(WaveData.class.getClassLoader().getResource("").getPath());
        SoundUtil.createWavFiles(tags);
        //4. play them



        SoundUtil.createSources(tags);
       // SoundUtil.playTags();

        MainFrame app = new MainFrame();
        app.init();

        //test - print all the tags
        //testTags(tags);

        //open GUI

    }

    private static void testTags(ArrayList<String> tags) {
        for (String tag:tags){
            System.out.println(tag);
        }
    }
}
