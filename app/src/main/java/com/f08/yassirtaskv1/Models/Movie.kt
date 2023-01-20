package com.f08.yassirtaskv1.Models

class Movie {
    var id:Int?=null
    var title:String?=null
    var release_date:String?=null
    var poster_path:String?=null
   // var genres:ArrayList<Genres>?=null
   // i wanted to add genres but it may take some time to fetch genres names by ids
    constructor(id:Int,title:String,release_date:String,poster_path:String){
        this.id=id
        this.title=title
        this.release_date=release_date
        this.poster_path=poster_path
       // this.genres=genres
    }

    constructor()
}