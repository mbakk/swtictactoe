package com.kristiania.madbakk.tictactoev3.controller

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class PlayerModel(application: Application) : AndroidViewModel(application) {
    private val  repository: PlayerRepository
    val allPlayers: LiveData<List<Player>>
    //co-routine
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    init {
        val playerDao = PlayerRoomDatabase.getDatabase(application.applicationContext)
            .playerDao()
        repository = PlayerRepository(playerDao)
        allPlayers = repository.getAllPlayerLive
    }


    fun insert(player: Player) = scope.launch(Dispatchers.IO) {
        repository.insert(player)
    }

    fun update(wins: Int, playerName: String) = scope.launch(Dispatchers.IO) {
        repository.update(wins, playerName)
    }

    fun delete() = scope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }
}