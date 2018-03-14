package GUI;

import Utils.KeyInput;

import javax.swing.*;

import App.*;
import Utils.SoundUtil;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * Created by shaha on 05/01/2018.
 */
public class MainFrame extends JFrame {
    private final String TITLE = "3D Narration for web browser";
    private boolean keyIsPressed;
    private int frameMaxWidth = 800;
    private int frameMaxHeight = 600;
    private JPanel jPanel;
    private boolean mMouseMoveDetection;
    private boolean mainNavMode;
    private int currentMap;


    public MainFrame() {
        setTitle(TITLE);
        setSize(frameMaxWidth, frameMaxHeight);
        jPanel = new JPanel();
        mMouseMoveDetection = false;
        this.add(jPanel);
        setVisible(true);
        mainNavMode = false;

    }

    /**
     * init some basic settings
     */
    public void init() {
        //add key listener
        keyIsPressed = false;
        this.addKeyListener(new KeyInput(this));
        //add mouse listener
        jPanel.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (MouseMoveDetection()) {
                    float dX = e.getX();
                    float dY = e.getY();
                    System.out.println("X : " + dX);
                    System.out.println("Y : " + dY);
                    //check if needed to make a make quit zone around the mid source.
                    //pro : better sound of the mid source.
                    //con : in the start we wont all 3 sentence to be heard simultaneity
                    //idea: in the zone silence v1+v3 in half (0.25f)
                    float newVolume1 = getFirstSourceNewVolume(dX, dY);
                    float newVolume2 = getSecondSourceNewVolume(dX, dY);
                    float newVolume3 = getThirdSourceNewVolume(dX, dY);
                    setNewVolumeToSources(newVolume1, newVolume2, newVolume3);
                }

            }

            /**
             * check if need to sense mouse moving and update source volume;
             * @return boolean
             */
            private boolean MouseMoveDetection() {
                return mMouseMoveDetection;
            }

            private void setNewVolumeToSources(float newVolume1, float newVolume2, float newVolume3) {
                SoundUtil.updateSourcesVolume(newVolume1, newVolume2, newVolume3);
            }

            private float getThirdSourceNewVolume(float dX, float dY) {
                float ans = dX / frameMaxWidth;
//                if (dX<=(frameMaxWidth*(5/8)))
//                    ans=0;
                System.out.println("New V3 : " + ans);
                return ans;
            }

            private float getSecondSourceNewVolume(float dX, float dY) {
                float ans = 0;
                if (dX >= (frameMaxWidth / 2))
                    ans = (1 - (dX / frameMaxWidth)) * 2;
                else
                    ans = (dX / frameMaxWidth) * 2;
                System.out.println("New V2 : " + ans);
                return ans;
            }

            private float getFirstSourceNewVolume(float dX, float dY) {
                float ans = 1 - (dX / frameMaxWidth);
//                if (dX>=(frameMaxWidth*(3/8)))
//                    ans=0;
                System.out.println("New V1 : " + ans);
                return ans;
            }
        });

    }

    public void keyPressed(KeyEvent e) {
        if (!keyIsPressed) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    System.out.println("You pressed left");
                    SoundUtil.rotateLeft();
                    SoundUtil.updateSources();
                    SoundUtil.playTags();
                    break;
                case KeyEvent.VK_RIGHT:
                    System.out.println("You pressed right");
                    SoundUtil.rotateRight();
                    SoundUtil.updateSources();
                    SoundUtil.playTags();
                    break;
                case KeyEvent.VK_N:
                    System.out.println("You Pressed N");
                    SoundUtil.navigateTraversal();
                    break;
                case KeyEvent.VK_1:
                    /*
                    if we in main nav mode that key that pressed will enter a require nav list create new sources and buffers from the require list
                    if we not in main nav mode we will
                     */
                    createAndPlaySourcesFromKeyPress(1);
                    break;

                case KeyEvent.VK_2:
                    createAndPlaySourcesFromKeyPress(2);
                    break;
                case KeyEvent.VK_P:
                    //play sound
                    //need to rethink what will happen after we press p
                    /*
                        lets start with reading and chose fromm all possible array in the Map in vox case 2 thing -> nav bar and headline
                        then we select 1 or 2 , 1->read the nav bar 2->read the headline list
                     */
                    SoundUtil.createMainNavSource();
                    SoundUtil.playTags();
                    mainNavMode = true;
//                    mMouseMoveDetection = true;
//                    SoundUtil.createSources(); // ->will fall because curnav = -1 ;
//                    SoundUtil.playTags();
                    break;
                case KeyEvent.VK_ENTER:
                    SoundUtil.createSources(0);
                    break;
            }
            keyIsPressed = true;
        }
    }

    /**
     * create the new sound sources load the buffer of the selected item
     * @param keyPress
     *
     */
    private void createAndPlaySourcesFromKeyPress(int keyPress) {
        if (mainNavMode){
            mainNavMode = false;
            SoundUtil.createSources(keyPress-1);
            mMouseMoveDetection=true;
            SoundUtil.playTags();
            mainNavMode = false;
            currentMap = keyPress-1;
        }else{
            SoundUtil.rotateLeft();
            SoundUtil.updateSources();
            SoundUtil.playTags();
        }
    }

    public void keyReleased(KeyEvent e) {
        System.out.println("Released a key " + e.getKeyChar());
        keyIsPressed = false;
    }
}
