package Utils.Scrapper;

import Utils.Tag;
import Utils.WebViewer;

import java.util.List;

/**
 * Created by shaha on 28/03/2018.
 * The abstract class for our scrappers
 * Here we do all the common logic and initialization to our scrappers
 */
public abstract class AScrapper implements IScrapper {
    public AScrapper(){
        //create a selenium instance
        WebViewer webViewer = new WebViewer();
    }

    @Override
    public abstract List<Tag> getArticles();

    @Override
    public abstract List<Tag> getMenus();

}
