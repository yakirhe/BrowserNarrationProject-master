package Utils;

import App.App;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.util.*;

/**
 * This class help us to interact with a website.
 * it takes a url and extract the relevant tags
 */
public class WebUtil {
    private static Document doc = null; //holds the webpage url
    private static String url;

    /**
     * Takes the webpage url and connect to it
     *
     * @param url
     */
    public static void connectToWebsite(String url) {
        try {
            doc = Jsoup.connect(url).get();
            System.setProperty("webdriver.chrome.driver", "./src/chromedriver.exe");

            //   WebDriver driver = new ChromeDriver();
            //   driver.get(url); //open the url in chrome
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the tags from the webpage and returns it as an ArrayList
     *
     * @return
     */
    public static Map<String, ArrayList<Tag>> getItems() {
        //check if we are connected to the webpage
        if (doc == null) {
            return null;
        }
        Map<String, ArrayList<Tag>> items = null;

        switch (App.URL_LINK) {
            case "https://www.vox.com/":
                items = getVoxItems();

        }

        //1. get all the nav tags
        Map<String, ArrayList<Tag>> navsDictionary = getNavMap();

        //2. get all the articles from the first site
//        Map<String, ArrayList<Tag>> sectionsDictionary = getSections();

        //3. unite the navsDictionary and the sections dictionary

        return navsDictionary;
    }

    /**
     * parser of the vox news site
     *
     * @return dictionary that contains list of nav tags , article , headline
     */
    public static Map<String, ArrayList<Tag>> getVoxItems() {
        //UserInput : nav type  , value : list of nav tag
        Map<String, ArrayList<Tag>> navsMap = getVoxNavMap("Main Navigation");
        //UserInput "headline" , value : list of article headline
        //idea : make sub groups like top stores , newest stores ... check the list that i get and check in som category if its the same layout and i can detect it .
        Map<String, ArrayList<Tag>> headlineMap = getVoxHeadline();
        //UserInput "article" , value : list of article tag
        Map<String, ArrayList<Tag>> articleMap = getVoxArticle();
        //return union
        Map<String, ArrayList<Tag>> items = unionMap(navsMap, headlineMap, articleMap);
        return items;
    }

    /**
     * return the union off all the items in the page including the navs , headline , articles
     *
     * @param navsMap
     * @param headlineMap
     * @param articleMap
     * @return union of all the maps
     */
    private static Map<String, ArrayList<Tag>> unionMap(Map<String, ArrayList<Tag>> navsMap, Map<String, ArrayList<Tag>> headlineMap, Map<String, ArrayList<Tag>> articleMap) {
        Map<String, ArrayList<Tag>> union = new HashMap<>(navsMap);
        union.putAll(headlineMap);
        //    union.putAll(articleMap);
        return union;
    }

    /**
     * extract all the headline of the articles on the page;
     *
     * @return Map < "HeadLine" , list of headlines tags >
     */
    private static Map<String, ArrayList<Tag>> getVoxHeadline() {
        Map<String, ArrayList<Tag>> headlineMap = new HashMap<>();
        ArrayList<Tag> headlines = new ArrayList<>();
        Elements h2 = doc.getElementsByTag("h2");
        for (Element header : h2
                ) {
            Elements a = header.getElementsByTag("a");
            String tagName = a.text();
            String tagUrl = a.attr("href");
            Tag tag = new Tag(tagName, tagName, Type.HEADLINE);
            tag.setUrl(tagUrl);
            try {
                if (tag.getName() != null && !tag.getName().equals("") && tag.getContent() != null && !tag.getContent().equals(""))
                    headlines.add(tag);
            } catch (Error error) {
                System.out.println(error.getMessage());
            }
        }
        headlineMap.put("HeadLine", headlines);
        return headlineMap;
    }

    /**
     * extract all the main nav tags fromm the vox news site
     *@param navigationType main or sub navigation if main then "Main Navigation" if sub then "Sub Navigation";
     * @return map <key = "Main Navigation" , value = list of tags>
     */
    private static Map<String, ArrayList<Tag>> getVoxNavMap(String navigationType) {
        Elements navs = doc.getElementsByTag("nav");
        Map<String, ArrayList<Tag>> navMap = new HashMap<>();

        for (Element nav : navs
                ) {
            ArrayList<Tag> tags;
            String navName = navigationType;
            //lets extract the tags
            Elements aTags = nav.getElementsByTag("li");
            tags = extractNavElements(aTags);
            if (tags.size() != 0) {
                if (!navMap.containsKey(navName)) {
                    navMap.put(navName, tags);
                } else {
                    //combine the lists
                    ArrayList<Tag> union = union(navMap.get(navName), tags);
                    navMap.put(navName, union);
                }
            }

        }

        return navMap;

    }

    /**
     * get nav element and extract list of tags
     *
     * @param navs
     * @return list of the menu tags
     */
    private static ArrayList<Tag> extractNavElements(Elements navs) {
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

    private static Map<String, ArrayList<Tag>> getNavMap() {
        Map<String, ArrayList<Tag>> navsDictionary = new HashMap<>();

        Elements navs = doc.getElementsByTag("nav");

        int i = 1;
        for (Element nav : navs) {
            ArrayList<Tag> tags = new ArrayList<>();
            String navName = nav.attr("aria-label");

            Elements navLinks = nav.getElementsByTag("a");
            for (Element link : navLinks) {
                String linkText = link.text();

                //Trim the text
                linkText = linkText.replaceAll("[^A-Za-z0-9 ]", "");
                linkText = linkText.trim();

                if (!linkText.equals("")) {
                    //get the href
                    String href = link.attr("href");

                    Tag tag = new Tag(linkText, href, Type.LINK);
                    tags.add(tag);
                }
            }

            if (tags.size() != 0) {
                if (!navsDictionary.containsKey(navName)) {
                    navsDictionary.put(navName, tags);
                } else {
                    //combine the lists
                    ArrayList<Tag> union = union(navsDictionary.get(navName), tags);
                    navsDictionary.put(navName, union);
                }
            }
        }

        return navsDictionary;
    }

    public static ArrayList<Tag> union(List<Tag> list1, List<Tag> list2) {
        ArrayList<Tag> union = (ArrayList<Tag>) list1;

        for (Tag tag : list2) {
            if (!union.contains(tag)) {
                union.add(tag);
            }
        }

        return union;
    }

    /**
     * update the sub articles Map(key->map , value -> list contain one article tag)
     */
    public static HashMap<String, ArrayList<Tag>> getVoxArticle() {
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
        Tag tag2 = new Tag("article", mainContentText, Type.ARTICLE);
        tag2.setUrl(App.URL_LINK);
        tags.add(tag2);
        map.put("article", tags);
        return map;
    }

    /**
     * make a tag witch attention to the headline and the sub headline
     *
     * @param mainHeadLineText
     * @param subHeadLineText
     * @param mainContentText
     * @return
     */
    public static Tag getArticleTag(String mainHeadLineText, String subHeadLineText, String mainContentText) {
        String content = " article main headline : " + mainHeadLineText + "article sub headline : " + subHeadLineText + "article main content : " + mainContentText;
        Tag tag = new Tag("article", content, Type.ARTICLE);
        tag.setUrl(App.URL_LINK);
        return tag;
    }

    public static HashMap<String,ArrayList<Tag>> getVoxSubNav() {
        return (HashMap<String, ArrayList<Tag>>) getVoxHeadline();

    }
}
