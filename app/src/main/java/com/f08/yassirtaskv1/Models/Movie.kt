package com.f08.yassirtaskv1.Models

class Movie {
    var id:Int?=null
    var title:String?=null
    var year:String?=null
    var genres:ArrayList<Genres>?=null
    constructor(id:Int,title:String,year:String,genres:ArrayList<Genres>){
        this.id=id
        this.title=title
        this.year=year
        this.genres=genres
    }
}