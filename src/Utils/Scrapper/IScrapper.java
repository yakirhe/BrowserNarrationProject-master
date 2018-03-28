package Utils.Scrapper;

import Utils.Tag;

import java.util.List;

/**
 * Created by shaha on 28/03/2018.
 */
public interface IScrapper {
    //get all the articles from the website
    List<Tag> getArticles();
    //get all the menus from the website
    List<Tag> getMenus();
}
