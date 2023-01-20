package com.f08.yassirtaskv1.interfaces
import com.f08.yassirtaskv1.Models.MovieDetails
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDetailsApi {
    @GET("/3/movie/{id}")
    fun getDetails(@Path("id") id: Int?, @Query("api_key") key: String?): Call<MovieDetails?>?
}