package Utils;

import org.apache.commons.collections.map.HashedMap;
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

    /**
     * Takes the webpage url and connect to it
     *
     * @param url
     */
    public static void connectToWebsite(String url) {
        try {
            doc = Jsoup.connect(url).get();

            System.setProperty("webdriver.chrome.driver", "./src/chromedriver.exe");

            WebDriver driver = new ChromeDriver();
            driver.get(url); //open the url in chrome
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

        //1. get all the nav tags
        Map<String, ArrayList<Tag>> navsDictionary = getNavMap();

        //2. get all the articles from the first site
        Map<String,ArrayList<Tag>> sectionsDictionary = getSections();

        //3. unite the navsDictionary and the sections dictionary
        
        return navsDictionary;
    }

    private static Map<String,ArrayList<Tag>> getSections() {
        Map<String,ArrayList<Tag>> sectionsDictionary = new HashMap<>();
        ArrayList<Tag> linksSection = new ArrayList<>();
        String sectionName = "";
        Elements section = doc.getElementsByTag("section");
        //remove the sections that have sections as child
        for(int i = 0; i < section.size();i++){
            if(section.select("section").get(i).select("section").size() > 1){
                section.remove(i);
            }else{
                
            }
       }
       //
        String x = "";
        return null;
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

    private static ArrayList<Tag> union(List<Tag> list1, List<Tag> list2) {
        ArrayList<Tag> union = (ArrayList<Tag>) list1;

        for(Tag tag:list2){
            if(!union.contains(tag)){
                union.add(tag);
            }
        }

        return union;
    }
}
