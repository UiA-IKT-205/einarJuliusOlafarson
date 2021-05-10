package com.example.tictacrequest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.tictacrequest.App.Companion.context
import com.example.tictacrequest.api.GameService
import com.example.tictacrequest.api.data.Game
import com.example.tictacrequest.api.data.GameState
import com.example.tictacrequest.databinding.ActivityMainBinding
import com.example.tictacrequest.dialogs.CreateGameDialog
import com.example.tictacrequest.dialogs.GameDialogListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), GameDialogListener {
    private lateinit var binding: ActivityMainBinding

    val TAG:String = "MainActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNewGameButton.setOnClickListener{
            newgameDialog()
        }
        joinGameButton.setOnClickListener{
        }
    }

    fun newgameDialog(){
        val dlg = CreateGameDialog()
        dlg.show(supportFragmentManager,"CreateGameDialogFragment")
    }

    override fun onDialogCreateGame(player: String) {
        Log.d(TAG,player)
        var user = player
//        GameHolder.player = mutableListOf<String>(user)
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("Player", player)
        intent.putExtra("GameID", "")
        startActivity(intent)
    }

    override fun onDialogJoinGame(player: String, gameId: String) {
        Log.d(TAG, "$player $gameId")
    }
}