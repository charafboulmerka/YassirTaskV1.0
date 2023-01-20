package com.f08.yassirtaskv1.Models

class MoviesMultipleResources {
    var page:Int?=null
    var total_pages:Int?=null
    var results:ArrayList<Movie>?=null
    constructor(page:Int,total_pages:Int,results:ArrayList<Movie>){
        this.page=page
        this.total_pages=total_pages
        this.results=results
    }

    constructor()
}