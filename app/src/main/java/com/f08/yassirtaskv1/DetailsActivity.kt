package com.f08.yassirtaskv1

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.astritveliu.boom.utils.BoomUtils
import com.f08.yassirtaskv1.Models.MovieDetails
import com.f08.yassirtaskv1.interfaces.MovieDetailsApi
import com.facebook.shimmer.ShimmerFrameLayout
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_details.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailsActivity : AppCompatActivity() {
     var movie_id = 0
    lateinit var mUtils:Utils
    lateinit var shimmerFrameLayout:ShimmerFrameLayout
    lateinit var swipeRefreshLayout:SwipeRefreshLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        BoomUtils.boomAll(btn_back,btn_add_fav,btn_share,btn_watch,btn_download)
        movie_id = intent.extras!!.getInt("id",0)
        mUtils = Utils(this)
        shimmerFrameLayout = findViewById(R.id.shimmer_view_container)
        swipeRefreshLayout = findViewById(R.id.swipe_layout)
        checkNetworkAndUpdateViews()
        swipeRefreshLayout.setOnRefreshListener {
            checkNetworkAndUpdateViews()
        }

        btn_back.setOnClickListener { finish() }
    }

    fun checkNetworkAndUpdateViews(){
        if (mUtils.isNetworkAvailable()) {
            getMovieDetails()
        } else {
            mUtils.showMotionToast("Error !","Please Check your internet connection","warning")
            shimmerFrameLayout.stopShimmer()
            shimmerFrameLayout.visibility = View.GONE
            swipeRefreshLayout.isRefreshing = false

        }
    }



    private fun getMovieDetails() {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Config().API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        val api: MovieDetailsApi = retrofit.create(MovieDetailsApi::class.java)
        val call: Call<MovieDetails?>? = api.getDetails(movie_id,Config().API_KEY)

        call!!.enqueue(object : Callback<MovieDetails?> {
            override fun onResponse(call: Call<MovieDetails?>, response: Response<MovieDetails?>) {
                if (response.code() == 200) {
                    val movie = response.body()
                    image_thumb.visibility = View.VISIBLE
                    float_btns.visibility = View.VISIBLE

                    setMovieDetails(movie!!)


                    shimmerFrameLayout.stopShimmer()
                    shimmerFrameLayout.visibility = View.GONE
                    swipeRefreshLayout.isRefreshing = false

                } else {

                    shimmerFrameLayout.stopShimmer()
                    shimmerFrameLayout.visibility = View.GONE
                    swipeRefreshLayout.isRefreshing = false

                }
            }



            override fun onFailure(call: Call<MovieDetails?>, t: Throwable) {
                mUtils.showMotionToast("Error !","ERROR : ${t.message.toString()}","error")
                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.visibility = View.GONE
                swipeRefreshLayout.isRefreshing = false
            }
        })
    }

    @SuppressLint("SetTextI18n")
    fun setMovieDetails(movie:MovieDetails){
        movie_name.text = movie.title
        tv_details.text = movie.overview
        tv_release_date.text = "Published In : ${movie.release_date}"

        if (movie.backdrop_path !="" && movie.backdrop_path !=null){
            Picasso.get().load(mUtils.getBackdropFullPath(movie.backdrop_path!!)).fit().centerCrop()
                .placeholder(R.drawable.poster_placeholder)
                .into(poster_iv)
        }

        if (movie.poster_path !="" && movie.poster_path !=null){
            Picasso.get().load(mUtils.getPosterFullPath(movie.poster_path!!)).fit().centerCrop()
                .placeholder(R.drawable.poster_placeholder)
                .into(image_thumby)
        }

        var genersTxt = ""

        for (g in movie.genres!!){
            genersTxt += "${g.name}, "
        }
        tv_genre.text =genersTxt.substring(0,genersTxt.length-2)
        genre_tv.text =genersTxt.substring(0,genersTxt.length-2)
    }
}