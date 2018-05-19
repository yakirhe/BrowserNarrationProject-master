package Utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Created by shaha on 28/03/2018.
 * This class is meant to show where we are
 * in the webpage using selenium
 */
public class WebViewer {
    private WebDriver driver = null;

    public WebViewer(){
        //set the selenium
        System.setProperty("webdriver.chrome.driver", "./src/chromedriver.exe");
    }

    public void openWebsite(String url) {
        if(driver == null){
            driver = new ChromeDriver();
        }
        driver.get(url); //open the url in chrome
        //log the action
        System.out.println("Connecting to " + url);
    }

    public void updateUrl(String url){

    }
}
