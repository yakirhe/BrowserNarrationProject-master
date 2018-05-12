package GUI;

import App.Engine;
import Utils.Scrapper.*;
import Utils.UserInput.InputHandler;

import javax.swing.*;

/**
 * Created by shaha on 05/01/2018.
 */
public class MainFrame extends JFrame {
    private final String TITLE = "3D Narration for web browser";
    private JPanel mainPanel;
    private InputHandler.KeyHandlerSelection keyListener;
    private Engine engine;

    private InputHandler.MouseHandler mouseListener;


    public MainFrame(Engine engine) {
        setTitle(TITLE);

        //save the engine instance
        this.engine = engine;

        //Open the window on full screen
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        //create the mainPanel
        mainPanel = new JPanel();
        add(mainPanel);

//        mMouseMoveDetection = false;
//        this.add(jPanel);
//        mainNavMode = false;
//        articleMode = false;
//        subNavMode = false;

    }

    public void setMouseListener() {
        mouseListener = new InputHandler.MouseHandler(this);
        this.addMouseMotionListener(mouseListener);
    }

    public void selectOption() {
        //add a key listener to handle the user input
        keyListener = new InputHandler.KeyHandlerSelection(this, 1);
        this.addKeyListener(keyListener);

        //1. play the different options to the user

        //Visual presentation of the options
        JLabel label = new JLabel("<html>For the OJ Simpson site press 1 <br>" +
                "For Vox news site press 2 <br>" +
                "For sky sports site press 3 <br>" +
                "To repeat the options press 4</html>");
        mainPanel.add(label);

        //update the gui
        revalidate();
    }

    public void removeSelectionListener() {
        //remove the key listener
        this.removeKeyListener(keyListener);
    }

    /**
     * This function determine which scrapper to use
     *
     * @param option 1 - OJ Simpson website
     *               2 - VOX website
     *               3 - SkySports website
     */
    public void setScrapper(char option) {
        AScrapper scrapper = null;
        switch (option) {
            case '1':
                //create new oj scrapper
                scrapper = new OJScrapper();
                break;
            case '2':
                scrapper = new VoxScrapper();
                break;
            case '3':
                scrapper = new SkySportsScrapper();
                break;
        }

        engine.startScrapping(scrapper);
    }

