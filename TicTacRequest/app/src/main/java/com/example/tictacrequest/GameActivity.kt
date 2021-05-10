package com.example.tictacrequest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.tictacrequest.databinding.ActivityGameBinding
import kotlinx.coroutines.*

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
        newGame = GameManager
        val player = intent.getStringExtra("Player")
        val gameID = intent.getStringExtra("GameID")
        newGame.player = player!!
        newGame.gameID = gameID
        // Since creating a game does not generate an ID until after the activity is made
        // We can assume that if the gameID is null or blank we can just create a new game
        // But if the user did input a gameID we assume they wish to join a game
        if (gameID.isNullOrBlank()){
            newGame.createNewGame()
        } else {
            newGame.joinGame(gameID)
        }

        // Originally joining and creating was together but they now invoke separate setup functions
        // OnCreationNew makes you the first player with X and sets the state accordingly
        // Little setup is required overall
        // Because of how the game is polled however, until the player makes the first move
        // They will not see the second player, as such the status message reflects that
        newGame.onCreationNew = {
            resetGame()
            newGame.playerSymbol = "X"
            newGame.currentSymbol = newGame.playerSymbol
            newGame.opponentSymbol = "O"
            newGame.players = it.players
            newGame.gameID = it.gameId
            binding.player1.text = newGame.players[0]
            newGame.state = it.state
            binding.gameStatus.text = getString(R.string.first_move)
            binding.gameId.text = newGame.gameID
            updateBoard()
        }

        // Here we join a game, setting ourselves as player 2 with O as our symbol
        // We also make it so its not our turn and use the default state to set the local state
        // This way we can start with game polling right away and just wait for the first player
        // to start the game proper
        newGame.onCreationJoin = {
            resetGame()
            newGame.playerSymbol = "O"
            newGame.opponentSymbol = "X"
            newGame.currentSymbol = newGame.opponentSymbol
            newGame.players = it.players
            newGame.gameID = it.gameId
            binding.player1.text = newGame.players[0]
            binding.player2.text = newGame.players[1]
            newGame.state = newGame.StartingGameState
            flipTurn()
            updateBoard()
        }

        // Here we check if the game has changed since the last time we polled the game
        // It updates our text to reflect the players in the game
        // Afterwards we set the text to be our turn before checking winner as winning also changes text
        // If we lose the text gets updated fast enough you should not notice it
        // Otherwise, we compare the old state to the new one and check if the opponent has won
        // If they have not we update the board and set the turn to be ours
        newGame.onUpdate = {
            newGame.players = it.players
            binding.player1.text = newGame.players[0]
            if (newGame.players.size > 1) {binding.player2.text = newGame.players[1]}
            if (newGame.state != it.state) {
                binding.gameStatus.text = getString(R.string.your_turn)
                newGame.moveCount++
                val oldState = newGame.state
                newGame.state = it.state
                val positionDifference = findStateDifference(oldState!!, it.state)
                checkWinner(positionDifference[0], positionDifference[1])
                updateBoard()
                newGame.currentSymbol = newGame.playerSymbol
                newGame.turnTracker = true
            }
        }

        binding.zerozero.setOnClickListener{changeGameState(0, 0, binding.zerozero)}
        binding.zeroone.setOnClickListener{changeGameState(0, 1, binding.zeroone)}
        binding.zerotwo.setOnClickListener{changeGameState(0, 2, binding.zerotwo)}
        binding.onezero.setOnClickListener{changeGameState(1,0, binding.onezero)}
        binding.oneone.setOnClickListener{changeGameState(1,1, binding.oneone)}
        binding.onetwo.setOnClickListener{changeGameState(1,2, binding.onetwo)}
        binding.twozero.setOnClickListener{changeGameState(2,0, binding.twozero)}
        binding.twoone.setOnClickListener{changeGameState(2,1, binding.twoone)}
        binding.twotwo.setOnClickListener{changeGameState(2,2, binding.twotwo)}
        setContentView(binding.root)
    }

    fun changeGameState(row:Int, column: Int, button: Button){
        // Here we make all the necessary checks and updates for when we make a move
        if (checkValidMove(button.text.toString())){
            // We set the text of the button to our symbol
            button.text = newGame.playerSymbol
            // We change the local state of our game by converting it to a string
            changeState(row,column)
            // We check if we have won by making said move
            checkWinner(row, column)
            // Otherwise, its our opponents turn and we need to poll the game
            flipTurn()
        } else {return}
    }

    fun changeState(row:Int, column:Int){
        newGame.moveCount++
        val state = newGame.state?.toMutableList()
        var columnRepresented = ""
        newGame.state?.get(row)?.forEach{columnRepresented+=it}
        val newState = mutableListOf<String>()
        columnRepresented = columnRepresented.substring(0, column) + newGame.playerSymbol + columnRepresented.substring(column+1)
        state?.removeAt(row)
        for (element in columnRepresented){
            newState.add(element.toString())
        }
        state?.add(row,newState)
        newGame.state = state
        newGame.updateGame(newGame.players, newGame.gameID.toString(), newGame.state!!)
    }

    fun checkValidMove(buttonText:String): Boolean {
        // Here we check that we have valid moves
        // It has to be our turn, it has to be an unused space and the game has to not be over
        return ((newGame.turnTracker) and (buttonText == "0") and (!newGame.gameOver))
    }

    fun flipTurn(){
        newGame.turnTracker = false
        binding.gameStatus.text = getString(R.string.waiting_for)
        if (!newGame.turnTracker && !newGame.gameOver){
            newGame.currentSymbol = newGame.opponentSymbol
            lifecycleScope.launch(Dispatchers.Default) {
                pollGame()
            }
        }
    }

    /*
    BOARD UPDATE

    Here we update the board visually on setup and on receiving poll updates
    Broken into updateBoard, returnStringRepresentation and changeSymbol

    returnStringRepresentation -> gets the current state, converts it to a string to itterate through
    changeSymbol -> accesses the buttons based on a switch statement and sets their text
    // updateBoard combines these two to work together

    */


    fun updateBoard(){
        val boardString = newGame.state?.let { returnStringRepresentation(it) }.toString()
        for (i in boardString.indices){
            changeSymbol(i, boardString[i].toString())
        }
    }

    private fun returnStringRepresentation(something: List<List<String>>): String {
        // We break up the states into a string to loop through
        var stateString = ""
        val list1 = something.get(0)
        val list2 = something.get(1)
        val list3 = something.get(2)
        list1.forEach{stateString += it}
        list2.forEach{stateString += it}
        list3.forEach{stateString += it}
        return stateString
    }

    private fun changeSymbol(pos:Int, symbol: String){
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

    /*
    *                                       CHECK WINNER
    *
    * Here we do a number of things but namely after every move update we check if there is a winner
    * We give the function the row and the column that the button was pressed
    * Then if the column is on an even number (0, 2) we check the row, column and diag for a win
    * otherwise if it is just the middle top, bottom and side buttons we only have to check the
    * column and row for a win
    * */


    fun checkWinner(row:Int, column:Int){
        if ((row == -1) || (column == -1)) return
        if (newGame.moveCount >= 0){
            if ((column % 2) == 0){
                checkColumn(column)
                checkRow(row)
                checkDiag()
            } else {
                checkColumn(row)
                checkRow(row)
            }
            if (newGame.moveCount == 9 && !newGame.gameOver){
                Toast.makeText(this, "The game is a draw", Toast.LENGTH_SHORT).show()
                newGame.gameOver = true
            }
        }

    }

    fun checkColumn(pos:Int){
        for (i in 0..2){
            if (newGame.state?.get(i)?.get(pos).toString() != newGame.currentSymbol){
                break
            }
            if (i == 2 && newGame.turnTracker){
                newGame.gameOver = true
                Toast.makeText(this, "You win!", Toast.LENGTH_SHORT).show()
            }
            if (i == 2 && !newGame.turnTracker){
                newGame.gameOver = true
                Toast.makeText(this, "You lose..", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun checkRow(pos:Int){
        for (i in 0..2){
            if (newGame.state?.get(pos)?.get(i).toString() != newGame.currentSymbol){
                break
            }
            if (i == 2 && newGame.turnTracker){
                newGame.gameOver = true
                Toast.makeText(this, "You win!", Toast.LENGTH_SHORT).show()

            }
            if (i == 2 && !newGame.turnTracker){
                newGame.gameOver = true
                Toast.makeText(this, "You lose..", Toast.LENGTH_SHORT).show()

            }
        }
    }

    fun checkDiag(){
        for (i in 0..2){
            if (newGame.state?.get(i)?.get(i) != newGame.currentSymbol){
                break
            }
            if (i == 2 && newGame.turnTracker){
                newGame.gameOver = true
                Toast.makeText(this, "You win!", Toast.LENGTH_SHORT).show()

            }
            if (i == 2 && !newGame.turnTracker){
                newGame.gameOver = true
                Toast.makeText(this, "You lose..", Toast.LENGTH_SHORT).show()
            }
        }
        for (i in 0..2){
            if (newGame.state?.get(i)?.get(2 - i) != newGame.currentSymbol) {
                break
            }
            if (i == 2 && newGame.turnTracker){
                newGame.gameOver = true
                Toast.makeText(this, "You win!", Toast.LENGTH_SHORT).show()

            }
            if (i == 2 && !newGame.turnTracker){
                newGame.gameOver = true
                Toast.makeText(this, "You lose..", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /*
    * Here we check the difference between our current state and the state made by our oppoent
    * Because tic tac toe only makes changes by 1 move we can convert our states to strings
    * then iterate through them to find the difference between them giving us the column and
    * row that needs to be checked for a win.
    */

    fun findStateDifference(oldState: List<List<String>>, newState: List<List<String>>): List<Int> {
        val board1:String = returnStringRepresentation(oldState)
        val board2:String = returnStringRepresentation(newState)
        for (i in board1.indices){
            if (board1[i] != board2[i]){
                when (i){
                    0 -> {return listOf<Int>(0,0)}
                    1 -> {return listOf<Int>(0,1)}
                    2 -> {return listOf<Int>(0,2)}
                    3 -> {return listOf<Int>(1,0)}
                    4 -> {return listOf<Int>(1,1)}
                    5 -> {return listOf<Int>(1,2)}
                    6 -> {return listOf<Int>(2,0)}
                    7 -> {return listOf<Int>(2,1)}
                    8 -> {return listOf<Int>(2,2)}
                }
            }
        }
        return listOf(-1,-1)
    }


    // Here we poll the game every 5 seconds
    // This gets updated through a invocation in the newGame object
    // If there is a change between our local state and delivered state
    // We update our turnTracker to be our turn, true
    // If it is not changed it will keep creating itself to pool the game again
    // Otherwise it will still and terminate all other poll games
    // The last bit is added in case the game ends and the player very quickly creates a new
    // game, as we can then end all polling by incident and not cause problems

    suspend fun pollGame(){
        delay(5000)
        // So long as it is not our turn we continually check for an updated gamestate
        // Else we set it to being our turn and end all gamepolling
        if (!newGame.turnTracker && !newGame.gameOver){
            newGame.poolGame(newGame.gameID!!)
            pollGame()
        } else {
            /* Here we make sure to cancel all pollGames as to not accidentally leave hanging coroutines*/
                lifecycleScope.coroutineContext.cancelChildren()
        }
    }

    // Resetting the game object so we can make more games in the future
    // Otherwise as a singleton the newGame object would carry over important variables from previous games
    fun resetGame(){
        newGame.playerSymbol = ""
        newGame.currentSymbol = ""
        newGame.opponentSymbol = ""
        newGame.state = null
        newGame.turnTracker = true
        newGame.moveCount = 0
        newGame.gameOver = false
    }
}