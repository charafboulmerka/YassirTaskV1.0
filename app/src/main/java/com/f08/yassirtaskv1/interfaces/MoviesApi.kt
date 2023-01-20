package com.f08.yassirtaskv1.interfaces
import com.f08.yassirtaskv1.Models.MoviesMultipleResources
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApi {
    @GET("/3/discover/movie")
    fun getMovies(@Query("api_key") key: String?,@Query("page") page: Int?): Call<MoviesMultipleResources?>?
}