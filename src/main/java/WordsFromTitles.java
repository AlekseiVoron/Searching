import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class WordsFromTitles {
    public static void getWords() throws IOException {
        List<Article> articles = WorkWithFiles.readSeries(LocalPaths.DATA_FILE);
        Set<String> words = new HashSet<>();
        articles.forEach(article-> words.addAll(article == null ? new HashSet<>() : getWords(article.title)));
        WorkWithFiles.writeToFileByLine(words, LocalPaths.WORDS_FROM_TITLES_FILE);
    }

    private static Set<String> getWords(String phrase) {
        return Arrays.stream(phrase.split("\\s"))
                .map(x->x.trim()
                        .replaceAll("[.,!?;:\"()%№+-=*<>$&]", "")
                        .replaceAll("\\d+", ""))
                .filter(x->!x.isEmpty())
                .collect(Collectors.toSet());
    }
}