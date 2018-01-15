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
import java.util.ArrayList;

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
    public static ArrayList<String> getItems() {
        //check if we are connected to the webpage
        if (doc == null) {
            return null;
        }

        //create an empty array list to store the tags
        ArrayList<String> items = new ArrayList<>();

        //get all the `a` tags
        Elements links = doc.getElementsByTag("a");
        for (Element link : links) {
            String linkText = link.text();

            //add only the links with text
            if (linkText.trim().length() != 0)
                items.add(linkText.trim());
        }
        return items;
    }
}
