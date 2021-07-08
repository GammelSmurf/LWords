package org.example.LWords.Scrapers;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.net.MalformedURLException;

public class ReversoScraper {
    public String getTranslations(String phrase){
        try (final WebClient webClient = new WebClient()) {
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(false);
            final HtmlPage page = webClient.getPage("https://context.reverso.net/%D0%BF%D0%B5%D1%80%D0%B5%D0%B2%D0%BE%D0%B4/%D0%B0%D0%BD%D0%B3%D0%BB%D0%B8%D0%B9%D1%81%D0%BA%D0%B8%D0%B9-%D1%80%D1%83%D1%81%D1%81%D0%BA%D0%B8%D0%B9/" + phrase);
            final HtmlDivision div = page.getHtmlElementById("translations-content");
            Iterable<DomElement> anchors = div.getChildElements();
            StringBuilder result = new StringBuilder();
            int iterator = 0;
            for (DomElement el:anchors
            ) {
                iterator++;
                if(iterator < 13 && el.getAttribute("title").length() > 75)
                    result.append(el.getAttribute("title").substring(75).split("<")[0]).append(";");
            }
            return result.toString();
        }
        catch (Exception ex){
            return null;
        }
    }
}
