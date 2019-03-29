package com.kristiania.madbakk.tictactoev3.model

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kristiania.madbakk.tictactoev3.R
import com.kristiania.madbakk.tictactoev3.controller.Player
import kotlinx.android.synthetic.main.lb_row.view.*

class Leaderboard_Adapter(val playerList: ArrayList<Player>): RecyclerView.Adapter<Leaderboard_ViewHolder>() {

    override fun getItemCount(): Int {
        return playerList.size
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Leaderboard_ViewHolder {

        val layoutInflater = LayoutInflater.from(p0.context)
        val cellRow = layoutInflater.inflate(R.layout.lb_row, p0, false)
        return Leaderboard_ViewHolder(cellRow)

    }

    override fun onBindViewHolder(p0: Leaderboard_ViewHolder, p1: Int) {
        val playerName = playerList[p1].name
        val playerScore = playerList[p1].wins
        p0.v.tv_name.text = playerName
        p0.v.tv_wins.text = playerScore.toString()
    }

}

class Leaderboard_ViewHolder(val v: View): RecyclerView.ViewHolder(v){
}