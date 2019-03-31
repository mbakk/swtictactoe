package com.kristiania.madbakk.tictactoev3.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "player_table")
data class Player(

    @PrimaryKey
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "wins")
    var wins: Int,
    val id: Int
)