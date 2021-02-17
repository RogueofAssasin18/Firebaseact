package com.example.favoritesapplication.models

class YTChannel(var id:String?="", var title:String?="", var link:String?="", var ranking:Int=0, var reason:String=""){
    override fun toString(): String {
        return "$ranking - $title"
    }
}