package com.example.todolistproject.TodoListItem

import android.content.Context
import com.example.todolistproject.TodoListItem.ToDoItem
import com.example.todolistproject.TodoListItem.toDoList

class ToDoMasterListDepositoryManager {

    private lateinit var MasterList: MutableList<toDoList>

    var onList: ((List<toDoList>) -> Unit)? = null
    var onListUpdate: ((list: toDoList)-> Unit)? = null

    fun load(url: String, context: Context) {

        MasterList =  mutableListOf(toDoList("Test", mutableListOf(
            ToDoItem("Cools", false))), toDoList("Tester1", mutableListOf()))

        onList?.invoke(MasterList)
    }

    fun updateList(masterList: toDoList){
        MasterList.add(masterList)
        onList?.invoke(MasterList)
    }


    fun addList(masterlist: toDoList) {
        MasterList.add(masterlist)
        onList?.invoke(MasterList)
    }

    fun deleteList(position:Int) {
        MasterList.removeAt(position)
        onList?.invoke(MasterList)
    }

    companion object{
        val instance = ToDoMasterListDepositoryManager()
    }

}