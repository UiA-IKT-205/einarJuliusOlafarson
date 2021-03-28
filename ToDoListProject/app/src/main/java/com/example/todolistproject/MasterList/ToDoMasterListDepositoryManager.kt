package com.example.todolistproject.MasterList

import android.content.Context
import com.example.todolistproject.TodoListItem.ToDoItem

class ToDoMasterListDepositoryManager {

    private lateinit var MasterList: MutableList<toDoList>

    var onList: ((List<toDoList>) -> Unit)? = null
    var onListUpdate: ((list: toDoList)-> Unit)? = null

    fun load(url: String, context: Context) {

        MasterList =  mutableListOf(
            toDoList("Test", mutableListOf(
            ToDoItem("Cools", true))), toDoList("Tester1", mutableListOf())
        )

        onList?.invoke(MasterList)
    }

    fun updateList(masterList: MutableList<toDoList>){
        MasterList = masterList
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

    fun addToItem(listTitle: String, oldToDoList: MutableList<ToDoItem>, newItem: ToDoItem){
        MasterList.find{it.title == listTitle && it.items == oldToDoList}?.items?.add(newItem)
        onList?.invoke(MasterList)
    }

    fun removeItem(position: Int, listName: String,  list: MutableList<ToDoItem>){
        println(MasterList.find{it.title == listName && it.items == list}?.items)
        MasterList.find{it.title == listName && it.items == list}?.items?.removeAt(position)
        onList?.invoke(MasterList)
    }

    fun setBoolean(position: Int, listName: String, list: MutableList<ToDoItem>, newState: Boolean){
        println(newState)
        MasterList.find{it.title == listName && it.items == list}?.items?.get(position)?.isDone = newState
        onList?.invoke(MasterList)
    }

    companion object{
        val instance = ToDoMasterListDepositoryManager()
    }

}