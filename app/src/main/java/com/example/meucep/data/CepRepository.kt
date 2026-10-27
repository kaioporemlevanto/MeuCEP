package com.example.meucep.data

class CepRepository(
    private val viaCepService: ViaCepService,
    private val codanteService: CodanteService
) {
    suspend fun fetchCep(cep: String) = viaCepService.getCep(cep)
    suspend fun fetchFlags() = codanteService.getFlags()
}