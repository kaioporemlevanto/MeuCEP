package com.example.meucep.data

class CepRepository(private val apiService: ViaCepService) {
    suspend fun fetchCep(cep: String) = apiService.getCepInfo(cep)
}