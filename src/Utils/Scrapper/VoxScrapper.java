package Utils.Scrapper;

import Utils.Tag;

import java.util.List;

/**
 * Created by shaha on 28/03/2018.
 */
public class VoxScrapper implements IScrapper {
    private final String URL = "https://www.vox.com/";
    public VoxScrapper(){

    }

    @Override
    public List<Tag> getArticles() {
        return null;
    }

    @Override
    public List<Tag> getMenus() {
        return null;
    }
}
