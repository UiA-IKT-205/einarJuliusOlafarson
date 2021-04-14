package com.example.todolistproject.MasterList

import android.content.Context
import com.example.todolistproject.TodoListItem.ToDoItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ToDoMasterListDepositoryManager {
    private val URL:String = "https://todolistproject-b4d36-default-rtdb.europe-west1.firebasedatabase.app/"
    private val USER:String = "Anonymous"
    private lateinit var MasterList: MutableList<toDoList>
    var onList: ((List<toDoList>) -> Unit)? = null

    // Database initialization
    val database = Firebase.database(URL)
    val ref = database.getReference("ToDo-MasterLists")

    fun load(url: String, context: Context) {
        // Commented out database initializer. I used this to make a reference point for how to grab things from the database
        // This is outdated now, but can be useful for future testing
//        MasterList =  mutableListOf(toDoList("awesome", mutableListOf(ToDoItem("Things", false), ToDoItem("Things", false))))
//        val newItem = ref.child(USER).child("ToDo - MasterLists")
//        MasterList.forEach{
//            newItem.child(it.title).setValue(it)
//        }
        MasterList = mutableListOf()
        readDataBaseContents()
        onList?.invoke(MasterList)
    }

    fun addList(masterlist: toDoList) {
        var listFound = false
        MasterList.forEach{
            if (it.title == masterlist.title) {
                listFound = true
            }
        }
        if (!listFound){
            MasterList.add(masterlist)
            val newItem = ref.child(USER).child("ToDo - MasterLists").child(masterlist.title)
            newItem.setValue(masterlist)
            onList?.invoke(MasterList)
        }
    }

    fun deleteList(position:Int) {
        val itemtoRemove = ref.child(USER).child("ToDo - MasterLists").child(MasterList[position].title)
        itemtoRemove.removeValue()
        MasterList.removeAt(position)
        onList?.invoke(MasterList)
    }

    fun addToItem(listTitle: String, oldToDoList: MutableList<ToDoItem>, newItem: ToDoItem){
        MasterList.find{it.title == listTitle && it.items == oldToDoList}?.items?.add(newItem)
        val newDatabaseItem = ref.child(USER).child("ToDo - MasterLists").child(listTitle).child("items").child(newItem.description)
        newDatabaseItem.setValue(newItem)
        onList?.invoke(MasterList)
    }

    fun removeItem(position: Int, itemName: String,  list: MutableList<ToDoItem>){
        var listName = MasterList.find{it.items == list}?.title
        val newDatabaseItem = ref.child(USER).child("ToDo - MasterLists").child(listName.toString()).child("items").child(itemName)
        newDatabaseItem.removeValue()
        onList?.invoke(MasterList)
    }

    fun setBoolean(position: Int, itemName: String, list: MutableList<ToDoItem>, newState: Boolean){
        var listName = MasterList.find{it.items == list}?.title
        val newDatabaseItem = ref.child(USER).child("ToDo - MasterLists").child(listName.toString()).child("items").child(itemName).child("done")
        newDatabaseItem.setValue(newState)
        onList?.invoke(MasterList)
    }


    fun readDataBaseContents(){

        ref.orderByKey().addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                // Im pretty sure this mess here is single handedly destroying my performance
                // But its all i know how to make through using some online guides
                // namely this one https://www.appsdeveloperblog.com/todo-list-app-kotlin-firebase/
                val lists = snapshot.child(USER).children.iterator()
                if(lists.hasNext()){
                    val toDoListindex = lists.next()
                    val map = toDoListindex.getValue() as HashMap<*, *>
                    var itemsIterator = toDoListindex.children.iterator()
                    while(itemsIterator.hasNext()){
                        val currentItem = itemsIterator.next()
                        val map = currentItem.getValue() as HashMap<*, *>
                        val tempToDoArray = mutableListOf<ToDoItem>()
                        if(currentItem.child("items").exists()){
                            var items = currentItem.child("items")
                            var todoIterator = items.children.iterator()
                            while(todoIterator.hasNext()){
                                val todoItem = todoIterator.next()
                                val todoMap = todoItem.getValue() as HashMap<*, *>
                                tempToDoArray.add(ToDoItem(todoMap.get("description") as String, todoMap.get("done") as Boolean))
                            }
                        }
                        MasterList.add(toDoList(map.get("title") as String, tempToDoArray))
                    }
                }
                onList?.invoke(MasterList)
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    companion object{
        val instance = ToDoMasterListDepositoryManager()
    }

}

