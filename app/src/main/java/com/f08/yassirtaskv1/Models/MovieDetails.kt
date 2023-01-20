package com.f08.yassirtaskv1.Models

class MovieDetails {
    var id:Int?=null
    var title:String?=null
    var release_date:String?=null
    var poster_path:String?=null
    var backdrop_path:String?=null
    var overview:String?=null
    var genres:ArrayList<Genres>?=null
    // i wanted to add genres but it may take some time to fetch genres names by ids
    constructor(id:Int,title:String,release_date:String,poster_path:String,backdrop_path:String,
                overview:String,genres:ArrayList<Genres>){
        this.id=id
        this.title=title
        this.release_date=release_date
        this.poster_path=poster_path
        this.backdrop_path=backdrop_path
        this.overview=overview
        this.genres=genres
    }

    constructor()
}