package com.kristiania.madbakk.tictactoev3.model

import android.media.MediaPlayer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TableRow
import com.kristiania.madbakk.tictactoev3.R
import com.kristiania.madbakk.tictactoev3.controller.Game
import com.kristiania.madbakk.tictactoev3.controller.Player
import kotlinx.android.synthetic.main.fragment_board.*
import kotlin.concurrent.thread


class BoardFragment : Fragment() {
    private var count = 0
    private var onePlayer : Player = Player("Mads", -1)
    private var twoPlayer :Player = Player("TTTBot", -2)
    private var game = Game(true, "easy")
    private var gameIsOn = true
    private var mp: MediaPlayer? = null

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
            onePlayer = Player(p1, -1)
            game = Game(gameMode, "easy")
            if(gameMode){
                twoPlayer = Player("TTTBot", -2)
            }else{
                val p2 = playerInfo.intent.getStringExtra("playerTwo")
                twoPlayer = Player(p2, -2)
            }
        }

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
                    //btn.text = "O"
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

            tv_status.text = "${turn().name} won the game!"
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
        gameIsOn = false
        for(i in 0 ..game.getBoard().size -1){
            Log.i("Mads", "${game.getBoard()[i]}")
        }
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
                //btn.text =""
                btn.setImageResource(R.drawable.sbrplaceholder)
                btn.isClickable = true
            }
        }
        count = 0
        gameIsOn = true
        tv_status.text = "${onePlayer.name}' starts the game!"
        game.resetBoard()
    }


    override fun onPause() {
        super.onPause()
        mp?.stop()
    }

    override fun onStart() {
        super.onStart()

        mp = MediaPlayer.create(context, R.raw.cantinaband)
        mp?.start()
        mp?.setOnCompletionListener {
            mp?.start()
        }

    }

}