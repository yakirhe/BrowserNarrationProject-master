package Utils.Scrapper;

import App.App;
import Utils.Tag;
import Utils.Type;
import Utils.WebViewer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.lwjgl.system.CallbackI;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by shaha on 28/03/2018.
 * The abstract class for our scrappers
 * Here we do all the common logic and initialization to our scrappers
 */
public abstract class AScrapper implements IScrapper {
    private static WebViewer webViewer = null;
    protected Element doc;
    protected static boolean isOpen = false;
    protected static boolean isLocal = false;

    public AScrapper() {
        //create a selenium instance
        if (!isOpen) {
            isOpen = true;
            webViewer = new WebViewer();
        }
    }

    public void openWebsite(String url,boolean isArticle) {
        try {

            if (!isLocal) {
                webViewer.openWebsite(url);
                doc = Jsoup.connect(url).get();
            }
            else {
                if(isArticle){
                    Path currentRelativePath = Paths.get("");
                    String local = currentRelativePath.toAbsolutePath().toString();
                    url = local + "\\voxOffline\\www.vox.com\\" + url;
                }
                webViewer.openWebsite(url);
                File input = new File(url);
                doc = Jsoup.parse(input, "UTF-8", "");
            }
            //load doc
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public abstract List<Tag> getArticles();

    @Override
    public abstract List<Tag> getMenus();

    public abstract Map<String, List<Tag>> getItems();

    /**
     * an helper method
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
