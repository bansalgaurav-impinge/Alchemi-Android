package com.app.alchemi.roomdatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CardDetail(
    @PrimaryKey(autoGenerate = true)
    val id:Long=0,
    @ColumnInfo(name ="uid") val uid: String,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "card_number") val card_number: String?,
    @ColumnInfo(name = "expiry_date") val expiry_date: String?,
    @ColumnInfo(name = "card_type") val cardType: String?
)
