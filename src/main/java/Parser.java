import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

public class Parser {
    private static final int COUNT_OF_PAGES = 200;

    public static void parse() throws IOException {
        ArrayList<Article> articles = new ArrayList<>();
        String dataFile = LocalPaths.DATA_FILE;
        if (Files.notExists(Paths.get(dataFile))) {
            Files.createFile(Paths.get(dataFile));
        } else {
            Files.write(Paths.get(dataFile), Collections.singleton(""));
        }
        System.out.println("********** Parsing **********");
        try (FileWriter ignored = new FileWriter(dataFile, false)) {
            for (int i = 0; i <= COUNT_OF_PAGES; i++) {
                System.out.println("---------- page #" + i + " ----------");
                if (i == 1) continue;  // this page is unavailable
                String url = "http://yard.hozvo.ru/?page=" + i;
                Document page = Jsoup.connect(url).userAgent("Yandex").timeout(1000 * 1000).get();
                Elements articlesOnPage = page.getElementsByClass("narod-item__link");
                articlesOnPage.remove(0);
                for (Element element: articlesOnPage) {
                    String articleRef = element.attr("href");
                    Document articleDoc = Jsoup.connect(articleRef).userAgent("Yandex").timeout(1000 * 1000).get();
                    String title = articleDoc.title();
                    String text = articleDoc.getElementsByClass("narod__detail-text").text();
                    Article article = new Article(title, articleRef, text);
                    articles.add(article);
                    System.out.println(article.toString());
                }
            }
            Indexer indexer = new Indexer(LocalPaths.INDEX_DIR);
            indexer.createIndex(articles);
            WorkWithFiles.writeToFile(articles, dataFile);
        }
        System.out.println("********** Parsing is done **********\n");
    }
}
