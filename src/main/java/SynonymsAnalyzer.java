import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.synonym.SynonymGraphFilter;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.util.CharsRef;

import java.util.List;

public class SynonymsAnalyzer extends Analyzer {
    @Override
    protected TokenStreamComponents createComponents(String s) {
        SynonymMap synonymMap = null;
        SynonymMap.Builder builder=new SynonymMap.Builder(true);
        try {
            List<List<String>> synonyms = WorkWithFiles.readSynonyms(LocalPaths.SYNONYMS_FILE);
            synonyms.forEach(x -> x.forEach(String::trim));
            for (List<String> words: synonyms) {
                for (String input: words) {
                    for (String output: words) {
                        builder.add(new CharsRef(input), new CharsRef(output), false);
                    }
                }
            }
            synonymMap = builder.build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Tokenizer tokenizer = new StandardTokenizer();
        assert synonymMap != null;
        TokenStream filter = new SynonymGraphFilter(tokenizer, synonymMap, true);
        return new TokenStreamComponents(tokenizer, filter);
    }

}