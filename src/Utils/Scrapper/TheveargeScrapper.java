package Utils.Scrapper;

import App.App;
import Utils.Tag;
import Utils.Type;
import org.apache.commons.lang.ObjectUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.xsoup.XElements;
import us.codecraft.xsoup.Xsoup;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Utils.WebUtil.extractNavElements;
import static Utils.WebUtil.getArticleTag;
import static Utils.WebUtil.union;

public class TheveargeScrapper extends AScrapper {
    private final String url = "https://www.theverge.com/";

    public TheveargeScrapper() throws IOException {
        super();
        this.openWebsite(url);
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
            if (links.size() == 0) {
                continue;
            }
            XElements xpath = Xsoup.compile("").evaluate(x);
            String tagName = links.text();
            String tagUrl = links.get(0).attr("href");
            Tag tag = new Tag(tagName, tagName, Type.HEADLINE);
            tag.setUrl(tagUrl);
            tag.setxPath(xpath);
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
    public List<Tag> getMenus() {
        Elements navs = doc.getElementsByTag("nav");
        List<Tag> navList = new ArrayList<>();

        for (Element nav : navs) {
            Elements aTags = nav.getElementsByTag("li");
            navList = extractNavElements(aTags);
        }
        return navList;

    }

    @Override
    public Map<String, List<Tag>> getItems() {
        Map<String, List<Tag>> TheVeargeDict = new HashMap<>();
        TheVeargeDict.put("Menu", getMenus());
        TheVeargeDict.put("Headlines", getHeadlines());
        return TheVeargeDict;
    }


    @Override
    public List<Tag> getArticles() {
        HashMap<String, ArrayList<Tag>> map = new HashMap<>();
        ArrayList<Tag> tags = new ArrayList<>();
        String mainHeadLineText = "";
        String subHeadLineText = "";
        String mainContentText = "";
        //idea : do i wont to read the head line and sub head line ? i extract and make a simple function that make example but i dont use it
        Elements mainHeadline = doc.getElementsByTag("h1");
        for (Element element : mainHeadline
                ) {
            mainHeadLineText = element.text();
        }
        Element subHeadline = doc.select("h2").first();
        subHeadLineText = subHeadline.text();
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
        Tag tag1 = getArticleTag(mainHeadLineText, subHeadLineText, mainContentText);
//        Tag tag2 = new Tag("article", mainContentText, Type.ARTICLE);
        tag1.setUrl(App.URL_LINK);
        tags.add(tag1);
        map.put("article", tags);
        return tags;
    }


}
