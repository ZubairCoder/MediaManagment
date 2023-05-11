package com.example.viewmodel.ui.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mediamanagment.db.BookMark

@Dao
interface DataDao {
    @Insert
    suspend fun  dataInsert(bookMark: BookMark)

    @Query("SELECT * FROM bookMark_table")
    fun getAllData() : LiveData<List<BookMark>>

    @Query("SELECT * FROM bookMark_table where id like :id limit 1")
    suspend fun getOneData(id : Int) : BookMark

    @Update
    fun updateData(bookMark: BookMark)

    @Delete
    fun deleteData(bookMark: BookMark)
}