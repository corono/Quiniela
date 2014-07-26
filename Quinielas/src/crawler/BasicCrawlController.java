/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package crawler;

/**
 *Clase encargada de establecer los parámetros de configuración del crawler
 * @author francisco
 */
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

/**
 * @author francisco
 */
public class BasicCrawlController {

        public static void main(String[] args) throws Exception {
               /* if (args.length != 2) {
                        System.out.println("Needed parameters: ");
                        System.out.println("\t rootFolder (it will contain intermediate crawl data)");
                        System.out.println("\t numberOfCralwers (number of concurrent threads)");
                        return;
                }
                   FileReader fr = abrirLecturaFicheroPartidos("/home/francisco/Dropbox/TFG/data/1-2div");
                   readMatchesFile(fr);
                */
                /*
                 * crawlStorageFolder is a folder where intermediate crawl data is
                 * stored.
                 */
                String crawlStorageFolder = "/home/francisco/crawler/";

                /*
                 * numberOfCrawlers shows the number of concurrent threads that should
                 * be initiated for crawling.
                 */
                int numberOfCrawlers = 8;

                CrawlConfig config = new CrawlConfig();

                config.setCrawlStorageFolder(crawlStorageFolder);

                /*
                 * Be polite: Make sure that we don't send more than 1 request per
                 * second (1000 milliseconds between requests).
                 */
                config.setPolitenessDelay(10);

                /*
                 * You can set the maximum crawl depth here. The default value is -1 for
                 * unlimited depth
                 */
                config.setMaxDepthOfCrawling(1);

                /*
                 * You can set the maximum number of pages to crawl. The default value
                 * is -1 for unlimited number of pages
                 */
                config.setMaxPagesToFetch(-1);

                /*
                 * Do you need to set a proxy? If so, you can use:
                 * config.setProxyHost("proxyserver.example.com");
                 * config.setProxyPort(8080);
                 *
                 * If your proxy also needs authentication:
                 * config.setProxyUsername(username); config.getProxyPassword(password);
Comment by tharindu...@gmail.com, Sep 15, 2013:

Isn't it proxy.setProxyPassword(password) ?

                 */

                /*
                 * This config parameter can be used to set your crawl to be resumable
                 * (meaning that you can resume the crawl from a previously
                 * interrupted/crashed crawl). Note: if you enable resuming feature and
                 * want to start a fresh crawl, you need to delete the contents of
                 * rootFolder manually.
                 */
                config.setResumableCrawling(false);

                /*
                 * Instantiate the controller for this crawl.
                 */
                PageFetcher pageFetcher = new PageFetcher(config);
                RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
                RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
                CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

                /*
                 * For each crawl, you need to add some seed urls. These are the first
                 * URLs that are fetched and then the crawler starts following links
                 * which are found in these pages
                 */

//                StringBuilder sb = new StringBuilder();
//                for (int z = 0; z<700000; z++){
//                    sb.append("http://www.bdfutbol.com/es/e/e");
//                    sb.append(z);
//                    sb.append(".html");
//                    controller.addSeed(sb.toString());
//                    sb.delete(0, sb.length());
//                }
//                for (int z = 50000; z<700000; z++){
//                    sb.append("http://www.bdfutbol.com/es/l/l");
//                    sb.append(z);
//                    sb.append(".html");
//                    controller.addSeed(sb.toString());
//                    sb.delete(0, sb.length());
//                }
//                for (int z = 500296; z<700000; z++){
//                    sb.append("http://www.bdfutbol.com/es/j/j");
//                    sb.append(z);
//                    sb.append(".html");
//                    controller.addSeed(sb.toString());
//                    sb.delete(0, sb.length());
//                }
                System.out.println("start");
                controller.addSeed("http://www.bdfutbol.com/es/index.html");
                //controller.addSeed("http://www.bdfutbol.com/es/j/j2187.html");
                //controller.addSeed("http://www.bdfutbol.com/es/a/jp.html");
                //controller.addSeed("http://www.bdfutbol.com/es/t/t2012-13.html");
                //controller.addSeed("http://www.bdfutbol.com/es/a/j___valencia.html");
                //controller.addSeed("http://www.ics.uci.edu/~welling/");

                /*
                 * Start the crawl. This is a blocking operation, meaning that your code
                 * will reach the line after this only when crawling is finished.
                 */
                controller.start(BasicCrawler.class, numberOfCrawlers);
        }
}

