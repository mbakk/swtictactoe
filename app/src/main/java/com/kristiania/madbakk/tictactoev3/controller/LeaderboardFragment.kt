package com.kristiania.madbakk.tictactoev3.controller


import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.kristiania.madbakk.tictactoev3.R
import com.kristiania.madbakk.tictactoev3.model.Player
import com.kristiania.madbakk.tictactoev3.model.PlayerModel
import kotlinx.android.synthetic.main.fragment_leaderboard.*

class LeaderboardFragment : Fragment() {

    private lateinit var playermodel : PlayerModel
    private var playerList = ArrayList<Player>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leaderboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playermodel = PlayerModel(activity!!.application)

        // init()
        init()

        recycler_view_leaderboard.layoutManager = LinearLayoutManager(activity)

    }

    private fun init(){
        playermodel.allPlayers.observe(this, Observer { packageTypes ->
            packageTypes?.forEach{
                playerList.add(it)
            }
            recycler_view_leaderboard.adapter = Leaderboard_Adapter(playerList)
        })

    }

}
