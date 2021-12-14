package co.titoschmidt.tutorial.jokerappdev.model

import com.google.gson.annotations.SerializedName

/*
 * 13/12/2021
 * @author titoesa@gmail.com (Tito Schmidt).
 * 
 */
data class Joke(

    @SerializedName("value") val text : String,
    @SerializedName("icon_url") val iconUrl : String
)
