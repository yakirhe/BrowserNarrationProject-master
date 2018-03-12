package Utils;

import App.App;
import App.AudioMaster;
import App.Source;
import org.lwjgl.egl.EGLClientPixmapHI;

import javax.sound.sampled.AudioInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SoundUtil {
    private static ArrayList<String> voices = null;
    private static ArrayList<Integer> bufferList;
    private static ArrayList<Source> sourceList;
    public static boolean isReady = false;
    private static int mCurrentTag;
    private static HashMap<String,ArrayList<Tag>> _tags = new HashMap<>();
    private static Integer navBuffer;
    private static int curNav = -1;
    private static Tag navTag;
    private static  Source navSource;

    public static void setDictionaryTags(HashMap<String,ArrayList<Tag>> tags) {
        _tags = tags;
    }

    public static void initVoices() {
        voices = new ArrayList<>();
        voices.add("cmu-slt-hsmm");
        voices.add("dfki-obadiah-hsmm");
        voices.add("dfki-spike-hsmm");
        voices.add("cmu-bdl-hsmm");
        voices.add("cmu-rms-hsmm");
        voices.add("dfki-poppy-hsmm");
    }

    /**
     * Create 3 sources that remain permanent!!
     * Only the buffer change
     *
     */
    public static void createSources() {
        sourceList = new ArrayList<Source>();
        bufferList = new ArrayList<>();
        double xInc = 40.000 / 2;
        double yInc = 20.0 / 3.0;

        //init the voices
        initVoices();

        for (int i = 0; i < 3; i++) {
            if (_tags.size() < i) {
                return;
            }

            _tags.get(curNav).get(i).setVoice(voices.get(0));
//            tags.get(i).setVoice(voices.get(0));
            int buffer = AudioMaster.loadSound(createTTSTag(_tags.get(curNav).get(i)));
            bufferList.add(buffer);
            final Source source = new Source();
            source.setBuffer(buffer);

            //source.setPitch((float) 0.8);

            //the center source, special position(with y)
            if (i % 2 == 1) {
                source.setPosition((float) (-20 + (i * xInc)), 10, 0);
            } else {
                source.setPosition((float) (-20 + (i * xInc)), 0, 0);
            }

//            System.out.println("x : " + (float) (-20 + (i * xInc)));
//            System.out.println("y : " + (float) (0 + (i * yInc)));
            source.setLooping(true);
            sourceList.add(source);

        }
        System.out.println("Ready");
        mCurrentTag = 0;
        isReady = true;
    }

    public static void playTags() {
        isReady = false;
        System.out.println("In play tags");
        for (int i = 0; i < sourceList.size(); i++) {
            sourceList.get(i).play(bufferList.get(i));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        AudioMaster.cleanUp();
        isReady = true;
    }

    public static boolean getIsReady() {
        return isReady;
    }

    public static void rotateRight() {
        mCurrentTag = (mCurrentTag + 3) % _tags.get(curNav).size();
        ;
    }

    public static void updateSources() {
        int bufferLeft = AudioMaster.loadSound(createTTSTag(_tags.get(curNav).get(mCurrentTag)));
        int bufferCenter = AudioMaster.loadSound(createTTSTag(_tags.get(curNav).get(mCurrentTag+1)));
        int bufferRight = AudioMaster.loadSound(createTTSTag(_tags.get(curNav).get(mCurrentTag+2)));
//        int bufferLeft = AudioMaster.loadSound(createTTSTag(tags.get(mCurrentTag)));
//        int bufferCenter = AudioMaster.loadSound(createTTSTag(tags.get(mCurrentTag + 1)));
//        int bufferRight = AudioMaster.loadSound(createTTSTag(tags.get(mCurrentTag + 2)));
        bufferList.set(0, bufferRight);
        bufferList.set(1, bufferCenter);
        bufferList.set(2, bufferLeft);
    }

    public static void rotateLeft() {
        if (mCurrentTag == 0) {
            mCurrentTag = _tags.get(curNav).size() - 3;
        } else {
            mCurrentTag = (mCurrentTag - 3) % _tags.get(curNav).size();
        }
    }

    public static void playBuffer() {
        int buffer = AudioMaster.loadSound(App.ais);
        final Source source = new Source();
        source.setBuffer(buffer);
        source.setLooping(true);
        source.play();
    }

    /**
     * Create a text to speech Audio Input Stream
     * And store it the data structure
     *
     * @param tag
     */
    public static AudioInputStream createTTSTag(Tag tag) {

        TextToSpeech tts = new TextToSpeech();
        tts.setVoice(tag.getVoice());
        return tts.createAudioInputStream(tag.getContent());
    }

    public static ArrayList<String> getVoices() {
        if (voices == null) {
            initVoices();
        }

        return voices;
    }

    /**
     * this function set source with nav name
     */
    public static void navigateTraversal() {
        if(navSource!=null){
            navSource.stop();
        }
        //
        curNav = ++curNav%_tags.size();
        navTag = new Tag((String)(_tags.keySet().toArray()[curNav]),(String)(_tags.keySet().toArray()[curNav]),Type.TEXT);
        navTag.setVoice(voices.get(0));
        navBuffer =  AudioMaster.loadSound(createTTSTag(navTag));
        navSource = new Source();
        navSource.setBuffer(navBuffer);
        navSource.setPosition(0,0,0);
        navSource.setLooping(true);
        navSource.play();
    }

}
