/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facebooklinkfinder;


//imports in the program

import org.jsoup.Jsoup;
import java.io.IOException;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

/**
 *
 * @author ajith
 */
public class Facebooklinkfinder {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        String url = "http://www.logica.in";
        
        Document doc = Jsoup.connect(url).get();
        
        Elements links = doc.select("a[href]");
        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");
        
//        print("\nMedia: (%d)", media.size());
//        for (Element src : media) {
//            if (src.tagName().equals("img"))
//                print(" * %s: <%s> %sx%s (%s)",
//                        src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),
//                        trim(src.attr("alt"), 20));
//            else
//                print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
//        }
//
//        print("\nImports: (%d)", imports.size());
//        for (Element link : imports) {
//            print(" * %s <%s> (%s)", link.tagName(),link.attr("abs:href"), link.attr("rel"));
//        }

        print("\nLinks: (%d)", links.size());
        for (Element link : links) {
           
            print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
        }
    }

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }
}
        
    