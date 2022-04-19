package com.app.alchemi.roomdatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CardDao {
     @Query("SELECT * FROM cardDetail")
    suspend fun getAll(): List<CardDetail>
     @Query("DELETE FROM cardDetail")
     suspend fun deleteAll()
    @Query("SELECT * FROM cardDetail WHERE uid IN (:userIds)")
    suspend fun loadAllByIds(userIds: IntArray): List<CardDetail>

//    @Query("SELECT * FROM cardDetail WHERE name LIKE :name LIMIT 1")
//    fun findByName(name: String): CardDetail

    @Insert
    suspend fun insertAll(vararg cardDetail: CardDetail)

    @Delete
    suspend fun delete(cardDetail: CardDetail)
}