package com.Anilkumawat3104.musicplayer

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private  lateinit var Recycler3 : RecyclerView
    private lateinit var useradapter : musicAdapter
    private lateinit var shuffle : Button
    private lateinit var favourites : Button
    private lateinit var Playlist : Button
    private lateinit var navView : NavigationView
    lateinit var Drawer: DrawerLayout
    var display = ArrayList<music>()

    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    companion object{
        var musicList = ArrayList<music>()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPinkNav)
        setContentView(R.layout.activity_main)
        //initialize the variable
        Drawer = findViewById(R.id.Drawer)

        actionBarDrawerToggle = ActionBarDrawerToggle(this, Drawer, R.string.nav_open, R.string.nav_close)

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        Drawer.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        // to make the Navigation drawer icon always appear on the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if(runtimepermission())
        initializeVariable()
        favourites.setOnClickListener{
            val intent = Intent(this@MainActivity, Favourite::class.java)
            // start your next activity
            startActivity(intent)
        }
        Playlist.setOnClickListener{
            val intent = Intent(this@MainActivity,PlayList::class.java)
            // start your next activity
            startActivity(intent)
        }
        shuffle.setOnClickListener{
            val intent = Intent(this@MainActivity,Activity_player::class.java)
            // start your next activity
            intent.putExtra("Index",0)
            intent.putExtra("class","MainActivity")
            startActivity(intent)
        }
        navView.setNavigationItemSelectedListener{
            when (it.itemId){
                  R.id.navFeedback -> Toast.makeText(this,"Feedback",Toast.LENGTH_SHORT).show()
                R.id.navSettings -> Toast.makeText(this,"Settings",Toast.LENGTH_SHORT).show()
                R.id.navAbout -> Toast.makeText(this,"About",Toast.LENGTH_SHORT).show()
                R.id.navExit -> exitProcess(1)

            }
            true
        }

    }
    private fun initializeVariable(){
        shuffle = findViewById(R.id.shuffle)
        favourites = findViewById(R.id.favourites)
        Playlist = findViewById(R.id.Playlist)

        navView = findViewById(R.id.navView1)
        musicList = getAllAudio()
        display.addAll(musicList)
        Recycler3 = findViewById(R.id.song_recyclerView)
        Recycler3.setHasFixedSize(true)
        Recycler3.setItemViewCacheSize(13)
        Recycler3.layoutManager = LinearLayoutManager(this@MainActivity)
        useradapter = musicAdapter(this@MainActivity,display)
        Recycler3.adapter = useradapter
        findViewById<TextView>(R.id.total_song).text = "Total song "+"${musicList.size}"
    }
    private fun runtimepermission() : Boolean{
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),13)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==13){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "permission Granted", Toast.LENGTH_SHORT).show()
                initializeVariable()
            }
        }
        else{
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),13)
        }
    }
            override fun onOptionsItemSelected(item: MenuItem): Boolean {
            return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
                true
            } else super.onOptionsItemSelected(item)
        }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        val menuItem = menu!!.findItem(R.id.search)
        if(menuItem!=null){
            val searchView = menuItem.actionView as SearchView
            val searchBox = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
            searchBox.hint = "Search.."
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    if(p0!!.isNotEmpty()){
                        display.clear()
                        val search = p0.toLowerCase(Locale.getDefault())
                        musicList.forEach {
                            if(it.title.toLowerCase(Locale.getDefault()).contains(search)){
                                display.add(it)
                            }
                        }
                        useradapter.notifyDataSetChanged()
                    }
                    else{
                        display.clear()
                        display.addAll(musicList)
                        useradapter.notifyDataSetChanged()
                    }
                    return true
                }
            })
        }
        return super.onCreateOptionsMenu(menu)
    }

    @SuppressLint("Range")
    private fun getAllAudio() : ArrayList<music>{
        val tempList = ArrayList<music>()
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!=0"
        val projection = arrayOf(MediaStore.Audio.Media._ID,MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.ALBUM,MediaStore.Audio.Media.ARTIST
        ,MediaStore.Audio.Media.DURATION,MediaStore.Audio.Media.DATE_ADDED,MediaStore.Audio.Media.DATA,MediaStore.Audio.Media.ALBUM_ID)
        val cusor = this.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,selection,null,MediaStore.Audio.Media.DATE_ADDED + " DESC",null)
        if(cusor != null){
            if(cusor.moveToFirst())
                do{
                    val title = cusor.getString(cusor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                    val id = cusor.getString(cusor.getColumnIndex(MediaStore.Audio.Media._ID))
                    val album = cusor.getString(cusor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
                    val artist = cusor.getString(cusor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val path = cusor.getString(cusor.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val duration = cusor.getLong(cusor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                    val albumc = cusor.getLong(cusor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)).toString()
                    val uri = Uri.parse("content://media/external/audio/albumart")
                    val artUric = Uri.withAppendedPath(uri,albumc).toString()
                    val music = music(id, title, album, artist, duration, path,artUric)
                    val file = File(music.path)
                    if(file.exists())
                        tempList.add(music)
                }while (cusor.moveToNext())
                cusor.close()
        }
        return tempList
    }
}
