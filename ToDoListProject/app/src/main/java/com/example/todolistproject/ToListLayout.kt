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
import com.example.todolistproject.TodoListItem.ToDoMasterListAdapter
import com.example.todolistproject.TodoListItem.ToDoMasterListDepositoryManager
import com.example.todolistproject.TodoListItem.toDoList
import com.example.todolistproject.databinding.FragmentToListLayoutBinding
import kotlinx.android.synthetic.main.alert_box_create_list.*

// List holder so i can later create the relevant activity for my list
class ListHolder{
    companion object{
        var PickedList:toDoList? = null
    }
}

class ToListLayout : Fragment() {

    private var _binding: FragmentToListLayoutBinding ?= null
    private val binding get() = _binding!!

    //Previous arrays have been moved to their own class

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }

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
            print(it)
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

        ListHolder.PickedList = masterlist

        val intent = Intent(context, OverViewList::class.java)

        startActivity(intent)

        //startActivityForResult(intent, REQUEST_BOOK_DETAILS)
    }


    //Earlier work
    // Will be removed in the future
    // Currently being commited to have a version history with it
    // In case I ever want to fall back on this
//    fun findItemList(listName: String): MutableList<ToDoItem> {
//        todolistest.forEach{
//            if (it.title == listName){
//                return it.items
//            }
//        }
//        return mutableListOf()
//    }

    //    fun addnewList(listName:String, view:View){
//
//        // This first bit here is just to check for redundancy. I found
//        // No good method to itterate through the mutable list while also specifically
//        // Targeting the objects title outside of the forEach operation
//        // I want to optimize this in the future but this works for now
//        var repeatListing = false
//
//        todolistest.forEach{
//            if (listName == it.title){
//                repeatListing = true
//            }
//        }
//
//        // We inform the user that their list already exists
//        // I use a tag based system for removing fragments and as such I can't support duplicates
//        // Would love to explore that possibility in the future and how i would add
//        // an identifier to allow duplicates, probably just tags but with extra steps
//        if (repeatListing){
//            Toast.makeText(context, "List already exists", Toast.LENGTH_SHORT).show()
//        } else{
//            // Because there is no overlap in lists we can add a new one to our list
//            // We declare an empty ToDoItem list for our new list
//            val todoitemsList:MutableList<ToDoItem> = mutableListOf()
//            // We add the list to our existing array
//            todolistest.add(toDoList(listName, todoitemsList))
//            // This is just to keep an eye on the array data and make sure there are no errors
//            print(todolistest)
//            /*
//            So here is where i am not quite sure what to do
//            As far as android studio seems concerned I need to keep instantiating the
//            fragmentmanager and fragmentTransaction every time i want to make a new fragment action
//            This was not needed in our Piano project, but if i don't do this then android studio
//            throws a bunch of errors in my face, so this is how its staying for now
//             */
//            val fragmentmanager = childFragmentManager
//            var fragmentTransaction = fragmentmanager.beginTransaction()
//            val newTodoList = ToDoMasterList.newInstance(0, listName)
//
//            /* Here I define that on the keyUp of our deleteList button I instantiate
//            the managers,transactions again and have them search for the fragment that was
//            pressed on by its tag which is delivered as its name.
//
//            I then remove it both as a fragment and from our list with a forEach search
//            */
//
//            /*
//            * Todo add a dialogue asking if the user is sure they want to delete their list.
//            * */
//            newTodoList.onKeyUp =  {listname ->
//                println(listname)
//                val fragmentmanager = childFragmentManager
//                val fragmentTransaction = fragmentmanager.beginTransaction()
//                val fragment = childFragmentManager.findFragmentByTag(listname)
//                if (fragment!= null) {
//                    println(fragment.tag)
//                    fragmentTransaction.remove(fragment).commit()
//                    todolistest.forEach{
//                        if (listName == it.title){
//                            todolistest.remove(it)
//                        }
//                    }
//                    println(todolistest)
//                }
//            }
//
//            newTodoList.onListPress = {listname ->
//                val itemsforView = findItemList(listname)
//                println(itemsforView)
//            }
//
//            /*
//            Moving back to the primary code of this section, this finally adds the list
//            to our primary view and everything just works out pretty nicely now.
//            I'm concerned about performance as I am fairly sure I could represent this better,
//            however I wanted to follow the visuals of the project somewhat strictly
//            and this feels like a pretty good approximation for creating it
//            even if it potentially might not be optimal.
//             */
//            fragmentTransaction.add(view.LinearLayoutForMasterList.id, newTodoList, listName)
//            fragmentTransaction.commit()
//
//        }
//
//    }

}
