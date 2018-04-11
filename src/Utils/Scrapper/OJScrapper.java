package Utils.Scrapper;

import Utils.Tag;

import java.util.List;
import java.util.Map;

/**
 * Created by shaha on 28/03/2018.
 */
public class OJScrapper extends AScrapper {
    private final String URL = "http://edition.cnn.com/US/OJ/";

    public OJScrapper(){
        super();
        this.openWebsite(URL);
    }

    @Override
    public List<Tag> getArticles() {
        return null;
    }

    @Override
    public List<Tag> getMenus() {
        return null;
    }

    @Override
    public Map<String, List<Tag>> getItems() {
        return null;
    }

}
