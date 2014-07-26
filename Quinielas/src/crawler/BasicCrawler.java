/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package crawler;

/**
 *
 * @author francisco
 */

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Clase encargada de comprobar si una url es válida y cuando lo es
 * se invoca a la clase encargada del parseado del código HTML
 * @author francisco
 */
 public class BasicCrawler extends WebCrawler {

        private final static Pattern FILTERS = Pattern.compile(".*(.\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4"
                        + "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
        private final static Pattern FILTER = Pattern.compile("^http://www.bdfutbol.com/es/+.*");

        /**
         * You should implement this function to specify whether the given url
         * should be crawled or not (based on your crawling logic).
         * @param url
         * @return 
         */
        @Override
        public boolean shouldVisit(WebURL url) {
                String href = url.getURL().toLowerCase();
                //System.out.println(href + " " + (!FILTERS.matcher(href).matches()&& FILTER.matcher(href).matches() ));
                return !FILTERS.matcher(href).matches()&& FILTER.matcher(href).matches();
                
        }

        /**
         * This function is called when a page is fetched and ready to be processed
         * by your program.
         * @param page
         */
        @Override
        public void visit (Page page) {
                int docid = page.getWebURL().getDocid();
                String url = page.getWebURL().getURL();
                String domain = page.getWebURL().getDomain();
                String path = page.getWebURL().getPath();
                
                page.getWebURL().setPath(path);
                
                String subDomain = page.getWebURL().getSubDomain();
                String parentUrl = page.getWebURL().getParentUrl();
                String anchor = page.getWebURL().getAnchor();
//
//                System.out.println("Docid: " + docid);
//                System.out.println("URL: " + url);
//                System.out.println("Domain: '" + domain + "'");
//                System.out.println("Sub-domain: '" + subDomain + "'");
//                System.out.println("Path: '" + path + "'");
//                System.out.println("Parent page: " + parentUrl);
//                System.out.println("Anchor text: " + anchor);
                
               
                if (page.getParseData() instanceof HtmlParseData) {
                        HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
                        String text = htmlParseData.getText();
                        String html = htmlParseData.getHtml();
                        List<WebURL> links = htmlParseData.getOutgoingUrls();
                        //Mi parseador
                        ParseHTML parser = new ParseHTML();
                        try {                   
                            parser.parseHTML(html, url);
                        
                        
//                        System.out.println("Text length: " + text.length());
//                        System.out.println("Html length: " + html.length());
//                        System.out.println("Number of outgoing links: " + links.size());
                        } catch (Exception ex) {
                            Logger.getLogger(BasicCrawler.class.getName()).log(Level.SEVERE, null, ex);

                        }
                    
                }               
        }
}

