package com.yousefalsaaidah.mynoteapp

import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_notes.*

class AddNotes : AppCompatActivity() {
    val dbTable ="Notes"
    var id =0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)

        try{
            var bundle:Bundle = intent.extras
            id=bundle.getInt("ID",0)
            if(id!=0) {
                etTitle.setText(bundle.getString("title"))
                etDes.setText(bundle.getString("des"))
            }
        }catch(ex:Exception){}

    }

    fun buAdd(view: View){
        var dbManager = DBManager(this)

        var values = ContentValues()
        values.put("Title",etTitle.text.toString())
        values.put("Description",etDes.text.toString())

        //Add
        if(id==0){
            val ID = dbManager.Insert(values)
            if(ID>0){
                Toast.makeText(this,"Note add",Toast.LENGTH_LONG).show()
                finish()
            }else{
                Toast.makeText(this,"Unable to add the note",Toast.LENGTH_LONG).show()
            }
            //Update
        }else{
            var selectionArgs = arrayOf(id.toString())
            val ID = dbManager.Update(values,"ID=?",selectionArgs)
            if(ID>0){
                Toast.makeText(this,"Note add",Toast.LENGTH_LONG).show()
                finish()
            }else{
                Toast.makeText(this,"Unable to add the note",Toast.LENGTH_LONG).show()
            }
        }

    }
}
