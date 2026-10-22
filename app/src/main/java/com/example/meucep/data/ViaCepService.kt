package com.example.meucep.data

import com.example.meucep.model.CepResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ViaCepService {
    @GET("{cep}/json/")
    suspend fun getCepInfo(@Path("cep") cep: String): Response<CepResponse>
}
