package Utils.Scrapper;

import App.App;
import Utils.Tag;
import Utils.Type;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Football365Scrapper extends AScrapper {
    private final String URL = "http://www.football365.com/";

    public Football365Scrapper() {
        super();
        this.openWebsite(URL);
    }


    @Override
    public List<Tag> getArticles() {
        List<Tag> tags = new ArrayList<>();
        String mainHeadLineText = "";
        String mainContentText = "";
        //idea : do i wont to read the head line and sub head line ? i extract and make a simple function that make example but i dont use it
        Elements mainHeadline = doc.getElementsByTag("h1");
        mainHeadLineText = mainHeadline.get(0).text();

        Elements contentElements = doc.getElementsByClass("article__body");
        for (Element element1 : contentElements.get(0).children()
                ) {
            if (element1.tagName().equals("p"))
                mainContentText += " " + element1.text();
        }
        //see idea
        Tag tag1 = getArticleTag(mainHeadLineText, "sub headline text", mainContentText);
        Tag tag2 = new Tag("article", mainContentText, Type.ARTICLE);
        tag2.setUrl(App.URL_LINK);
        tags.add(tag2);
        return tags;
    }

    private static Tag getArticleTag(String mainHeadLineText, String subHeadLineText, String mainContentText) {
        String content = " article main headline : " + mainHeadLineText + "article sub headline : " + subHeadLineText + "article main content : " + mainContentText;
        Tag tag = new Tag("article", content, Type.ARTICLE);
        tag.setUrl(App.URL_LINK);
        return tag;
    }

    @Override
    public List<Tag> getMenus() {
        List<Tag> navElementsTags = new ArrayList<>();
        Elements links = doc.getElementsByClass("mega-menu-link");
        for (Element link : links) {
            String tagName = link.text();
            String tagUrl = link.attr("href");
            if (!(tagUrl.indexOf("http") != -1 ? true : false)) {
                tagUrl = URL;
            }
            Tag tag = new Tag(tagName, tagName, Type.LINK);
            tag.setUrl(tagUrl);
            navElementsTags.add(tag);
        }
        return navElementsTags;
    }

    private List<Tag> getHeadlines() {
        List<Tag> headlines = new ArrayList<>();
        Elements a = doc.getElementsByTag("a");
        for (Element header : a
                ) {
            Elements figcaptionHero = header.getElementsByClass("hero__figcaption");
            Elements figcaptionHeroSmall = header.getElementsByClass("hero-sml__figcaption");
            Elements figcaptionArticleList = header.getElementsByClass("articleList__figcaption");
            if (figcaptionArticleList.size() == 0 && figcaptionHero.size() == 0 && figcaptionHeroSmall.size() == 0) {
                continue;
            }
            Elements figcaption = null;
            if (figcaptionHero.size() > 0) {
                figcaption = figcaptionHero;
            } else if (figcaptionHeroSmall.size() > 0) {
                figcaption = figcaptionHeroSmall;
            } else {
                figcaption = figcaptionArticleList;
            }
            //get the text from h2 or h3
            Elements h = null;
            Elements h2 = figcaption.get(0).getElementsByTag("h2");
            Elements h3 = figcaption.get(0).getElementsByTag("h3");
            if (h2.size() > 0) {
                h = h2;
            } else {
                h = h3;
            }
            String tagName = h.get(0).text();
            String tagUrl = header.attr("href");
            Tag tag = new Tag(tagName, tagName, Type.HEADLINE);
            tag.setUrl(tagUrl);
            try {
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
        Map<String, List<Tag>> Football365Dict = new HashMap<>();
        //1. get all the menu options
        Football365Dict.put("Menu", getMenus());
        //2. get all the headlines
        Football365Dict.put("Headlines", getHeadlines());
        //3. get all the articles
        //voxDict.put("Articles", getArticles());
        return Football365Dict;
    }


}
