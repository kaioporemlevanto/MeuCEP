package com.example.meucep.model

import com.google.gson.annotations.SerializedName

data class EstadoFlag(
    val uf: String,
    val name: String,
    @SerializedName("flag_url") val flagUrl: String,
    @SerializedName("flag_url_rounded") val flagUrlRounded: String
)