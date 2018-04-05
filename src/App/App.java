package App;

import GUI.MainFrame;
import Utils.SoundUtil;
import Utils.Tag;
import Utils.WebUtil;

import javax.sound.sampled.AudioInputStream;
import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the main class
 * from here we call to all the parts of our program and connect them into
 * one system
 */
public class App {
    private static final float LISTENER_POS_X = 0, LISTENER_POS_Y = 0, LISTENER_POS_Z = 0;
    //private static final String URL_LINK = "http://edition.cnn.com/US/OJ/"; //the web page url
    public static String URL_LINK = "https://www.vox.com/"; //the web page url
    public static OutputStream STREAM;
    public static AudioInputStream AIS;
    public static boolean FIRST_TIME = true;
    public static byte[] BYTES;
    public static AudioInputStream ais;

    public static void main(String[] args) {
        //1. create an engine
        Engine app = new Engine();
        //2. start the engine
        app.start();




//        System.out.println("Connecting to " + URL_LINK);
//        //1. connect to the web
//        WebUtil.connectToWebsite(URL_LINK);
//
//        //init voices
//        SoundUtil.initVoices();
        //2. extract the tags from the web

        //test vox - daniel
        //testVox();
        /*
        Map<String,ArrayList<Tag>> navsDictionary = WebUtil.getItems();
        HashMap<String,ArrayList<Tag>> navsDict = (HashMap<String, ArrayList<Tag>>) navsDictionary;
        //
        SoundUtil.setDictionaryTags(navsDict);

        //3. create the wav files from the tagss
        AudioMaster.init();
        AudioMaster.setListenerData(LISTENER_POS_X, LISTENER_POS_Y, LISTENER_POS_Z);

        System.out.println(WaveData.class.getClassLoader().getResource("").getPath());

        //SoundUtil.createWavFiles(tags);
        //4. play them
        //SoundUtil.playBuffer();
//        SoundUtil.createSources(tags);
        //SoundUtil.playTags();
        MainFrame app = new MainFrame();
        app.init();

        //test - print all the tags
        //testTags(tags);

        //open GUI
        */

    }

    private static void testVox() {
        Map<String,ArrayList<Tag>> items = WebUtil.getVoxItems();
        HashMap<String,ArrayList<Tag>> itemsDict = (HashMap<String, ArrayList<Tag>>) items; // WHY ??
        SoundUtil.setDictionaryTags(itemsDict);
        AudioMaster.init();
        AudioMaster.setListenerData(LISTENER_POS_X, LISTENER_POS_Y, LISTENER_POS_Z);
        //MainFrame app = new MainFrame();
        //app.init();
    }

    private static void testTags(ArrayList<String> tags) {
        for (String tag : tags) {
            System.out.println(tag);
        }
    }
}
