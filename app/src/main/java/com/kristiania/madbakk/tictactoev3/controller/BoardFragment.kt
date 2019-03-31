package com.kristiania.madbakk.tictactoev3.controller

import android.arch.lifecycle.Observer
import android.media.MediaPlayer
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TableRow
import com.kristiania.madbakk.tictactoev3.R
import com.kristiania.madbakk.tictactoev3.model.Game
import com.kristiania.madbakk.tictactoev3.model.Player
import com.kristiania.madbakk.tictactoev3.model.PlayerModel
import kotlinx.android.synthetic.main.fragment_board.*
import kotlin.concurrent.thread


class BoardFragment : Fragment() {
    private var count = 0
    private lateinit var onePlayer :Player
    private lateinit var twoPlayer :Player
    private lateinit var game :Game
    private var gameIsOn = true
    private lateinit var  mp: MediaPlayer
    private lateinit var playermodel : PlayerModel
    private var playerList = arrayListOf<Player>()
    private var savedState: Bundle? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_board, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val playerInfo = activity
        if(playerInfo?.intent != null){
            val gameMode = playerInfo.intent.getBooleanExtra("isOnePlayer", true)
                val p1 = playerInfo.intent.getStringExtra("playerOne")
            onePlayer = Player(p1, 0, -1)
            game = Game(gameMode)
            if(gameMode){
                twoPlayer = Player("TTTBot", 0, -2)
            }else{
                val p2 = playerInfo.intent.getStringExtra("playerTwo")
                twoPlayer = Player(p2, 0, -2)
            }
        }

        chronometer.base = SystemClock.elapsedRealtime()

        tv_status.text = "${onePlayer.name}' starts the game!"

        btn_restart.setOnClickListener {
            resetGame()
        }

        btn_lb.setOnClickListener {
            val manager = fragmentManager
            val ft = manager!!.beginTransaction()
            ft.replace(R.id.fragment_container,LeaderboardFragment())
            ft.addToBackStack(null)
            ft.commit()
        }

        for (i in 0..tb_layout.childCount - 1) {
            val row = tb_layout.getChildAt(i) as TableRow
            for (j in 0..row.childCount - 1) {
                val btn = row.getChildAt(j) as ImageButton
                btn.setOnClickListener {
                    val cellId = btn.tag.toString().toInt()
                    playerMove(cellId, btn)
                }
            }
        }

        playermodel = PlayerModel(activity!!.application)
        playermodel.allPlayers.observe(this, Observer { packageTypes ->
            packageTypes?.forEach{
                playerList.add(it)
            }
        })

    }


    private fun playerMove(cellId: Int, btn: ImageButton){
        if(game.move(cellId, turn())){
            if(turn() == onePlayer){
                btn.setImageResource(R.drawable.obiwan)
            }else{
                btn.setImageResource(R.drawable.darthvader)
            }
            winOrDraw()
            count++
            if(game.isOnePlayer && gameIsOn){
                aiMove()
            }
            if(gameIsOn){
                tv_status.text = "${turn().name}'s turn!"
            }

        }
    }

    private fun aiMove(){
        var move = -1
        thread {
            move = game.aiMove()
        }.join()

        for (i in 0..tb_layout.childCount - 1) {
            val row = tb_layout.getChildAt(i) as TableRow
            for (j in 0..row.childCount - 1) {
                val btn = row.getChildAt(j) as ImageButton
                if(move == btn.tag.toString().toInt()){
                    btn.setImageResource(R.drawable.darthvader)
                }
            }
        }
        winOrDraw()
        count++
    }

    private fun turn(): Player{
        if(count % 2 == 0){
            return onePlayer
        }
        return twoPlayer
    }

    private fun gameOver(wOd: String){
        if(wOd == "win"){
            var winner = turn()
            var foundwinner = false
            tv_status.text = "${turn().name} won the game!"
            playerList.forEach{
                if(it.name == winner.name){
                    var wins = it.wins
                    wins++
                    playermodel.update(wins, it.name)
                    foundwinner = true
                }
            }
            if(!foundwinner){
                winner.wins++
                playermodel.insert(winner)
            }

        }else if(wOd == "draw"){
            tv_status.text = "It's a draw!"
        }
        for (i in 0..tb_layout.childCount - 1){
            val row = tb_layout.getChildAt(i) as TableRow
            for (j in 0..row.childCount - 1) {
                val btn = row.getChildAt(j) as ImageButton
                btn.isClickable = false
            }
        }
        chronometer.stop()
        gameIsOn = false
        btn_restart.text = getString(R.string.start_button)
    }

    private fun winOrDraw(){
        if(game.checkWin(game.getBoard(),turn().id)){
            gameOver("win")
        }else if(game.isTie(game.getBoard())){
            gameOver("draw")
        }
    }

    private fun resetGame(){

        for (i in 0..tb_layout.childCount - 1) {
            val row = tb_layout.getChildAt(i) as TableRow
            for (j in 0..row.childCount - 1) {
                val btn = row.getChildAt(j) as ImageButton
                btn.setImageResource(R.drawable.sbrplaceholder)
                btn.isClickable = true
            }
        }
        count = 0
        btn_restart.text = getString(R.string.start_button_restart)
        gameIsOn = true
        tv_status.text = "${onePlayer.name}' starts the game!"
        game.resetBoard()
        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.start()

    }


    override fun onStop() {
        super.onStop()
        mp.stop()
        chronometer.stop()

        savedState = Bundle()
        savedState?.putInt("mp", mp.currentPosition)
        savedState?.putLong("timer-offset", chronometer.base - SystemClock.elapsedRealtime())
        savedState?.putBoolean("gameState", gameIsOn)
        savedState?.putInt("count", count)
        savedState?.putIntArray("boardState", game.getBoard().toIntArray())
        savedState?.putString("turn", tv_status.text.toString())
    }

    override fun onStart() {
        super.onStart()
        chronometer.start()
        mp = MediaPlayer.create(context, R.raw.cantinaband)

        if(savedState != null){
            tv_status.text = savedState!!.getString("turn")
            count = savedState!!.getInt("count")
            mp.seekTo(savedState!!.getInt("mp"))
            chronometer.base = SystemClock.elapsedRealtime() + savedState!!.getLong("timer-offset")
            gameIsOn = savedState!!.getBoolean("gameState")
            game.setBoard(savedState?.getIntArray("boardState")!!.toTypedArray())
            for   (i in 0..tb_layout.childCount - 1) {
                val row = tb_layout.getChildAt(i) as TableRow
                for (j in 0..row.childCount - 1) {
                    val btn = row.getChildAt(j) as ImageButton
                    if(!gameIsOn){
                        btn.isClickable = false
                    }
                    var currentBoard = game.getBoard()
                    if(currentBoard[btn.tag.toString().toInt()] == -1){
                        btn.setImageResource(R.drawable.obiwan)
                    }else if(currentBoard[btn.tag.toString().toInt()] == -2){
                        btn.setImageResource(R.drawable.darthvader)
                    }
                }
            }
            savedState = null
        }
        mp.start()
        mp.setOnCompletionListener {
            mp.start()
        }
    }

}
