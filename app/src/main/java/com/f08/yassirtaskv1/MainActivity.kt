package com.f08.yassirtaskv1

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.f08.yassirtaskv1.Adapters.MoviesAdapter
import com.f08.yassirtaskv1.Models.Movie
import com.f08.yassirtaskv1.Models.MoviesMultipleResources
import com.f08.yassirtaskv1.interfaces.MoviesApi
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var ViewlayoutManager: RecyclerView.LayoutManager
    private var mList = ArrayList<Movie>()
    //private lateinit var viewsList:ArrayList<View>
    lateinit var mUtils: Utils
    var pageCount = 1
    var isLoading = false
    lateinit var shimmerFrameLayout: ShimmerFrameLayout
    lateinit var progressBar: ProgressBar
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var tvNoItem: TextView

    val HIDE_THRESHOLD = 20
    var scrolledDistance = 0
    var controlsVisible = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()

    }


    fun init(){
        mUtils = Utils(this)
        progressBar = findViewById(R.id.item_progress_bar)
        shimmerFrameLayout = findViewById(R.id.shimmer_view_container)
        shimmerFrameLayout.startShimmer()
        swipeRefreshLayout = findViewById(R.id.swipe_layout)
        coordinatorLayout = findViewById(R.id.coordinator_lyt)
        tvNoItem = findViewById<TextView>(R.id.tv_noitem)

        ViewlayoutManager = GridLayoutManager(this,3)
        viewAdapter = MoviesAdapter(this, mList)

        recyclerView = findViewById<RecyclerView>(R.id.movies_recyclerView).apply {
            setHasFixedSize(true)
            ViewlayoutManager.isAutoMeasureEnabled = false
            isNestedScrollingEnabled = false
            layoutManager = ViewlayoutManager
            adapter = viewAdapter
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && !isLoading) {
                    coordinatorLayout.visibility = View.GONE
                    pageCount += + 1
                    isLoading = true
                    progressBar.visibility = View.VISIBLE
                    getMovies(pageCount)
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                    controlsVisible = false
                    scrolledDistance = 0
                } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                    controlsVisible = true
                    scrolledDistance = 0
                }
                if ((controlsVisible && dy > 0 || !controlsVisible) && dy < 0) {
                    scrolledDistance += dy
                }
            }
        })

        checkNetworkAndUpdateViews()

        swipeRefreshLayout.setOnRefreshListener(OnRefreshListener {
            coordinatorLayout.visibility = View.GONE
            pageCount = 1
            mList.clear()
            recyclerView.removeAllViews()
            viewAdapter.notifyDataSetChanged()
            checkNetworkAndUpdateViews()
        })

    }

    fun checkNetworkAndUpdateViews(){
        if (mUtils.isNetworkAvailable()) {
            getMovies(pageCount)
        } else {
            tvNoItem.text = resources.getString(R.string.no_internet)
            shimmerFrameLayout.stopShimmer()
            shimmerFrameLayout.visibility = View.GONE
            swipeRefreshLayout.isRefreshing = false
            coordinatorLayout.visibility = View.VISIBLE
        }
    }



    private fun getMovies(page: Int) {
        //Toast.makeText(this@MainActivity,"called",Toast.LENGTH_LONG).show()
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Config().API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        val api: MoviesApi = retrofit.create(MoviesApi::class.java)
        val call: Call<MoviesMultipleResources?>? = api.getMovies(Config().API_KEY, page)

        call!!.enqueue(object : Callback<MoviesMultipleResources?> {
            override fun onResponse(call: Call<MoviesMultipleResources?>, response: Response<MoviesMultipleResources?>) {
                if (response.code() == 200) {
                   // Toast.makeText(this@MainActivity,"SUCCESS:${response.body()!!.results!!.get(0).title}",Toast.LENGTH_LONG).show()

                    isLoading = false
                    progressBar.visibility = View.GONE
                    shimmerFrameLayout.stopShimmer()
                    shimmerFrameLayout.visibility = View.GONE
                    swipeRefreshLayout.isRefreshing = false
                    if (response.toString().length < 10 && pageCount == 1) {
                        coordinatorLayout.visibility = View.VISIBLE
                    } else {
                        coordinatorLayout.visibility = View.GONE
                    }
                    mList.addAll(response.body()!!.results!!)
                   // println("CF1013")
                    //println(response.body())
                    viewAdapter.notifyDataSetChanged()
                } else {
                    isLoading = false
                    progressBar.visibility = View.GONE
                    shimmerFrameLayout.stopShimmer()
                    shimmerFrameLayout.visibility = View.GONE
                    swipeRefreshLayout.isRefreshing = false
                    if (pageCount == 1) {
                        coordinatorLayout.visibility = View.VISIBLE
                    }
                }
            }



            override fun onFailure(call: Call<MoviesMultipleResources?>, t: Throwable) {
                mUtils.showMotionToast("Error !","ERROR : ${t.message.toString()}","error")
                isLoading = false
                progressBar.visibility = View.GONE
                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.visibility = View.GONE
                swipeRefreshLayout.isRefreshing = false
                if (pageCount == 1) {
                    coordinatorLayout.visibility = View.VISIBLE
                }
            }
        })
    }
}