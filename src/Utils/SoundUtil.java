package Utils;

import App.App;
import App.AudioMaster;
import App.Source;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SoundUtil {
    private static final ArrayList<String> voices = new ArrayList<>();
    private static ArrayList<Integer> bufferList;
    private static ArrayList<Source> sourceList;
    private static boolean isReady = false;
    private static int mCurrentTag;

    public static void setTags(ArrayList<Tag> tags) {
        SoundUtil.tags = tags;
    }

    private static ArrayList<Tag> tags = new ArrayList<>();

    public static void createWavFiles(ArrayList<Tag> tags) {
        //clear audio directory
        try {
            FileUtils.deleteDirectory(new File("audio"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //init the voices
        initVoices();

        TextToSpeech tts = new TextToSpeech();
        for (int i = 0; i < tags.size(); i++) {
            tts.setVoice(voices.get(i % 6));
            tts.saveToFile(tags.get(i).getContent(), 1.0f, false, false, tags.get(i).getFileName());
        }
    }

    private static void initVoices() {
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
     * @param tags
     */
    public static void createSources(ArrayList<Tag> tags) {
        sourceList = new ArrayList<Source>();
        bufferList = new ArrayList<>();
        double xInc = 40.000 / 2;
        double yInc = 20.0 / 3.0;
        for (int i = 0; i < 3; i++) {
            if (tags.size() < i) {
                return;
            }

            int buffer = AudioMaster.loadSound(tags.get(i).getFileName() + ".wav");
            bufferList.add(buffer);
            final Source source = new Source();
//            source.setBuffer(buffer);

            source.setPitch((float) 0.8);

            //the center source, special position(with y)
            if (i % 2 == 1) {
                source.setPosition((float) (-20 + (i * xInc)), 10, 0);
            } else {
                source.setPosition((float) (-20 + (i * xInc)), 0, 0);
            }

//            System.out.println("x : " + (float) (-20 + (i * xInc)));
//            System.out.println("y : " + (float) (0 + (i * yInc)));
            source.setLooping(true);
//            source.play(buffer);
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
        mCurrentTag = (mCurrentTag + 3) % tags.size();
        ;
    }

    public static void updateSources() {
        int bufferLeft = AudioMaster.loadSound(tags.get(mCurrentTag).getFileName() + ".wav");
        int bufferCenter = AudioMaster.loadSound(tags.get(mCurrentTag + 1).getFileName() + ".wav");
        int bufferRight = AudioMaster.loadSound(tags.get(mCurrentTag + 2).getFileName() + ".wav");
        bufferList.set(0, bufferRight);
        bufferList.set(1, bufferCenter);
        bufferList.set(2, bufferLeft);
    }

    public static void rotateLeft() {
        if (mCurrentTag == 0) {
            mCurrentTag = tags.size() - 3;
        } else {
            mCurrentTag = (mCurrentTag - 3) % tags.size();
            ;
        }
    }

    public static void playBuffer() {
        int buffer = AudioMaster.loadSound(App.BYTES);
        final Source source = new Source();
        source.setBuffer(buffer);
        source.setLooping(true);
        source.play();
    }
}
