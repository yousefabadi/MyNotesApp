package com.yousefalsaaidah.mynoteapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.ticket.view.*

class MainActivity : AppCompatActivity() {
    var listNotes = ArrayList<Note>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //add rubish data
        //listNotes.add(Note(1,"Meet my supervisor","The Quran is the central religious text of Islam, which Muslims believe to be a revelation from God. It is widely regarded as the finest work in classical Arabic literature."))
        //listNotes.add(Note(2,"Meet my brother","The Quran is the central religious text of Islam, which Muslims believe to be a revelation from God. It is widely regarded as the finest work in classical Arabic literature."))
        //listNotes.add(Note(3,"Meet my friend","The Quran is the central religious text of Islam, which Muslims believe to be a revelation from God. It is widely regarded as the finest work in classical Arabic literature."))



        //load from DB
        LoadQuery("%")

    }

    override fun onResume() {
        super.onResume()
        LoadQuery("%")
    }

    fun LoadQuery(title:String){
        var dbManager = DBManager(this)
        val projection= arrayOf("ID","Title","Description")
        val selectionArgs = arrayOf(title)
        val cursor = dbManager.Query(projection,"Title like ?",selectionArgs, "Title")
        listNotes.clear()
        if(cursor.moveToFirst()){
            do{
                val ID=cursor.getInt(cursor.getColumnIndex("ID"))
                val Title=cursor.getString(cursor.getColumnIndex("Title"))
                val Description=cursor.getString(cursor.getColumnIndex("Description"))

                listNotes.add(Note(ID,Title,Description))

            }while(cursor.moveToNext())
        }
        var myNoteAdapter = MyNotesAdapter(this,listNotes)
        lvNotes.adapter = myNoteAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)

        val sv = menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        val sm = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                Toast.makeText(applicationContext,p0, Toast.LENGTH_LONG).show()
                LoadQuery("%"+p0+"%")
                return false
            }

        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when(item.itemId){
                R.id.menu_add_note->{
                    var intent = Intent(this,AddNotes::class.java)
                    startActivity(intent)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun delNote(){
        Toast.makeText(this,"del clicked",Toast.LENGTH_LONG).show()
    }

    inner class MyNotesAdapter:BaseAdapter{
        var listNotesAdapter = ArrayList<Note>()
        var context:Context?=null
        constructor(context: Context, listNotes: ArrayList<Note>):super(){
            this.listNotesAdapter = listNotes
            this.context = context

        }
        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            var myView = layoutInflater.inflate(R.layout.ticket,null)
            var myNote = listNotesAdapter[p0]
            myView.tvTitle.text = myNote.noteTitle
            myView.tvDes.text = myNote.noteDes
            myView.ivDelete.setOnClickListener(View.OnClickListener {
                var dbManager = DBManager(this.context!!)
                val selectionArgs = arrayOf(myNote.noteID.toString())
                dbManager.Delete("ID=?",selectionArgs)
                LoadQuery("%")
            })
            myView.ivEdit.setOnClickListener(View.OnClickListener {
                GoToUpdate(myNote)
            })
            return myView
        }

        override fun getItem(p0: Int): Any {
            return listNotesAdapter[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
            return listNotesAdapter.size
        }
    }
    fun GoToUpdate(note:Note){
        var intent = Intent(this,AddNotes::class.java)
        intent.putExtra("ID",note.noteID)
        intent.putExtra("title",note.noteTitle)
        intent.putExtra("des",note.noteDes)
        startActivity(intent)
    }
}
