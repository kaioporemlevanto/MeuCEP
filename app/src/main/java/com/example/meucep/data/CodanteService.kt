package com.example.meucep.data

import com.example.meucep.model.EstadoFlag
import retrofit2.Response
import retrofit2.http.GET

interface CodanteService {
    @GET("bandeiras-dos-estados")
    suspend fun getFlags(): Response<List<EstadoFlag>>
}