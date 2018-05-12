package Utils;

import App.App;
import App.AudioMaster;
import App.Source;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.*;

public class SoundUtil {
    private static ArrayList<String> voices = null;
    private static ArrayList<Source> sourceList;
    public static boolean isReady = false;
    public static int mCurrentTag;
    private static Clip clip; //store the instruction wav file

    public static Map<String, List<Tag>> get_tags() {
        return _tags;
    }

    private static Map<String, List<Tag>> _tags = new HashMap<>();
    private static List<Tag> mTagsList;

    public static List<Tag> getmTagsList() {
        return mTagsList;
    }

    public static void setmTagsList(List<Tag> mTagsList) {
        SoundUtil.mTagsList = mTagsList;
    }

    public static NarrationMode getMode() {
        return m_mode;
    }

    public static void setMode(NarrationMode mode) {
        SoundUtil.m_mode = mode;
    }

    private static NarrationMode m_mode;

    public static void setDictionaryTags(Map<String, List<Tag>> tags) {
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
     * update  all 3 sources volume
     *
     * @param leftSource
     * @param midSource
     * @param rightSource
     */
    public static void updateSourcesVolume(float leftSource, float midSource, float rightSource) {
        sourceList.get(0).setVolume(leftSource);
        sourceList.get(2).setVolume(midSource);
        sourceList.get(1).setVolume(rightSource);
    }

    /**
     * Create 3 sources that remain permanent!!
     * Only the buffer change
     */
    public static void createSources() {
        sourceList = new ArrayList<Source>();
        StopAllPrevSources();

        double xInc = 40.000 / 2;
        double yInc = 20.0 / 3.0;

        //init the voices
        initVoices();

        initSources();

        System.out.println("Ready");

        //our current position
        mCurrentTag = 0;
    }

    private static void initSources() {
        final Source source1 = new Source();
        final Source source2 = new Source();
        final Source source3 = new Source();

        source1.setPosition(-20, 0, 0);
        source2.setPosition(20, 0, 0);
        source3.setPosition(0, 10, 0);

        source1.setLooping(true);
        source2.setLooping(true);
        source3.setLooping(true);

        sourceList.add(source1);
        sourceList.add(source2);
        sourceList.add(source3);
    }

    private static void StopAllPrevSources() {
        if (sourceList == null) {
            return;
        }

        for (Source source : sourceList) {
            source.stop();
        }
    }

    public static void playTags() {
        //clean the sources
        StopAllPrevSources();
        isReady = false;
        System.out.println("In play tags");
        if (m_mode == NarrationMode.ARTICLE) {
            sourceList.get(2).play();

        } else {
            for (int i = 0; i < sourceList.size(); i++) {
                sourceList.get(i).play();
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
        }
        isReady = true;
    }

    public static void rotateRight() {
        mCurrentTag = (mCurrentTag + 3) % mTagsList.size();

    }

    public static void updateSources() {
        //lets set a voice for 3 tags lets see its different , idea : the voice will be diff from the previous tags
        //idea : if i have a voice dont change it
        update3NextTagsVoices();
        int bufferLeft = AudioMaster.loadSound(createTTSTag(mTagsList.get(mCurrentTag)));
        int bufferCenter = AudioMaster.loadSound(createTTSTag(mTagsList.get(mCurrentTag + 1)));
        int bufferRight = AudioMaster.loadSound(createTTSTag(mTagsList.get(mCurrentTag + 2)));
        sourceList.get(0).setBuffer(bufferLeft);
        sourceList.get(1).setBuffer(bufferRight);
        sourceList.get(2).setBuffer(bufferCenter);
    }

    private static void update3NextTagsVoices() {
        ArrayList<String> voices = get3RandomVoices();
        for (int i = 0; i < 3; i++) {
            if (mTagsList.get(mCurrentTag + i).getVoice() == null)
                mTagsList.get(mCurrentTag + i).setVoice(voices.get(i));
        }
    }

    private static ArrayList<String> get3RandomVoices() {
        ArrayList<String> voices = new ArrayList<>();
        Random rand = new Random();
        while (voices.size() < 3) {
            int value = rand.nextInt(SoundUtil.voices.size());
            String voice = SoundUtil.voices.get(value);
            if (!voices.contains(voice))
                voices.add(voice);
        }
        return voices;
    }

    public static void rotateLeft() {
        mCurrentTag = mTagsList.size() - 3;
        if (mCurrentTag < 0) {
            mCurrentTag = (mTagsList.size() + mCurrentTag);
        } else {
            mCurrentTag = (mCurrentTag - 3) % mTagsList.size();
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
        TextToSpeech tts = new TextToSpeech(); // idea : load it one time on the load of the app because its take a lot of time ;
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
     * return the url of selected tag from the user and update the new mKey to be the name(the next function will be to parse and add the new array of tags to _tags
     *
     * @param keyPress
     * @return
     */
    public static String getUrlFromSelectedTag(int keyPress) {
        return mTagsList.get(mCurrentTag + keyPress - 1).getUrl();
    }

    public static void setNewSpeedToSources(float newSpeed) {
        for (Source source : sourceList
                ) {
            source.setPitch(newSpeed);
        }
    }

    public static void initBuffers(NarrationMode mode) {
        m_mode = mode;
        switch (mode) {
            case MAIN_NAVIGATION:
                ArrayList<String> keyList = new ArrayList<>(_tags.keySet());
                for (int i = 0; i < _tags.size(); i++) {
                    String tag = keyList.get(i);
                    int buffer = AudioMaster.loadSound(createTTSString(tag, 3 + i));
                    sourceList.get(i).setBuffer(buffer);
                }
                break;
            case SUB_NAVIGATION:
                for (int i = 0; i < 3; i++) {
                    mTagsList.get(i).setVoice(voices.get(3 + i));
                    int buffer = AudioMaster.loadSound(createTTSTag(mTagsList.get(i)));
                    sourceList.get(i).setBuffer(buffer);

                }
                break;
            case ARTICLE:
                mTagsList.get(0).setVoice(voices.get(3));
                int buffer = AudioMaster.loadSound(createTTSTag(mTagsList.get(0)));
                sourceList.get(2).setBuffer(buffer);
                break;
        }
    }

    private static AudioInputStream createTTSString(String tag, int voice) {
        TextToSpeech tts = new TextToSpeech();
        tts.setVoice(voices.get(voice));
        return tts.createAudioInputStream(tag);
    }

    public static void playInstructions() {
        String filename = "introduction.wav";
        try {
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new File(filename)));
            clip.start();
        } catch (Exception exc) {
            exc.printStackTrace(System.out);
        }
    }

    public static void stopInstruction() {
        clip.stop();
    }

    public static void playChoice(String filename) {
        try {
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new File(filename)));
            clip.start();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
}
