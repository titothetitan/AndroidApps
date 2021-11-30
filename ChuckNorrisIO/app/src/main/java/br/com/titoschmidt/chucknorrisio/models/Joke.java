package br.com.titoschmidt.chucknorrisio.models;

public class Joke {

    private final String iconUrl;
    private final String value;

    public Joke(String iconUrl, String joke) {
        this.iconUrl = iconUrl;
        this.value = joke;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getValue() {
        return value;
    }
}
