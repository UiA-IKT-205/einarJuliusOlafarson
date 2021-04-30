package com.example.todolistproject.TodoListItem

import android.content.Context
import com.example.todolistproject.MasterList.ToDoMasterListDepositoryManager
import com.example.todolistproject.MasterList.toDoList

class ToDoItemListDepositoryManager {

    private lateinit var itemList: MutableList<ToDoItem>

    var onItem: ((List<ToDoItem>) -> Unit)? = null

    fun load(url: String, context: Context) {

        itemList =  mutableListOf<ToDoItem>()
        onItem?.invoke(itemList)
    }

    fun updateItems(newItems: MutableList<ToDoItem>){
        itemList = newItems
        onItem?.invoke(itemList)
    }

    fun addItem(newItem: ToDoItem, list: toDoList) {
        ToDoMasterListDepositoryManager.instance.addToItem(list.title, list.items, newItem)
        onItem?.invoke(itemList)
    }

    fun setBoolean(position: Int, listName: String){
        itemList[position].isDone = itemList[position].isDone.not()
        ToDoMasterListDepositoryManager.instance.setBoolean(position, listName, itemList, itemList[position].isDone)
        onItem?.invoke(itemList)
    }

    fun deleteItem(position:Int, listName:String) {
        ToDoMasterListDepositoryManager.instance.removeItem(position, listName,  itemList)
        itemList.removeAt(position)
        onItem?.invoke(itemList)
    }

    companion object{
        val instance = ToDoItemListDepositoryManager()
    }
}