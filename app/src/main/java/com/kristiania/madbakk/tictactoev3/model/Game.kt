package com.kristiania.madbakk.tictactoev3.model

class Game(onePlayer: Boolean) {

    private var board = arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)

    val isOnePlayer = onePlayer

    fun move(index: Int, player: Player): Boolean{
        return if(board[index] == -1 || board[index] == -2){
            false
        }else {
            board[index] = player.id
            true
        }
    }


    fun aiMove(): Int{

        var bestVal = -1000
        var bestMove = 0

        for(i in 0.. board.size -1){

            if(board[i] != -1 && board[i] != -2){
                var tempvalue = board[i]
                board[i] = -2
                var moveValue = minimax(board, -1)

                board[i] = tempvalue

                if(moveValue > bestVal){
                    bestMove = i
                    bestVal = moveValue
                }
            }
        }
        board[bestMove] = -2
        return bestMove
    }

    private fun minimax(currentBoard: Array<Int>, playerID: Int): Int {

        var score : Int

        if(checkWin(currentBoard, -1)){
            score = - 10
            return score

        }
        if(checkWin(currentBoard, -2)){
            score = 10
            return score
        }
        if(isTie(currentBoard)){
            return 0
        }

        if(playerID == -2){
            var best = -1000

            for(i in 0 .. currentBoard.size -1){
                if(currentBoard[i] != -1 && currentBoard[i] != -2) {
                    var tempCell = currentBoard[i]
                    currentBoard[i] = -2
                    val value = minimax(currentBoard, -1)

                    //get highest value
                    if( value > best){
                        best = value
                    }
                    //undo move
                    currentBoard[i] = tempCell
                }
            }
            return best
        }
        else{
            var best = 1000

            for(i in 0 ..currentBoard.size -1){
                if(currentBoard[i] != -1 && currentBoard[i] != -2){
                    var tempCell= currentBoard[i]
                    currentBoard[i] = -1
                    val value = minimax(currentBoard, -2)

                    //get lowest value
                    if(value < best){
                        best = value
                    }
                    //undo move
                    currentBoard[i] = tempCell
                }
            }
            return best
        }
    }

    fun checkWin(board: Array<Int>, playerId: Int): Boolean {

        //All winning combinations
        val win1: IntArray = intArrayOf(0, 1, 2)
        val win2: IntArray = intArrayOf(3, 4, 5)
        val win3: IntArray = intArrayOf(6, 7, 8)
        val win4: IntArray = intArrayOf(0, 3, 6)
        val win5: IntArray = intArrayOf(1, 4, 7)
        val win6: IntArray = intArrayOf(2, 5, 8)
        val win7: IntArray = intArrayOf(2, 4, 6)
        val win8: IntArray = intArrayOf(0, 4, 8)

        val winCombos: Array<IntArray> = arrayOf(win1, win2, win3, win4, win5, win6, win7, win8)

        for (i in 0..winCombos.size - 1) {
            val combo = winCombos[i]
            if (board[combo[0]] == playerId && board[combo[1]] == playerId
                && board[combo[2]] == playerId
            ) {
                return true
            }
        }
        return false
    }

    fun isTie(board: Array<Int>): Boolean{
        if(findEmptyCell(board).size == 0){
            return true
        }
        return false
    }

    private fun findEmptyCell(tempboard: Array<Int>): ArrayList<Int> {
        var emptyCells = arrayListOf<Int>()
        for (i in 0..tempboard.size - 1) {
            if (tempboard[i] != -1 && tempboard[i] != -2) {
                emptyCells.add(tempboard[i])
            }
        }
        return emptyCells
    }

    fun getBoard(): Array<Int>{
        return board
    }

    fun resetBoard(){
        board = arrayOf(0,0,0,0,0,0,0,0,0)
    }

    fun setBoard(savedBoard: Array<Int>){
        board = savedBoard
    }
}