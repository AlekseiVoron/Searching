import java.util.Objects;

public class Article {
    public String title;
    public String ref;
    public String text;
    public Float score;

    public Article(String title, String ref, String text, Float score) {
        this.title = title;
        this.ref = ref;
        this.text = text;
        this.score = score;
    }

    public Article(String title, String ref, String text) {
        this.title = title;
        this.ref = ref;
        this.text = text;
        this.score = (float) 0.;
    }

    @Override
    public String toString() {
        return "Article: title = " + title + ": " + ref;
    }

    public String toStringWithScore() {
        return "Article: title = " + title + " (" + ref + "): " + "score = " + score;
    }

    public String toStringWithText() {
        return "Article: title = " + title + " (" + ref + "): " + text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(title, article.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}
