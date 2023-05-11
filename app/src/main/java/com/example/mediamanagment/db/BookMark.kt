package com.example.mediamanagment.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookMark_table")
data class BookMark(@PrimaryKey(autoGenerate = true)var Id : Int?,
                    val title : String, val url : String)
