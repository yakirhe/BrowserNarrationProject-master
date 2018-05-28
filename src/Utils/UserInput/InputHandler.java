package Utils.UserInput;

import App.App;
import App.Engine;
import GUI.MainFrame;
import Utils.*;
import Utils.Scrapper.AScrapper;
import com.sun.org.apache.bcel.internal.generic.SWITCH;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.ArrayList;

import static Utils.SoundUtil.mCurrentTag;
import static Utils.SoundUtil.setNewSpeedToSources;
import static com.google.common.collect.ComparisonChain.start;

/**
 * Created by shaha on 28/03/
 * This class will be in charge of all the key events in our project
 */
public class InputHandler {
    public static class KeyHandlerSelection implements KeyListener {
        private final MainFrame app;
        private int mode;
        private boolean play = false;

        /**
         * @param app
         * @param mode - describe for which situation we need the key handler
         *             1 - for the app main menu
         *             2 - for the scrapping process
         */
        public KeyHandlerSelection(MainFrame app, int mode) {
            this.app = app;
            this.mode = mode;
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (mode == 1) {
                try {
                    menuHandler(e);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } else {
                try {

                    scrapperHandler(e);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }

        }

        /**
         * Handle the scrapper logic.
         *
         * @param e
         */
        private void scrapperHandler(KeyEvent e) throws InterruptedException {
            //handle the user
            int c = e.getKeyCode();

            if (c == KeyEvent.VK_P && !play) {
                playMenu();
            }

            //We only interact with the elements after the user pressed play
            if (!play) {
                return;
            }

            System.out.println("The user pressed " + c); //log for our use
            switch (c) {
                case KeyEvent.VK_1:
                    updateSourcesByKeyPress(1);
                    break;
                case KeyEvent.VK_2:
                    updateSourcesByKeyPress(2);
                    break;
                case KeyEvent.VK_3:
                    updateSourcesByKeyPress(3);
                    break;
                case KeyEvent.VK_RIGHT:
                    SoundUtil.rotateRight();
                    SoundUtil.updateSources();
                    SoundUtil.playTags();
                    break;
                case KeyEvent.VK_LEFT:
                    SoundUtil.rotateLeft();
                    SoundUtil.updateSources();
                    SoundUtil.playTags();
                    break;
                case KeyEvent.VK_BACK_SPACE:
                case KeyEvent.VK_B:
                    goBack();
                    break;
            }
        }

        private void goBack() throws InterruptedException {
            //stop all the sources
            SoundUtil.StopAllPrevSources();
            //check the current mode
            switch(SoundUtil.getMode()){
                case MAIN_NAVIGATION:
                    SoundUtil.setMode(null);
                    play = false;
                    app.removeListeners();
                    App.app.start(false);
                    break;
                case SUB_NAVIGATION:
                    SoundUtil.setMode(NarrationMode.MAIN_NAVIGATION);
                    app.removeMouseListener();
                    //clear the sources
                    SoundUtil.clearSourceList();
                    playMenu();
                    break;
                case ARTICLE:
                    SoundUtil.setMode(NarrationMode.SUB_NAVIGATION);
                    SoundUtil.setmTagsList(SoundUtil.get_tags().get("Headlines"));
                    SoundUtil.initBuffers(NarrationMode.SUB_NAVIGATION);
                    SoundUtil.playTags();
                    break;
            }
        }


        private void playMenu() throws InterruptedException {
            play = true;

            //1. init the layout
            if (SoundUtil.getMode() != NarrationMode.MAIN_NAVIGATION) {
                SoundUtil.createSources();
            }

            //2. init the initial buffers
            SoundUtil.initBuffers(NarrationMode.MAIN_NAVIGATION);

            //3. play the tags
            SoundUtil.playTags();

            //add mouse listener
            app.setMouseListener();
        }

        private void updateSourcesByKeyPress(int keyPress) throws InterruptedException {
            switch (SoundUtil.getMode()) {
                case MAIN_NAVIGATION:
                    //update sources and buffers
                    ArrayList<String> keyList = new ArrayList<>(SoundUtil.get_tags().keySet());
                    SoundUtil.setmTagsList(SoundUtil.get_tags().get(keyList.get(keyPress - 1)));
                    SoundUtil.initBuffers(NarrationMode.SUB_NAVIGATION);
                    SoundUtil.playTags();
                    break;
                case SUB_NAVIGATION:
                    Type type = SoundUtil.getmTagsList().get(mCurrentTag - 1 + keyPress).getType();
                    App.URL_LINK = SoundUtil.getUrlFromSelectedTag(keyPress);
                    SoundUtil.StopAllPrevSources();
                    switch (type) {
                        case LINK:
                            Engine.getaScrapper().openWebsite(App.URL_LINK);
                            ArrayList<Tag> headlines = (ArrayList<Tag>) (Engine.getaScrapper().getItems().get("Headlines"));
                            SoundUtil.setmTagsList(headlines);
                            SoundUtil.initBuffers(NarrationMode.SUB_NAVIGATION);
                            SoundUtil.playTags();
                            break;
                        case HEADLINE:
                            SoundUtil.playChangeToArticle();
                            Engine.getaScrapper().openWebsite(App.URL_LINK);
                            ArrayList<Tag> article = (ArrayList<Tag>) Engine.getaScrapper().getArticles();
                            SoundUtil.setmTagsList(article);
                            SoundUtil.StopAllPrevSources();
                            SoundUtil.initBuffers(NarrationMode.ARTICLE);
                            SoundUtil.stopChangeToArticle();
                            SoundUtil.playTags();
                            break;
                    }
                    break;
                case ARTICLE:
                    break;
                default:
                    return;
            }
        }

        private void menuHandler(KeyEvent e) throws IOException {
            //handle the user
            char c = e.getKeyChar();
            System.out.println("The user pressed " + c); //log for our use
            if (c == '1' || c == '2' || c == '3') {
                //remove the key listener from the app
                app.removeSelectionListener();
                SoundUtil.stopInstruction();
                app.remove(app.getjLabel());
                app.set_instruction_screen();

                //set the selected choice to use the appropriate scrapper
                app.setScrapper(c);
            } else if (c == '4') {
                SoundUtil.stopInstruction();
                SoundUtil.playInstructions();
            } else {
                System.out.println("The user have to choose again"); //log for our use
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }

    public static class MouseHandler implements MouseMotionListener {
        private final MainFrame app;
        private float width = 0;
        private float height = 0;


        public MouseHandler(MainFrame app) {
            this.app = app;
            Rectangle rectangle = app.getBounds();
            width = rectangle.width;
            height = rectangle.height;
        }

        @Override
        public void mouseDragged(MouseEvent e) {

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            //use this site to help : https://mycurvefit.com/
            float dX = e.getX();
            float dY = e.getY();
            System.out.println("X : " + dX);
            System.out.println("Y : " + dY);
            if (SoundUtil.getMode() == NarrationMode.ARTICLE) {
                //will make the read X1.5 if most right or 0.5 if most left normal X1 -> idea : make it more exponential   lets say if(<0.3 || >0.7) than double by 2
                // also can use this equation y = 2746680 + (0.7730038 - 2746680)/(1 + (x/11.82128)^5.424924) -> not need to add 0.5
                float newSpeed = (float) (0.5 + (dX / width));
                //lock at idea
                float newSpeed2 = getNewSpeed(dX);
                setNewSpeedToSources(newSpeed2);
                System.out.println("New Speed : " + newSpeed);
                System.out.println("New Speed2 : " + newSpeed2);
            } else {
                float newVolume1 = getFirstSourceNewVolume(dX, dY);
                float newVolume2 = getSecondSourceNewVolume(dX, dY);
                float newVolume3 = getThirdSourceNewVolume(dX, dY);
                setNewVolumeToSources(newVolume1, newVolume2, newVolume3);
            }


        }

        private void setNewVolumeToSources(float newVolume1, float newVolume2, float newVolume3) {
            SoundUtil.updateSourcesVolume(newVolume1, newVolume2, newVolume3);
        }

        private float getThirdSourceNewVolume(float dX, float dY) {
//            //original
//            float ans = dX / width;
//            if (dX <= (width * (5 / 8)))
//                ans = 0;
//            System.out.println("New V3 : " + ans);
//            return ans;
            //test
            float ans = 0;
            ans = dX / width;
            if ((dY / height) >= 0.5) { // firstHalf
            } else {
                ans = (float) (ans-0.5);
            }
            System.out.println("New V3 : " + ans);
            return ans;
        }

        private float getSecondSourceNewVolume(float dX, float dY) {
//            //original
//            float ans = 0;
//            if (dX >= (width / 2))
//                ans = (1 - (dX / width)) * 2;
//            else
//                ans = (dX / width) * 2;
//            System.out.println("New V2 : " + ans);
//            return ans;
            //test
            float ans = 0;
            if ((dY / height) >= 0.5) {
                if (dX >= (width / 2))
                    ans = (1 - (dX / width)) * 2;
                else
                    ans = (dX / width) * 2;
                System.out.println("New V2 : " + ans);
                return (float) (ans - 0.2);
            } else {
                //get ansX
                float ansX = 0;
                if (dX >= (width / 2)) {
                    ansX = (1 - (dX / width)) * 2;
                } else {
                    ansX = (dX / width) * 2;
                }
                //get ansY
                float ansY = 1 - (dY / height);
                //compute ans
                if (dX > width / 3 && dX < width * (3 / 4)) {
                    ans = (float) (ansY - 0.5 * ansX);
                } else
                    ans = (float) (ansY * 0.5 + ansX);
                System.out.println("New V2 : " + ans);
                return ans;
            }
        }

        private float getFirstSourceNewVolume(float dX, float dY) {
//            //original
//            float ans =0;
//            float ansY = dY/height;
//            float ansX = 1 - (dX / width);
//            System.out.println("New V1 : " + ansX);
//            return ansX;
            //test
            float ans = 0;
            float ansX = 1 - (dX / width);
            if (dY >= height / 2) {
                ans = ansX;
            } else {
                ans = (float) (ansX - 0.5);
            }
            System.out.println("New V1 : " + ans);
            return ans;
        }


        private float getNewSpeed(float dX) {
            float newSpeed = (float) (0.5 + (dX / width));
            if (newSpeed > 1.4)
                newSpeed = newSpeed * 4;
            else if (newSpeed < 0.3)
                newSpeed = newSpeed / 4;
            return newSpeed;
        }

    }
}