    public void setScrapperListener() {
        //add a key listener to handle the user input
        keyListener = new InputHandler.KeyHandlerSelection(this, 2);
        this.addKeyListener(keyListener);
    }

//    /**
//     * init some basic settings
//     */
//    public void init() {
//        //add key listener
//        keyIsPressed = false;
//        this.addKeyListener(new KeyInput(this));
//        //add mouse listener
//        jPanel.addMouseMotionListener(new MouseMotionListener() {
//            @Override
//            public void mouseDragged(MouseEvent e) {
//            }
//
//            @Override
//            public void mouseMoved(MouseEvent e) {
//                // use this site to help : https://mycurvefit.com/
//                if (MouseMoveDetection()) {
//                    float dX = e.getX();
//                    float dY = e.getY();
//                    System.out.println("X : " + dX);
//                    System.out.println("Y : " + dY);
//                    if (articleMode) {
//                        //will make the read X1.5 if most right or 0.5 if most left normal X1 -> idea : make it more exponential   lets say if(<0.3 || >0.7) than double by 2
//                        // also can use this equation y = 2746680 + (0.7730038 - 2746680)/(1 + (x/11.82128)^5.424924) -> not need to add 0.5
//                        float newSpeed = (float) (0.5 + (dX / frameMaxWidth));
//                        //lock at idea
//                        float newSpeed2 = getNewSpeed(dX);
//                        setNewSpeedToSources(newSpeed2);
//                        System.out.println("New Speed : " + newSpeed);
//                        System.out.println("New Speed2 : " + newSpeed2);
//
//
//                    } else {
//                        //check if needed to make a make quit zone around the mid source.
//                        //pro : better sound of the mid source.
//                        //con : in the start we wont all 3 sentence to be heard simultaneity
//                        //idea: in the zone silence v1+v3 in half (0.25f)
//                        float newVolume1 = getFirstSourceNewVolume(dX, dY);
//                        float newVolume2 = getSecondSourceNewVolume(dX, dY);
//                        float newVolume3 = getThirdSourceNewVolume(dX, dY);
//                        setNewVolumeToSources(newVolume1, newVolume2, newVolume3);
//                    }
//
//
//                }
//
//            }
//
//            private void setNewSpeedToSources(float newSpeed) {
//                SoundUtil.setNewSpeedToSources(newSpeed);
//            }
//
//            private float getNewSpeed(float dX) {
//                float newSpeed = (float) (0.5 + (dX / frameMaxWidth));
//                if (newSpeed > 1.4)
//                    newSpeed = newSpeed * 4;
//                else if (newSpeed < 0.3)
//                    newSpeed = newSpeed / 4;
//                return newSpeed;
//            }
//
//            /**
//             * check if need to sense mouse moving and update source volume;
//             * @return boolean
//             */
//            private boolean MouseMoveDetection() {
//                return mMouseMoveDetection;
//            }
//
//            private void setNewVolumeToSources(float newVolume1, float newVolume2, float newVolume3) {
//                SoundUtil.updateSourcesVolume(newVolume1, newVolume2, newVolume3);
//            }
//
//            private float getThirdSourceNewVolume(float dX, float dY) {
//                float ans = dX / frameMaxWidth;
////                if (dX<=(frameMaxWidth*(5/8)))
////                    ans=0;
//                System.out.println("New V3 : " + ans);
//                return ans;
//            }
//
//            private float getSecondSourceNewVolume(float dX, float dY) {
//                float ans = 0;
//                if (dX >= (frameMaxWidth / 2))
//                    ans = (1 - (dX / frameMaxWidth)) * 2;
//                else
//                    ans = (dX / frameMaxWidth) * 2;
//                System.out.println("New V2 : " + ans);
//                return ans;
//            }
//
//            private float getFirstSourceNewVolume(float dX, float dY) {
//                float ans = 1 - (dX / frameMaxWidth);
//                System.out.println("New V1 : " + ans);
//                return ans;
//            }
//        });
//
//    }
//
//    public void keyPressed(KeyEvent e) {
//        if (!keyIsPressed) {
//            switch (e.getKeyCode()) {
//                case KeyEvent.VK_LEFT:
//                    System.out.println("You pressed left");
//                    SoundUtil.rotateLeft();
//                    SoundUtil.updateSources();
//                    SoundUtil.playTags();
//                    break;
//                case KeyEvent.VK_RIGHT:
//                    System.out.println("You pressed right");
//                    SoundUtil.rotateRight();
//                    SoundUtil.updateSources();
//                    SoundUtil.playTags();
//                    break;
//                case KeyEvent.VK_N:
//                    System.out.println("You Pressed N");
//                    SoundUtil.navigateTraversal();
//                    break;
//                case KeyEvent.VK_1:
//                    /*
//                    if we in main nav mode that key that pressed will enter a require nav list create new sources and buffers from the require list
//                    if we not in main nav mode we will get the new url and load it
//                     */
//                    createAndPlaySourcesFromKeyPress(1);
//                    break;
//
//                case KeyEvent.VK_2:
//                    createAndPlaySourcesFromKeyPress(2);
//                    break;
//                case KeyEvent.VK_P:
//                    //play sound
//                    //need to rethink what will happen after we press p
//                    /*
//                        lets start with reading and chose fromm all possible array in the Map in vox case 2 thing -> nav bar and headline
//                        then we select 1 or 2 , 1->read the nav bar 2->read the headline list
//                     */
//                    SoundUtil.createMainNavSource();
//                    SoundUtil.playTags();
//                    mainNavMode = true;
////                    mMouseMoveDetection = true;
////                    SoundUtil.createSources(); // ->will fall because curnav = -1 ;
////                    SoundUtil.playTags();
//                    break;
//                case KeyEvent.VK_ENTER:
//                    SoundUtil.createSources(0);
//                    break;
//                case KeyEvent.VK_B:
//                    //after press B i wont to come back from an article to the headline reading from the sub navigation to main navigation.
//                    //idea : long press on B will automatically go back to Main Navigation
//                    float time = 0;
//                    if (subNavMode) {
//                        subNavMode = false;
//                        mainNavMode = true;
//                        SoundUtil.createMainNavSource();
//                        SoundUtil.playTags();
//                    } else if (articleMode) {
//                        articleMode = false;
//                        SoundUtil.rotateRight();
//                        SoundUtil.rotateLeft();
//                        SoundUtil.playTags();
//                    }
//
//            }
//            keyIsPressed = true;
//        }
//    }
//
//    /**
//     * create the new sound sources load the buffer of the selected item
//     *
//     * @param keyPress
//     */
//    private void createAndPlaySourcesFromKeyPress(int keyPress) {
//        if (mainNavMode) {
//            mainNavMode = false;
//            SoundUtil.createSources(keyPress - 1);
//            mMouseMoveDetection = true;
//            SoundUtil.playTags();
//            mainNavMode = false;
//            currentMap = keyPress - 1;
//        } else { // in vox case i am in headline list or in navigation list and press to enter to an article or sub navigation;
//            if (SoundUtil.getMkey().equals("HeadLine")) {
//                articleMode = true;
//                App.URL_LINK = SoundUtil.getUrlFromSelectedTag(keyPress);
//                WebUtil.connectToWebsite(App.URL_LINK);
//                SoundUtil.createAndUpdateOneSourceArticle();
//            } else if (SoundUtil.getMkey().equals("Main Navigation")) {
//                subNavMode = true;
//                App.URL_LINK = SoundUtil.getUrlFromSelectedTag(keyPress);
//                WebUtil.connectToWebsite(App.URL_LINK);
//                SoundUtil.createAndUpdateSubNavSources();
//            }
//            SoundUtil.playTags();
//            mMouseMoveDetection = true;
//        }
//    }
//
//
//    public void keyReleased(KeyEvent e) {
//        System.out.println("Released a key " + e.getKeyChar());
//        keyIsPressed = false;
//    }
}
