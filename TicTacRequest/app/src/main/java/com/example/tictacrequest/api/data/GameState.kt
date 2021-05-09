package com.example.tictacrequest.api.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

typealias GameState = List<List<String>>

@Parcelize
data class Game(val players: MutableList<String>, val gameId:String, val state:GameState):
    Parcelable