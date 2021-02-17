package com.example.favoritesapplication.handlers

import com.example.favoritesapplication.models.YTChannel
import com.google.firebase.database.*

class YTChannelsHandler {
    var database : FirebaseDatabase
    var ytChannelRef : DatabaseReference


    init {
        database = FirebaseDatabase.getInstance()
        ytChannelRef = database.getReference("YTChannels")
    }

    fun create(ytChannel: YTChannel): Boolean {
        val id = ytChannelRef.push().key
        ytChannel.id = id
        ytChannelRef.child(id!!).setValue(ytChannel)
        return true
    }

    fun update(ytChannel: YTChannel): Boolean {
        ytChannelRef.child(ytChannel.id!!).setValue(ytChannel)
        return true
    }

    fun delete(ytChannel: YTChannel): Boolean{
        ytChannelRef.child(ytChannel.id!!).removeValue()
        return true
    }
}