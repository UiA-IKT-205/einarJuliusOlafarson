package com.example.tictacrequest

import com.example.tictacrequest.api.GameService
import com.example.tictacrequest.api.data.Game
import com.example.tictacrequest.api.data.GameState
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

object GameManager {

    var player:String = ""
    var state:GameState? = null
    var gameID:String? = null
    var moveCount:Int = 0
    var gameOver:Boolean = false
    var turnTracker:Boolean = true
    var players = listOf<String>()
    var playerSymbol = ""
    var currentSymbol = ""
    var onCreation: ((Game) -> Unit)? = null
    val StartingGameState:GameState = listOf(listOf("0","0","0"),listOf("0","0","0"),listOf("0","0","0"))

    fun createNewGame(){
        GameService.createGame(players[0], StartingGameState) { game: Game?, err: Int? ->
            if(err != null){
                ///TODO("What is the error code? 406 you forgot something in the header. 500 the server di not like what you gave it")
            } else {
                if (game != null) {
                    onCreation?.invoke(game)
                    /// TODO("We have a game. What to do?)
                }
            }
        }
    }

    fun joinGame(gameId:String){
        val newGame: Game = Game(mutableListOf("skoster"), GameService.GameID.toString(), StartingGameState)
        GameService.joinGame(players[0], gameId) { game: Game?, err: Int? ->
            if(err != null){
                ///TODO("What is the error code? 406 you forgot something in the header. 500 the server di not like what you gave it")
            } else {
                if (game != null) {
                    onCreation?.invoke(game)
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
                    println(game.players)
                    println(game.gameId)
                    println(game.state)
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
                    println(game.players)
                    println(game.gameId)
                    println(game.state)
                }
            }
        }
    }
}