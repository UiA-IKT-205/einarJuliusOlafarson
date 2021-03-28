package com.example.todolistproject.MasterList

import com.example.todolistproject.TodoListItem.ToDoItem
import kotlin.math.ceil

data class toDoList(val title:String, var items:MutableList<ToDoItem>){

    // Updated my data class so it could set some bindings
    // Namely it now gives a completed amount of tasks for the binding
    // So it can update the progress bar

    fun returnLength(): Int{
        return items.size
    }

    fun GiveCompletedAmount(): Double {

        var amount:Double = 0.0
        for (i in items){
            if (i.isDone){
                amount += 1
            }
        }
        if (amount > 0){
            amount = (amount/returnLength())*100
        }
        return amount
    }

    fun returnAllItems(): MutableList<ToDoItem>{

        val listofItems = mutableListOf<ToDoItem>()

        for (i in items){
            listofItems.add(i)
        }
        return listofItems
    }
}