package com.f08.yassirtaskv1

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class Utils {
    var mCtx:Context?=null

    constructor(mCtx:Context) {
        this.mCtx = mCtx
    }

    fun showMotionToast(title:String,msg:String,type:String){
        MotionToast.createColorToast(
            mCtx!! as Activity,
            title,
            msg,
            getCorrectMotionToastType(type),
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            null)
    }

    fun getCorrectMotionToastType(type:String): MotionToastStyle {
        if (type=="success"){
            return MotionToastStyle.SUCCESS
        }else if(type=="warning"){
            return MotionToastStyle.WARNING
        }else if(type=="error"){
            return MotionToastStyle.ERROR
        }
        return MotionToastStyle.SUCCESS
    }

    fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            mCtx!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun getPosterFullPath(path:String):String{
        return "${Config().POSTER_MAIN_URL}/$path"
    }

    fun getBackdropFullPath(path:String):String{
        return "${Config().BACKDROP_URL}/$path"
    }

}