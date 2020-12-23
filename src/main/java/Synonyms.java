import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Synonyms {
    public static void getSynonyms() throws IOException {
        Word2Vec w2vModel = WordVectorSerializer.readWord2VecModel(LocalPaths.SYNONYMS_MODEL);
        List<Collection<String>> result = new ArrayList<>();
        List<String> words = Files.readAllLines(Paths.get(LocalPaths.WORDS_FROM_TITLES_FILE));
        for (String word: words) {
            Collection<String> synonyms = w2vModel.wordsNearest(word, 5);
            if (!(synonyms == null || synonyms.isEmpty())) {
                synonyms.add(word);
                result.add(synonyms);
            }
        }
        WorkWithFiles.writeSynonyms(result, LocalPaths.SYNONYMS_FILE);
    }

} 