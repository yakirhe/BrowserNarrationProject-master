package Utils.Scrapper;

import App.App;
import Utils.Tag;
import Utils.Type;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.lwjgl.system.CallbackI;

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
        ArrayList<Tag> tags = new ArrayList<>();
        String mainHeadLineText = "";
        String subHeadLineText = "";
        String mainContentText = "";
        //idea : do i wont to read the head line and sub head line ? i extract and make a simple function that make example but i dont use it
        Elements mainHeadline = doc.getElementsByTag("h1");
        for (Element element : mainHeadline) {
            mainHeadLineText = element.text();
            Element subHeadline = doc.select("h2").first();
            subHeadLineText = subHeadline.text();
        }

        Elements contentElements = doc.getElementsByClass("c-entry-content");
        for (Element element : contentElements
                ) {
            for (Element element1 : element.children()
                    ) {
                if (element1.tagName().equals("p") || element1.tagName().equals("blockquote"))
                    mainContentText += " " + element1.text();
            }
        }
        //see idea
        Tag tag2 = new Tag("article", mainContentText, Type.ARTICLE);
        tag2.setUrl(App.URL_LINK);
        tags.add(tag2);
        return tags;
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
            Element x = links.first();
            //Check if the header is not part of an headline
            if(links.size()==0){
                continue;
            }
            String tagName = links.text();
            String tagUrl = links.get(0).attr("href");
            Tag tag = new Tag(tagName, tagName, Type.HEADLINE);
            tag.setUrl(tagUrl);
            try {
                //Check if everything is valid
                if (tag.getName() != null && !tag.getName().equals("") && tag.getContent() != null && !tag.getContent().equals(""))
                    headlines.add(tag);
            } catch (Error error) {
                System.out.println(error.getMessage());
            }
        }
        return headlines;
    }

    @Override
    public Map<String, List<Tag>> getItems() {
        Map<String, List<Tag>> voxDict = new HashMap<>();
        //1. get all the menu options
        voxDict.put("Menu", getMenus());
        //2. get all the headlines
        voxDict.put("Headlines", getHeadlines());
        //3. get all the articles
        //voxDict.put("Articles", getArticles());
        return voxDict;
    }
}
