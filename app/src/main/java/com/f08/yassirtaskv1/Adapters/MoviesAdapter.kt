package com.f08.yassirtaskv1.Adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.balysv.materialripple.MaterialRippleLayout
import com.f08.yassirtaskv1.DetailsActivity
import com.f08.yassirtaskv1.ItemAnimation
import com.f08.yassirtaskv1.Models.Movie
import com.f08.yassirtaskv1.R
import com.f08.yassirtaskv1.Utils
import com.squareup.picasso.Picasso

class MoviesAdapter : RecyclerView.Adapter<MoviesAdapter.YassirMovieViewHolder> {
    private var items: List<Movie> = ArrayList<Movie>()
    private lateinit var mCtx: Context
    private var lastPosition = -1
    private var on_attach = true
    private val animation_type = 2

    constructor(mCtx: Context, items: List<Movie>){
        this.items = items
        this.mCtx = mCtx
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YassirMovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_grid_item, parent
            , false)
        return YassirMovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: YassirMovieViewHolder, position: Int) {
        val mMovie: Movie = items[position]

        holder.releaseYear.text = mMovie.release_date!!.substring(0,4)
        holder.name.text = mMovie.title

        if (!mMovie.poster_path!!.isEmpty()) {
            Picasso.get().load(Utils(mCtx)
                .getPosterFullPath(mMovie.poster_path!!))
                .fit().centerCrop()
                .placeholder(R.drawable.poster_placeholder).into(holder.image)
        } else {
            Picasso.get().load(R.drawable.poster_placeholder).fit().centerCrop()
                .placeholder(R.drawable.poster_placeholder).into(holder.image)
        }

        holder.lyt_parent.setOnClickListener {
            goToDetailsActivity(mMovie)
        }
        setAnimation(holder.itemView, position)
    }

    private fun goToDetailsActivity(movie: Movie) {
        val intent = Intent(mCtx, DetailsActivity::class.java)
        intent.putExtra("id", movie.id)
        mCtx.startActivity(intent)
    }

    override fun getItemCount(): Int {
        return items.size
    }

     class YassirMovieViewHolder(var view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

         var image = view.findViewById<ImageView>(R.id.image)
         var name = view.findViewById<TextView>(R.id.name)
         var lyt_parent = view.findViewById<MaterialRippleLayout>(R.id.lyt_parent)
         var releaseYear = view.findViewById<TextView>(R.id.release_year)

         override fun onClick(p0: View?) {
             Log.e("BUTTON","CLICKED")
         }
     }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                on_attach = false
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        super.onAttachedToRecyclerView(recyclerView)
    }

    private fun setAnimation(view: View, position: Int) {
        if (position > lastPosition) {
            ItemAnimation.animate(view, if (on_attach) position else -1, animation_type)
            lastPosition = position
        }
    }
}