package br.com.titoschmidt.chucknorrisio.models;

import com.google.gson.annotations.SerializedName;

public class Joke {

    @SerializedName("icon_url")
    private final String iconUrl;
    @SerializedName("value")
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
