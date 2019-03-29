package com.kristiania.madbakk.tictactoev3.controller


import android.arch.lifecycle.LiveData
import android.support.annotation.WorkerThread

class PlayerRepository(private val playerDao : PlayerDAO) {

    val getAllPlayerLive : LiveData<List<Player>> = playerDao.getAllPlayersLive()


    fun insert(player : Player){
        playerDao.insert(player)
    }

    fun update(wins : Int, name: String){
        playerDao.update(wins, name)
    }

    fun deleteAll(){
        playerDao.deleteAll()
    }
}