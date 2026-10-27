package com.example.meucep.model

import java.io.Serializable

data class CepResponse(
    val cep: String = "",
    val logradouro: String = "",
    val complemento: String = "",
    val unidade: String = "",
    val bairro: String = "",
    val localidade: String = "",
    val uf: String = "",
    val estado: String = "",
    val regiao: String = "",
    val ibge: String = "",
    val gia: String = "",
    val ddd: String = "",
    val siafi: String = ""
) : Serializable