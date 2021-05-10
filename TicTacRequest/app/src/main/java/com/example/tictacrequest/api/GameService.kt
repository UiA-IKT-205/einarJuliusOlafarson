package com.example.tictacrequest.api

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.tictacrequest.App
import com.example.tictacrequest.R
import com.example.tictacrequest.api.data.Game
import com.example.tictacrequest.api.data.GameState
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

typealias GameServiceCallback = (state: Game?, errorCode:Int? ) -> Unit

object GameService {

    private val context = App.context
    var GameID: String? = null

    private val reqQue:RequestQueue = Volley.newRequestQueue(context)

    private fun createGameURL():String{
        return "%1s%2s%3s".format(context.getString(R.string.protocol), context.getString(R.string.domain),context.getString(R.string.base_path))
    }

    private fun joinGameURL():String{
        return "%1s%2s%3s%4s".format(context.getString(R.string.protocol), context.getString(R.string.domain),context.getString(R.string.base_path),
            context.getString(R.string.join_game_path).format(GameID))
    }

    private fun poolGameURL():String{
        return "%1s%2s%3s%4s".format(context.getString(R.string.protocol), context.getString(R.string.domain),context.getString(R.string.base_path),
            context.getString(R.string.pool_game_path).format(GameID))
    }

    private fun updateGameURL():String{
        return "%1s%2s%3s%4s".format(context.getString(R.string.protocol), context.getString(R.string.domain),context.getString(R.string.base_path),
            context.getString(R.string.update_game_path).format(GameID))
    }

    fun createGame(playerId:String, state:GameState, callback: GameServiceCallback) {
        val url = createGameURL()
        val requestData = JSONObject()
        requestData.put("player", playerId)
        requestData.put("state", JSONArray(state))
        println(requestData)

        val request = object : JsonObjectRequest(Request.Method.POST, url, requestData,
            {
                // Success game created.
                val game = Gson().fromJson(it.toString(0), Game::class.java)
                callback(game, null)
            }, {
                // Error creating new game.
                callback(null, it.networkResponse.statusCode)
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Game-Service-Key"] = context.getString(R.string.game_service_key)
                return headers
            }
        }
        reqQue.add(request)
    }

    fun joinGame(playerId:String, gameId:String, callback: GameServiceCallback){
        GameID = gameId
        val url = joinGameURL()
        val requestData = JSONObject()
        requestData.put("player", playerId)
        requestData.put("gameId", gameId)

        val request = object : JsonObjectRequest(Request.Method.POST, url, requestData,
            {
                // Success game joined.
                val game = Gson().fromJson(it.toString(0), Game::class.java)
                callback(game, null)
            }, {
                // Error joining game.
                callback(null, it.networkResponse.statusCode)
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Game-Service-Key"] = context.getString(R.string.game_service_key)
                return headers
            }
        }
        reqQue.add(request)
    }

    fun poolGame(gameId: String, callback: GameServiceCallback) {
        GameID = gameId
        val url = poolGameURL()

        val request = object : JsonObjectRequest(Request.Method.GET, url, null,
            {
                // Success game joined.
                val game = Gson().fromJson(it.toString(0), Game::class.java)
                callback(game, null)
            }, {
                // Error joining game.
                callback(null, it.networkResponse.statusCode)
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Game-Service-Key"] = context.getString(R.string.game_service_key)
                return headers
            }
        }
        reqQue.add(request)
    }

    fun updateGame(players: MutableList<String>, gameId: String, state: GameState, callback: GameServiceCallback){
        val url = updateGameURL()
        val requestData = JSONObject()
        requestData.put("players", players)
        requestData.put("gameId", gameId)
        requestData.put("state", JSONArray(state))

        val request = object : JsonObjectRequest(Request.Method.POST, url, requestData,
            {
                // Success game updated.
                val game = Gson().fromJson(it.toString(0), Game::class.java)
                callback(game, null)
            }, {
                // Error joining game.
                callback(null, it.networkResponse.statusCode)
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Game-Service-Key"] = context.getString(R.string.game_service_key)
                return headers
            }
        }
        reqQue.add(request)
    }
}