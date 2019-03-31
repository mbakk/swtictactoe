package com.kristiania.madbakk.tictactoev3.model

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface PlayerDAO{

    @Insert
    fun insert(name : Player)

    @Delete
    fun delete(name : Player)

    @Query("UPDATE player_table SET wins=:newWin WHERE name=:playerName")
    fun update(newWin: Int, playerName: String)

    @Query("DELETE FROM player_table")
    fun deleteAll()

    @Query("SELECT * FROM player_table ORDER BY wins DESC")
    fun getAllPlayersLive() : LiveData<List<Player>>



}