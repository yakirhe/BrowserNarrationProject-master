package Utils.Scrapper;

import App.App;
import Utils.Tag;
import Utils.Type;
import Utils.WebViewer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by shaha on 28/03/2018.
 * The abstract class for our scrappers
 * Here we do all the common logic and initialization to our scrappers
 */
public abstract class AScrapper implements IScrapper {
    private final WebViewer webViewer;
    protected Element doc;

    public AScrapper(){
        //create a selenium instance
        webViewer = new WebViewer();
    }

    public void openWebsite(String url){
        webViewer.openWebsite(url);
        try {
            //load doc
            doc = Jsoup.connect(url).get();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public abstract List<Tag> getArticles();

    @Override
    public abstract List<Tag> getMenus();

    public abstract Map<String,List<Tag>> getItems();

    /** an helper method
     * get nav element and extract list of tags
     *
     * @param navs
     * @return list of the menu tags
     */
    protected List<Tag> extractNavElements(Elements navs) {
        ArrayList<Tag> tags = new ArrayList<>();
        for (Element link : navs) {
            String linkText = link.text();

            //Trim the text
            linkText = linkText.replaceAll("[^A-Za-z0-9 ]", "");
            linkText = linkText.trim();

            if (!linkText.equals("")) {
                //get the href
                String href = link.getElementsByTag("a").attr("href");
                href = App.URL_LINK + href;

                Tag tag = new Tag(linkText, linkText, Type.LINK);
                tag.setUrl(href);
                tags.add(tag);
            }
        }
        return tags;
    }
}
