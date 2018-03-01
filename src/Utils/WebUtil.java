package Utils;

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

        Map<String, ArrayList<Tag>> navsDictionary = new HashMap<>();

        //get all the nav tags
        Elements navs = doc.getElementsByTag("nav");

        int i = 1;
        for (Element nav : navs) {
            ArrayList<Tag> tags = new ArrayList<>();
            String navName = nav.attr("aria-label");
            System.out.println("Nav " + navName);
            System.out.println("-----------------------------");

            Elements navLinks = nav.getElementsByTag("a");
            for (Element link : navLinks) {
                String linkText = link.text();

                //Trim the text
                linkText.replaceAll("[^A-Za-z0-9]", "");
                linkText.trim();

                //get the href
                link.attr("href");

                Tag tag = new Tag(linkText, linkText, Type.LINK);
                tags.add(tag);
            }

            navsDictionary.put(navName, tags);
            System.out.println();
        }

        return navsDictionary;
    }
}
