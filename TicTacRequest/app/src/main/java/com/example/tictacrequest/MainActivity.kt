package com.example.tictacrequest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tictacrequest.api.GameService
import com.example.tictacrequest.api.data.Game
import com.example.tictacrequest.api.data.GameState
import com.example.tictacrequest.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        val newGame: Game = Game(mutableListOf("skost"), GameService.GameID.toString(), listOf(listOf(0,1,0),listOf(0,0,0),listOf(0,0,0)))
        testVolley.setOnClickListener{
            createNewGame()
            //createdNewGame()
        }
        testVolley2.setOnClickListener{
            var gameID = putGameId.text.toString()
            poolGame(gameID)
            //createdNewGame()
        }

        testvolley3.setOnClickListener{
            var gameID = putGameId.text.toString()
            updateGame(newGame.players, gameID, newGame.state)
        }
    }

    fun createNewGame(){
        val newGame: Game = Game(mutableListOf("skost"), GameService.GameID.toString(), listOf(listOf(0,0,0),listOf(0,0,0),listOf(0,0,0)))
        GameService.createGame(newGame.players[0], newGame.state) { game: Game?, err: Int? ->
            if(err != null){
                ///TODO("What is the error code? 406 you forgot something in the header. 500 the server di not like what you gave it")
            } else {
                if (game != null) {
                    println(game.gameId)
                    println(game.players)
                    println(game.state)
                /// TODO("We have a game. What to do?)
                }
            }
        }
    }

    fun joinGame(gameId:String){
        val newGame: Game = Game(mutableListOf("skoster"), GameService.GameID.toString(), listOf(listOf(0,0,0),listOf(0,0,0),listOf(0,0,0)))
        GameService.joinGame(newGame.players[0], gameId) { game: Game?, err: Int? ->
            if(err != null){
                ///TODO("What is the error code? 406 you forgot something in the header. 500 the server di not like what you gave it")
            } else {
                if (game != null) {
                    println(game.players)
                    println(game.gameId)
                    println(game.state)
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