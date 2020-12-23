import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Indexer {
    private final IndexWriter indexWriter;

    public Indexer(String indexDirectoryPath) throws IOException {
        Directory index = FSDirectory.open(new File(indexDirectoryPath).toPath());
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig();
        indexWriter = new IndexWriter(index, indexWriterConfig);
    }

    public void createIndex(ArrayList<Article> list) throws IOException {
        for(Article article: list) {
            indexWriter.addDocument(getDocument(article));
        }
        indexWriter.close();
    }

    private static Document getDocument(Article article) {
        Document document = new Document();
        document.add(new TextField(ARTICLE_FIELD.TITLE.getValue(), article.title, Field.Store.YES));
        document.add(new StringField(ARTICLE_FIELD.REF.getValue(), article.ref, Field.Store.YES));
        document.add(new TextField(ARTICLE_FIELD.TEXT.getValue(), article.text, Field.Store.YES));
        return document;
    }

}