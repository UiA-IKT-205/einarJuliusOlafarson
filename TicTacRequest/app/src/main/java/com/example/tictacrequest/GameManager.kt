package com.example.tictacrequest

import com.example.tictacrequest.api.GameService
import com.example.tictacrequest.api.data.Game
import com.example.tictacrequest.api.data.GameState

object GameManager {

    // All initial parameters for the game required for it to run
    var player:String = ""
    var state:GameState? = null
    var gameID:String? = null
    var moveCount:Int = 0
    var gameOver:Boolean = false
    var turnTracker:Boolean = true
    var players = mutableListOf<String>()
    var playerSymbol = "X"
    var opponentSymbol = "O"
    var currentSymbol = "X"

    // We use these like we did in the piano app to update the game on resolving a function
    // Primarily onUpdate is used to check if a new state has been found
    var onCreationNew: ((Game) -> Unit)? = null
    var onCreationJoin: ((Game) -> Unit)? = null
    var onUpdate: ((Game) -> Unit)? = null

    val StartingGameState:GameState = listOf(listOf("0","0","0"),listOf("0","0","0"),listOf("0","0","0"))

    fun createNewGame(){
        GameService.createGame(player, StartingGameState) { game: Game?, err: Int? ->
            if(err != null){
                ///TODO("What is the error code? 406 you forgot something in the header. 500 the server di not like what you gave it")
            } else {
                if (game != null) {
                    onCreationNew?.invoke(game)
                }
            }
        }
    }

    fun joinGame(gameId:String){
        val newGame: Game = Game(mutableListOf("skoster"), GameService.GameID.toString(), StartingGameState)
        GameService.joinGame(player, gameId) { game: Game?, err: Int? ->
            if(err != null){
                ///TODO("What is the error code? 406 you forgot something in the header. 500 the server di not like what you gave it")
            } else {
                if (game != null) {
                    onCreationJoin?.invoke(game)
                }
            }
        }
    }

    fun poolGame(gameId: String){
        GameService.poolGame(gameId){ game: Game?, err: Int? ->
            if(err != null){
                ///TODO("What is the error code? 406 you forgot something in the header. 500 the server did not like what you gave it")
            } else {
                if (game != null) {
                    onUpdate?.invoke(game)
                }
            }
        }
    }

    fun updateGame(players:MutableList<String>, gameId: String, state:GameState){
        GameService.updateGame(players, gameId, state){game: Game?, err:Int? ->
            if (err != null){
                ///TODO("What is the error code? 406 you forgot something in the header. 500 the server did not like what you gave it")
            }else {
                if (game != null){
                }
            }
        }
    }
}