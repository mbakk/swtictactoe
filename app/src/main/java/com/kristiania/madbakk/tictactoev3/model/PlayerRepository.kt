package com.kristiania.madbakk.tictactoev3.model


import android.arch.lifecycle.LiveData

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