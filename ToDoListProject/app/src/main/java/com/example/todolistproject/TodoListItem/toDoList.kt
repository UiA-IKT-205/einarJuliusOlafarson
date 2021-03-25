package com.example.todolistproject.TodoListItem

import com.example.todolistproject.TodoListItem.ToDoItem

data class toDoList(val title:String, val items:MutableList<ToDoItem>){

    // Updated my data class so it could set some bindings
    // Namely it now gives a completed amount of tasks for the binding
    // So it can update the progress bar

    fun returnLength(): Int{
        return items.size
    }

    fun GiveCompletedAmount(): Int {

        var amount:Int = 0

        for (i in items){
            if (i.isDone){
                amount += 1
            }
        }
        println("Amount completed in list $amount")
        if (amount > 0){
            amount = (amount/returnLength())*100
        }
        return amount
    }
}