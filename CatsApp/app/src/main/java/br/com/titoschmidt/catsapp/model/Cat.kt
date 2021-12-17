package br.com.titoschmidt.catsapp.model

import com.google.gson.annotations.SerializedName

/*
 * 15/12/2021
 * @author titoesa1988@gmail.com (Tito Schmidt).
 * 
 */

data class Cat(
    @SerializedName("link") var imgUrl: String = ""
)

data class CatsResponse(
    @SerializedName("data") var catsArr : ArrayList<Cat>
)

