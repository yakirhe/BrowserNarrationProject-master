package Utils;

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

    public static void createWavFiles(ArrayList<String> tags) {
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
            String fileName = tags.get(i);
            //replace the '.' with blank space
            fileName = fileName.replaceAll("[^A-Za-z0-9 ]","");

            tts.setVoice(voices.get(i%6));
            tts.saveToFile(tags.get(i), 1.0f, false, false, String.valueOf(i));

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

    public static void createSources(ArrayList<String> tags) {
        int elemntNum = 0;
        bufferList = new ArrayList<Integer>();
        sourceList = new ArrayList<Source>();
        double xInc = 40.000 / 2;
        double yInc = 20.0 / 3.0;
        for (int i = 0; i < tags.size(); i++) {
            final int buffer = AudioMaster.loadSound(String.valueOf(i) + ".wav");
            bufferList.add(buffer);
            final Source source = new Source();
            source.setPitch((float) 0.8);
            source.setLooping(true);
            //the center source, special position(with y)
            if (i % 2 == 0) {
                source.setPosition((float) (-20 + (i * xInc)), 10, 0);
            }else{
                source.setPosition((float) (-20 + (i * xInc)), 0, 0);
            }
            System.out.println("x : " + (float) (-20 + (i * xInc)));
            System.out.println("y : " + (float) (0 + (i * yInc)));
            source.setLooping(true);
            sourceList.add(source);
            //
//            source.play(buffer);
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //new state, reset valus
            if(i%3 == 0){
                xInc = 40.000/2;
                yInc = 20.0/3.0;
            }
        }
        try {
            Thread.sleep(1000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        AudioMaster.cleanUp();

    }

    public static void playTags() {
        for(int i = 0; i < sourceList.size();i++){
            sourceList.get(i).play(bufferList.get(i));
        }
    }
}
