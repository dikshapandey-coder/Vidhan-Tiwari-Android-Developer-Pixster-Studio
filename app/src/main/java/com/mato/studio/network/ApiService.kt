package com.mato.studio.network

import com.mato.studio.network.response.ExchangeRateResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("v6/{apiKey}/pair/{base}/{target}/{amount}")
    suspend fun convertPairAmount(
        @Path("apiKey") apiKey: String,
        @Path("base") base: String,
        @Path("target") target: String,
        @Path("amount") amount: Double
    ): Response<ExchangeRateResponse>
}