package br.com.titoschmidt.catsapp.model

import com.google.gson.annotations.SerializedName

/*
 * 15/12/2021
 * @author titoesa1988@gmail.com (Tito Schmidt).
 * 
 */

data class CatsResponse(
    @SerializedName("data")
    val cats: List<Cat>
)

data class Cat(
    @SerializedName("link")
    val imgUrl: String = ""
)

