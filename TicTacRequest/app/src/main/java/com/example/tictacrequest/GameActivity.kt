package com.example.tictacrequest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
//import com.example.tictacrequest.GameHolder.Companion.player
import com.example.tictacrequest.databinding.ActivityGameBinding

//class GameHolder{
//    companion object{
//        var player: MutableList<String>?=null
//        var state: GameState ?= null
//    }
//}

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private lateinit var newGame: GameManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        val player = intent.getStringExtra("Player")
        val gameID = intent.getStringExtra("GameID")
        newGame = GameManager
        newGame.players = listOf(player.toString(), "")
        newGame.gameID = ""
        newGame.currentSymbol = "X"
        if (gameID.isNullOrBlank()){
            newGame.createNewGame()
            newGame.playerSymbol = "X"
            //window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            //window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            //binding.gameStatus.isVisible = true
        } else {
            newGame.joinGame(newGame.gameID!!)
            newGame.playerSymbol = "O"
        }
        newGame.onCreation = {
            newGame.state = it.state
            newGame.players = it.players
            newGame.gameID = it.gameId
            updateBoard()
        }
        binding.zerozero.setOnClickListener{changeGameState(0, binding.zerozero)}
        binding.zeroone.setOnClickListener{changeGameState(1, binding.zeroone)}
        binding.zerotwo.setOnClickListener{changeGameState(2, binding.zerotwo)}
        binding.onezero.setOnClickListener{changeGameState(3, binding.onezero)}
        binding.oneone.setOnClickListener{changeGameState(4, binding.oneone)}
        binding.onetwo.setOnClickListener{changeGameState(5, binding.onetwo)}
        binding.twozero.setOnClickListener{changeGameState(6, binding.twozero)}
        binding.twoone.setOnClickListener{changeGameState(7, binding.twoone)}
        binding.twotwo.setOnClickListener{changeGameState(8, binding.twotwo)}
        setContentView(binding.root)
    }

    fun changeGameState(pos:Int, button: Button){
        when(pos){
            0 -> {if (checkValidMove(button.text.toString())) {button.text = newGame.playerSymbol
                changeState(0,0)
                checkWinner(pos)
                flipTurn()}}
            1 -> {if (checkValidMove(button.text.toString())) {button.text = newGame.playerSymbol
                changeState(0,1)
                checkWinner(1)
                flipTurn()}}
            2 -> {if (checkValidMove(button.text.toString())) {button.text = newGame.playerSymbol
                changeState(0,2)
                checkWinner(2)
                flipTurn()}}
            3 -> {if (checkValidMove(button.text.toString())) {button.text = newGame.playerSymbol
                checkWinner(3)
                changeState(1,0)
                flipTurn()}}
            4 -> {if (checkValidMove(button.text.toString())) {button.text = newGame.playerSymbol
                changeState(1,1)
                // To change state just copy the current existing state but in each of these layers just specify
                // which position has to be changed
                checkWinner(4)
                flipTurn()}}
            5 -> {if (checkValidMove(button.text.toString())) {button.text = newGame.playerSymbol
                changeState(1,2)
                checkWinner(5)
                flipTurn()}}
            6 -> {if (checkValidMove(button.text.toString())) {button.text = newGame.playerSymbol
                changeState(2,0)
                checkWinner(6)
                flipTurn()}}
            7 -> {if (checkValidMove(button.text.toString())) {button.text = newGame.playerSymbol
                changeState(2,1)
                checkWinner(7)
                flipTurn()}}
            8 -> {if (checkValidMove(button.text.toString())) {button.text = newGame.playerSymbol
                changeState(2,2)
                checkWinner(8)
                flipTurn()}}
            else -> {println("Something went wrong")}
        }
    }

    fun changeState(row:Int, column:Int){
        var state = newGame.state?.toMutableList()
        var columnRepresented = ""
        var newState = mutableListOf<String>()
        println("Row: $row")
        println("Column $column")
        state?.get(row)?.forEach { columnRepresented += it }
        // (0,0) -> "X", "X", "X"
        columnRepresented = columnRepresented.substring(0, column) + newGame.playerSymbol + columnRepresented.substring(column+1)
        state?.removeAt(row)
        for (element in columnRepresented){
            newState.add(element.toString())
        }
        state?.add(row,newState)
        newGame.state = state
        println(newGame.state)
    }

    fun checkValidMove(buttonText:String): Boolean {
        return ((buttonText.isNotBlank()) or  (buttonText != "0") )
        //&& newGame.turnTracker
    }

    fun flipTurn(){
        //newGame.turnTracker = !newGame.turnTracker
        //println(newGame.turnTracker)
    }

    fun updateBoard(){
        var test = ""
        val list1 = newGame.state?.get(0)
        val list2 = newGame.state?.get(1)
        val list3 = newGame.state?.get(2)
        list1?.forEach{test += it}
        list2?.forEach{test += it}
        list3?.forEach{test += it}
        println(test)
        for (i in test.indices){
            changeSymbol(i, test[i].toString())
        }
    }

    fun changeSymbol(pos:Int, symbol: String){
        when(pos){
            0 -> {binding.zerozero.text = symbol}
            1 -> {binding.zeroone.text = symbol}
            2 -> {binding.zerotwo.text = symbol}
            3 -> {binding.onezero.text = symbol}
            4 -> {binding.oneone.text = symbol}
            5 -> {binding.onetwo.text = symbol}
            6 -> {binding.twozero.text = symbol}
            7 -> {binding.twoone.text = symbol}
            8 -> {binding.twotwo.text = symbol}
        }
    }

    fun checkWinner(positionCheck:Int){
        var row = 0
        if (positionCheck in 3..5) row = 1
        if (positionCheck in 6..8) row = 2
        if (newGame.moveCount > 3){
            if (positionCheck.rem(0) == 0){
                //Check column
                checkColumn(row)
                // Check row
                checkRow(row)
            }
            if (positionCheck.rem(1) == 0){
                checkColumn(row)
                checkRow(row)
                checkDiag()
            }
        }

        if (newGame.moveCount == 5){
            Toast.makeText(this, "The game is a draw", Toast.LENGTH_SHORT).show()
        }
    }

    fun checkColumn(pos:Int){
        for (i in 0..2){
            if (newGame.state?.get(i)?.get(pos) != newGame.currentSymbol){
                break
            }
            if (i == 2 && newGame.turnTracker){
                Toast.makeText(this, "You win!", Toast.LENGTH_SHORT).show()
                return
            }
            if (i == 2 && !newGame.turnTracker){
                Toast.makeText(this, "You lose..", Toast.LENGTH_SHORT).show()
                return
            }
        }
    }

    fun checkRow(pos:Int){
        for (i in 0..2){
            if (newGame.state?.get(pos)?.get(i) != newGame.currentSymbol){
                break
            }
            if (i == 2 && newGame.turnTracker){
                Toast.makeText(this, "You win!", Toast.LENGTH_SHORT).show()
                return
            }
            if (i == 2 && !newGame.turnTracker){
                Toast.makeText(this, "You lose..", Toast.LENGTH_SHORT).show()
                return
            }
        }
    }

    fun checkDiag(){
        for (i in 0..2){
            if (newGame.state?.get(i)?.get(i) != newGame.currentSymbol){
                return
            }
            if (i == 2 && newGame.turnTracker){
                Toast.makeText(this, "You win!", Toast.LENGTH_SHORT).show()
                return
            }
            if (i == 2 && !newGame.turnTracker){
                Toast.makeText(this, "You lose..", Toast.LENGTH_SHORT).show()
                return
            }
        }
        for (i in 0..2){
            if (newGame.state?.get(i)?.get(2 - i) != newGame.currentSymbol) {
                return
            }
            if (i == 2 && newGame.turnTracker){
                Toast.makeText(this, "You win!", Toast.LENGTH_SHORT).show()
                return
            }
            if (i == 2 && !newGame.turnTracker){
                Toast.makeText(this, "You lose..", Toast.LENGTH_SHORT).show()
                return
            }
        }
    }
}