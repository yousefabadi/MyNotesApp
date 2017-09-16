package com.yousefalsaaidah.mynoteapp

/**
 * Created by Toshiba on 9/15/2017.
 */

class Note{
    var noteID:Int?=null
    var noteTitle:String?=null
    var noteDes:String?=null

    constructor(noteID:Int,noteTitle:String,noteDes:String){
        this.noteID = noteID
        this.noteTitle = noteTitle
        this.noteDes = noteDes
    }

}