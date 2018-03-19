package Utils;

import App.App;
import App.AudioMaster;
import App.Source;

import javax.sound.sampled.AudioInputStream;
import java.util.*;

public class SoundUtil {
    private static ArrayList<String> voices = null;
    private static ArrayList<Integer> bufferList;
    private static ArrayList<Source> sourceList;
    public static boolean isReady = false;
    private static int mCurrentTag;
    private static HashMap<String, ArrayList<Tag>> _tags = new HashMap<>();
    private static HashMap<String, ArrayList<Tag>> articleMap = new HashMap<>();
    private static Integer navBuffer;
    private static int curNav = -1;
    private static Tag navTag;
    private static Source navSource;
    private static ArrayList<Tag> mMainNavTags;

    public static String getMkey() {
        return mkey;
    }

    public static void setMkey(String mkey) {
        SoundUtil.mkey = mkey;
    }

    private static String mkey;

    public static void setDictionaryTags(HashMap<String, ArrayList<Tag>> tags) {
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
        sourceList.get(1).setVolume(midSource);
        sourceList.get(2).setVolume(rightSource);
    }

    /**
     * Create 3 sources that remain permanent!!
     * Only the buffer change
     */
    public static void createSources(int curnav) {
        curNav = curnav;
        String key = getKeyByPlace(curnav);
        StopAllPrevSources();
        double xInc = 40.000 / 2;
        double yInc = 20.0 / 3.0;

        //init the voices
        initVoices();

        for (int i = 0; i < 3; i++) {
            if (_tags.size() < i) {
                return;
            }

            _tags.get(key).get(i).setVoice(voices.get(i + 2));
//            tags.get(i).setVoice(voices.get(0));
            int buffer = AudioMaster.loadSound(createTTSTag(_tags.get(key).get(i)));
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

    private static void StopAllPrevSources() {
        for (Source source : sourceList
                ) {
            source.stop();
            source.delete();
        }
        bufferList.clear();
        sourceList = new ArrayList<>();
        bufferList = new ArrayList<>();
    }

    private static String getKeyByPlace(int curnav) {
        Set<String> set = _tags.keySet();
        String[] array = new String[set.size()];
        set.toArray(array);
        mkey = array[curnav];
        return mkey;
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
        mCurrentTag = (mCurrentTag + 3) % _tags.get(mkey).size();

    }

    public static void updateSources() {
        //lets set a voice for 3 tags lets see its different , idea : the voice will be diff from the previous tags
        //idea : if i have a voice dont change it
        update3NextTagsVoices();
        int bufferLeft = AudioMaster.loadSound(createTTSTag(_tags.get(mkey).get(mCurrentTag)));
        int bufferCenter = AudioMaster.loadSound(createTTSTag(_tags.get(mkey).get(mCurrentTag + 1)));
        int bufferRight = AudioMaster.loadSound(createTTSTag(_tags.get(mkey).get(mCurrentTag + 2)));
//        int bufferLeft = AudioMaster.loadSound(createTTSTag(tags.get(mCurrentTag)));
//        int bufferCenter = AudioMaster.loadSound(createTTSTag(tags.get(mCurrentTag + 1)));
//        int bufferRight = AudioMaster.loadSound(createTTSTag(tags.get(mCurrentTag + 2)));
        bufferList.set(0, bufferRight);
        bufferList.set(1, bufferCenter);
        bufferList.set(2, bufferLeft);
    }

    private static void update3NextTagsVoices() {
        ArrayList<String> voices = get3RandomVoices();
        for (int i = 0; i < 3; i++) {
            if (_tags.get(mkey).get(mCurrentTag + i).getVoice() == null)
                _tags.get(mkey).get(mCurrentTag + i).setVoice(voices.get(i));
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
        if (mCurrentTag == 0) {
            mCurrentTag = _tags.get(mkey).size() - 3;
        } else {
            mCurrentTag = (mCurrentTag - 3) % _tags.get(mkey).size();
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
     * this function set source with nav name
     */
    public static void navigateTraversal() {
        if (navSource != null) {
            navSource.stop();
        }
        //
        curNav = ++curNav % _tags.size();
        navTag = new Tag((String) (_tags.keySet().toArray()[curNav]), (String) (_tags.keySet().toArray()[curNav]), Type.TEXT);
        navTag.setVoice(voices.get(0));
        navBuffer = AudioMaster.loadSound(createTTSTag(navTag));
        navSource = new Source();
        navSource.setBuffer(navBuffer);
        navSource.setPosition(0, 0, 0);
        navSource.setLooping(true);
        navSource.play();
    }

    public static void createMainNavSource() {
        curNav = 0;
        sourceList = new ArrayList<>();
        bufferList = new ArrayList<>();
        double xInc = 40.000 / 2;
        double yInc = 20.0 / 3.0;

        //init the voices
        initVoices();

        for (int i = 0; i < _tags.keySet().size(); i++) {

            mMainNavTags = createMainNavTagsFromKeys(_tags.keySet());
            //warning will fall if more than 2 nav option
            mMainNavTags.get(i).setVoice(voices.get(i + 3));
//            _tags.get(curNav).get(i).setVoice(voices.get(i+2));
//            tags.get(i).setVoice(voices.get(0));
            int buffer = AudioMaster.loadSound(createTTSTag(mMainNavTags.get(i)));
            bufferList.add(buffer);
            final Source source = new Source();
            source.setBuffer(buffer);
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
        mCurrentTag = 0;


    }

    private static ArrayList<Tag> createMainNavTagsFromKeys(Set<String> strings) {
        ArrayList<Tag> tags = new ArrayList<>();
        for (String key : strings
                ) {
            Tag t = new Tag(key, key, Type.MAINNAVTAG);
            tags.add(t);
        }
        return tags;
    }

    /**
     * return the url of selected tag from the user and update the new mKey to be the name(the next function will be to parse and add the new array of tags to _tags
     *
     * @param keyPress
     * @return
     */
    public static String getUrlFromSelectedTag(int keyPress) {
        String url = "";
        switch (keyPress) {
            case 1:
                url = _tags.get(mkey).get(mCurrentTag).getUrl();
                mkey = _tags.get(mkey).get(mCurrentTag).getName();
                break;
            case 2:
                url = _tags.get(mkey).get(mCurrentTag + 1).getUrl();
                mkey = _tags.get(mkey).get(mCurrentTag + 1).getName();
                break;
            case 3:
                url = _tags.get(mkey).get(mCurrentTag + 2).getUrl();
                mkey = _tags.get(mkey).get(mCurrentTag + 2).getName();
                break;
        }
        return url;
    }

    /**
     * create one source for reading the article from vox site;
     */
    public static void createAndUpdateOneSourceArticle() {
        if (articleMap.keySet().size() > 0)
            articleMap.clear();
        articleMap = WebUtil.getVoxArticle();
        StopAllPrevSources();
        initVoices();
        Tag articleTag = articleMap.get("article").get(0);
        //set voice to article tag -> idea: lets chose the best voice not random , and if we going to pronounce the headlines than maybe 2 source with to different voices
        articleTag.setVoice(getVoices().get(4));
        bufferList.add(AudioMaster.loadSound(createTTSTag(articleTag)));
        Source source = new Source();
        source.setBuffer(bufferList.get(0));
        source.setPosition(0, 0, 0);
        source.setLooping(true);
        sourceList.add(source);
    }

    public static void setNewSpeedToSources(float newSpeed) {
        for (Source source : sourceList
                ) {
            source.setPitch(newSpeed);
        }
    }
}
