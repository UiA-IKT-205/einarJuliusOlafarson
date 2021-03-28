package com.example.todolistproject

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolistproject.TodoListItem.ToDoItem
import com.example.todolistproject.MasterList.ToDoMasterListAdapter
import com.example.todolistproject.MasterList.ToDoMasterListDepositoryManager
import com.example.todolistproject.MasterList.toDoList
import com.example.todolistproject.databinding.FragmentToListLayoutBinding
import kotlinx.android.synthetic.main.alert_box_create_list.*

// List holder so i can later create the relevant activity for my list
class ListHolder{
    companion object{
        var PickedList: toDoList? = null
        var Position: Int = 0
    }
}

class ToListLayout : Fragment() {

    private var _binding: FragmentToListLayoutBinding ?= null
    private val binding get() = _binding!!

    //Previous arrays have been moved to their own class

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentToListLayoutBinding.inflate(layoutInflater)
        val view = binding.root
        binding.masterlistListing.layoutManager = LinearLayoutManager(context)
        binding.masterlistListing.adapter = ToDoMasterListAdapter(emptyList<toDoList>(), this::onlistClicked)

        ToDoMasterListDepositoryManager.instance.onList = {
            (binding.masterlistListing.adapter as ToDoMasterListAdapter).updateList(it)
            println("Change registered")
        }

        // Loading the list in fragment
        context?.let { ToDoMasterListDepositoryManager.instance.load("test", it) }

        // Using the same alertdialog box as before
        // However I am now using the list methods as shown in the lecture
        // I also automatically dismiss the box now, it is commented out for testing purposes
        binding.addList.setOnClickListener {
            val listdialogView = LayoutInflater.from(context).inflate(R.layout.alert_box_create_list, null)
            val listBuilder = AlertDialog.Builder(context).setView(listdialogView)
            val listAlertDialog = listBuilder.show()

            listAlertDialog.dismissAlert.setOnClickListener{
                listAlertDialog.dismiss()
            }

            listAlertDialog.savenewElement.setOnClickListener{
                if(listAlertDialog.newElementName.text.toString().isNotEmpty()){
                    addList(listAlertDialog.newElementName.text.toString(), mutableListOf())
//                    listAlertDialog.dismiss()
                }
            }
        }
        return view
    }

    private fun addList(listName: String, emptyItems: MutableList<ToDoItem>) {
        val list = toDoList(listName, emptyItems)
        ToDoMasterListDepositoryManager.instance.addList(list)
    }


    private fun onlistClicked(masterlist: toDoList) {

        /*val intent =Intent(this, BookDetailsActivity::class.java).apply {
            putExtra(EXTRA_BOOK_INFO, book)
        }*/
        binding.AppSubtitle.text = masterlist.title
        ListHolder.PickedList = masterlist

        val intent = Intent(activity, OverViewList::class.java)

        startActivity(intent)

        //startActivityForResult(intent, REQUEST_BOOK_DETAILS)
    }
}
