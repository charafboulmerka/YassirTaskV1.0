package com.f08.yassirtaskv1.interfaces
import com.f08.yassirtaskv1.Models.Movie
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApi {
    @GET("/movies")
    fun getMovie(@Query("id") title: String?): Call<Movie?>?
}