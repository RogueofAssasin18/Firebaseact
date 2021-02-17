package com.example.favoritesapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.favoritesapplication.handlers.YTChannelsHandler
import com.example.favoritesapplication.models.YTChannel
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    lateinit var titleEditText : EditText
    lateinit var linkEditText : EditText
    lateinit var rankingEditText : EditText
    lateinit var reasonEditText : EditText
    lateinit var addEditChannelBtn : Button
    lateinit var ytChannelHandler : YTChannelsHandler
    lateinit var ytChannels : ArrayList<YTChannel>
    lateinit var channelsListView : ListView
    lateinit var ytChannelToEdit: YTChannel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ytChannelHandler = YTChannelsHandler()
        titleEditText = findViewById(R.id.titleEditText)
        linkEditText = findViewById(R.id.linkEditText)
        rankingEditText = findViewById(R.id.rankingEditText)
        reasonEditText = findViewById(R.id.reasonEditText)
        addEditChannelBtn = findViewById(R.id.addEditChannelBtn)
        ytChannels = ArrayList()
        channelsListView = findViewById(R.id.channelsListView)

        addEditChannelBtn.setOnClickListener {
            val title = titleEditText.text.toString()
            val link = linkEditText.text.toString()
            val ranking = rankingEditText.text.toString().toInt()
            val reason = reasonEditText.text.toString()
            //create restaurant
            if(addEditChannelBtn.text.toString() == "Add") {
                val ytChannel = YTChannel(title = title, link = link, ranking = ranking, reason = reason)
                if (ytChannelHandler.create(ytChannel)) {
                    Toast.makeText(applicationContext, "Youtube channel added", Toast.LENGTH_SHORT).show()
                    clearFields()
                }
            }
            else if(addEditChannelBtn.text.toString() == "Update"){
                val ytChannel = YTChannel(id = ytChannelToEdit.id, title = title, link = link, ranking = ranking, reason = reason)
                if (ytChannelHandler.update(ytChannel)) {
                    Toast.makeText(applicationContext, "Channel updated", Toast.LENGTH_SHORT).show()
                    clearFields()
                }
            }
        }
        registerForContextMenu(channelsListView)
    }
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.favorites_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        return when(item.itemId){
            R.id.edit_channel -> {
                ytChannelToEdit = ytChannels[info.position]
                titleEditText.setText(ytChannelToEdit.title)
                linkEditText.setText(ytChannelToEdit.link)
                rankingEditText.setText(ytChannelToEdit.ranking).toString().toInt()
                reasonEditText.setText(ytChannelToEdit.reason)

                addEditChannelBtn.setText("Update")
                true
            }
            R.id.delete_channel -> {
                if(ytChannelHandler.delete(ytChannels[info.position])){
                    Toast.makeText(applicationContext, "Channel deleted.", Toast.LENGTH_SHORT).show()
                }
                true
            }
            else ->super.onOptionsItemSelected(item)
        }
    }
    override fun onStart() {
        super.onStart()
        /*---  ytChannelHandler.ytChannelRef.addChildEventListener(object : ChildEventListener {
             override fun onCancelled(error: DatabaseError) {
                 //TODO("Not yet implemented")
             }

             override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                 //TODO("Not yet implemented")
             }

             override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                 //TODO("Not yet implemented")
             }

             override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                 //TODO("Not yet implemented")
                 ytChannels.clear()
                  snapshot.children.forEach {
                     it -> val ytChannel = it.getValue(YTChannel::class.java)
                      ytChannels.add(ytChannel!!)
                 }
                 val adapter = ArrayAdapter<YTChannel>(applicationContext, android.R.layout.simple_list_item_1, ytChannels)
                 channelsListView.adapter = adapter
             }

             override fun onChildRemoved(snapshot: DataSnapshot) {
                 //TODO("Not yet implemented")
             }
         })--*/
        ytChannelHandler.ytChannelRef.addValueEventListener(object : ValueEventListener {
             override fun onCancelled(error: DatabaseError) {
                 //TODO("Not yet implemented")
             }

             override fun onDataChange(snapshot: DataSnapshot) {
                 ytChannels.clear()
                 snapshot.children.forEach {
                         it -> val ytChannel = it.getValue(YTChannel::class.java)
                     ytChannels.add(ytChannel!!)

                 }
                 val adapter = ArrayAdapter<YTChannel>(applicationContext, android.R.layout.simple_list_item_1, ytChannels)
                 channelsListView.adapter = adapter
             }
         })
    }
    fun clearFields(){
        titleEditText.text.clear()
        linkEditText.text.clear()
        rankingEditText.text.clear()
        reasonEditText.text.clear()
        addEditChannelBtn.setText("Add")
    }
}