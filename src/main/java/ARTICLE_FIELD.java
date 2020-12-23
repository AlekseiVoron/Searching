public enum ARTICLE_FIELD {
    TITLE ("title"),
    REF ("ref"),
    TEXT ("text");

    private final String value;

    public String getValue() {
        return value;
    }

    ARTICLE_FIELD(String value) {
        this.value = value;
    }
}
