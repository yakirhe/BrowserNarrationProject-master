package Utils.Scrapper;

import Utils.Tag;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shaha on 28/03/2018.
 */
public class VoxScrapper extends AScrapper {
    private final String URL = "https://www.vox.com/";

    public VoxScrapper() {
        super();
        this.openWebsite(URL);
    }

    @Override
    public List<Tag> getArticles() {
        return null;
    }

    @Override
    public List<Tag> getMenus() {
        Elements navs = doc.getElementsByTag("nav");
        List<Tag> navList = new ArrayList<>(); //The list holds all of the menus with their corresponded title

        for (Element nav : navs) {
            Elements aTags = nav.getElementsByTag("li");
            navList = extractNavElements(aTags);
        }
        return navList;
    }

    /**
     * Get all the headlines from the current page
     *
     * @return
     */
    public List<Tag> getHeadlines() {
        ArrayList<Tag> headlines = new ArrayList<>();

        //all the headlines are in H2 tags
        Elements h2 = doc.getElementsByTag("h2");
        for (Element header : h2) {
            Elements links = header.getElementsByTag("a");
            String tagName = links.text();
//            String tagUrl = a.attr("href");
//            Tag tag = new Tag(tagName, tagName, Type.HEADLINE);
//            tag.setUrl(tagUrl);
//            try {
//                if (tag.getName() != null && !tag.getName().equals("") && tag.getContent() != null && !tag.getContent().equals(""))
//                    headlines.add(tag);
//            } catch (Error error) {
//                System.out.println(error.getMessage());
//            }
        }
//        headlineMap.put("HeadLine", headlines);
        return headlines;
    }

    @Override
    public Map<String, List<Tag>> getItems() {
        //1. get all the menu options
        getMenus();
        //2. get all the headlines
        return null;
    }
}
