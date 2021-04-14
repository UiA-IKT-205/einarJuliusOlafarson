package com.example.todolistproject

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolistproject.databinding.OverViewListLayoutBinding
import com.example.todolistproject.MasterList.toDoList
import com.example.todolistproject.TodoListItem.ToDoItem
import com.example.todolistproject.TodoListItem.ToDoItemListAdapter
import com.example.todolistproject.TodoListItem.ToDoItemListDepositoryManager
import kotlinx.android.synthetic.main.alert_box_create_list.*


class OverViewList : AppCompatActivity() {

    private var binding: OverViewListLayoutBinding? = null
    private lateinit var toDoItem: toDoList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OverViewListLayoutBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        val recievedItems = ListHolder

        if(recievedItems != null){
            toDoItem = recievedItems.PickedList!!
            Log.i("Details view", toDoItem.toString())
        } else{
            finish()
        }

        binding!!.itemListListing.layoutManager = LinearLayoutManager(this)
        binding!!.itemListListing.adapter = ToDoItemListAdapter(emptyList<ToDoItem>())

        ToDoItemListDepositoryManager.instance.onItem = {
            (binding!!.itemListListing.adapter as ToDoItemListAdapter).updateItems(it)
            updateProgress(binding!!, toDoItem)
        }

        updateProgress(binding!!, toDoItem)
        binding!!.listName.text = toDoItem.title
        ToDoItemListDepositoryManager.instance.updateItems(toDoItem.items)

        binding!!.addItem.setOnClickListener {
            val listdialogView = LayoutInflater.from(this).inflate(R.layout.alert_box_create_list, null)
            val listBuilder = AlertDialog.Builder(this).setView(listdialogView)
            val listAlertDialog = listBuilder.show()
            listAlertDialog.NewElementorList.text = "Name your new Todo"

            listAlertDialog.dismissAlert.setOnClickListener{
                listAlertDialog.dismiss()
            }

            listAlertDialog.savenewElement.setOnClickListener{
                if(listAlertDialog.newElementName.text.toString().isNotEmpty()){
                    addNewItem(listAlertDialog.newElementName.text.toString())
                    updateProgress(binding!!, toDoItem)
                    listAlertDialog.dismiss()
                }
            }
        }
    }

    fun addNewItem(itemName: String){
        val newItem = ToDoItem(itemName, false)
        val safetyItem = ToDoItem(itemName, true)
        if (toDoItem.items.contains(newItem) or toDoItem.items.contains(safetyItem)){
            Toast.makeText(this,"Todo already exists", Toast.LENGTH_LONG).show()
        }else {
            ToDoItemListDepositoryManager.instance.addItem(newItem, toDoItem)
        }
    }

    fun updateProgress(binding: OverViewListLayoutBinding, list: toDoList){
        binding.listProgress.progress = list.GiveCompletedAmount().toInt()
    }

}