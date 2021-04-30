package com.example.todolistproject.TodoListItem

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ToDoItem(val description:String, var isDone:Boolean): Parcelable {
}