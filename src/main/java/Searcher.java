import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Searcher {
    public Set<Article> search(String constant, String searchingText, String indexDirectoryPath, Query query) throws IOException, ParseException {
        Directory indexDirectory = FSDirectory.open(new File(indexDirectoryPath).toPath());
        IndexReader indexReader = DirectoryReader.open(indexDirectory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        if (query == null) {
            QueryParser queryParser = new QueryParser(constant, new StandardAnalyzer());
            query = queryParser.parse(searchingText);
        }
        System.out.println("\nSearching query: '" + query + '\'');
        TopDocs docs = indexSearcher.search(query, 10000);
        Set<Article> searchedResults = new HashSet<>();
        for(ScoreDoc scoreDoc: docs.scoreDocs){
            Document document = indexSearcher.doc(scoreDoc.doc);
            String title = document.get(ARTICLE_FIELD.TITLE.getValue());
            String ref = document.get(ARTICLE_FIELD.REF.getValue());
            String text = document.get(ARTICLE_FIELD.TEXT.getValue());
            if (title == null || ref == null) continue;
            searchedResults.add(new Article(title, ref, text, scoreDoc.score));
        }
        return searchedResults;
    }

    public Set<Article> searchBySynonym(String text) throws ParseException, IOException {
        QueryParser queryParser = new QueryParser(ARTICLE_FIELD.TITLE.getValue(), new SynonymsAnalyzer());
        Query query = queryParser.parse(text);
        return search(null,null,LocalPaths.INDEX_DIR, query);
    }

    public static void search() throws IOException, ParseException {
        System.out.println("********** Searching **********");
        Searcher searcher = new Searcher();

        System.out.println("Enter a search word:");
        Scanner scanner = new Scanner(System.in);
        String search_word = scanner.next();
        Set<Article> results = searcher.search(ARTICLE_FIELD.TITLE.getValue(), search_word, LocalPaths.INDEX_DIR, null);
        System.out.println("Results:");
        int i = 1;
        for (Article article: results) {
            System.out.println(i++ + "# " + article.toStringWithScore());
        }

//        WordsFromTitles.getWords(); // get words from titles
//        Synonyms.getSynonyms(); // get synonyms from titles
        System.out.println("Enter a search word (with synonyms):");
        search_word = scanner.next();
        results = searcher.searchBySynonym(search_word);
        System.out.println("Results:");
        i = 1;
        for (Article article: results) {
            System.out.println(i++ + "# " + article.toStringWithScore());
        }

        System.out.println("********** Searching is done **********");
    }
}