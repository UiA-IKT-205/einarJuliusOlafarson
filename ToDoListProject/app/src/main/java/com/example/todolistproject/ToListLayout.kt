package com.example.todolistproject

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AlertDialogLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.todolistproject.TodoListItem.ToDoItem
import com.example.todolistproject.TodoListItem.toDoList
import com.example.todolistproject.databinding.AlertBoxCreateListBinding
import com.example.todolistproject.databinding.FragmentToListLayoutBinding
import kotlinx.android.synthetic.main.alert_box_create_list.*
import kotlinx.android.synthetic.main.fragment_to_do_master_list.*
import kotlinx.android.synthetic.main.fragment_to_list_layout.view.*


class ToListLayout : Fragment() {

    private var _binding: FragmentToListLayoutBinding ?= null
    private val binding get() = _binding!!

/*    // Current test data to work with
    // todoitemtest is a mutable list of tasks keyed to a specific master list
    // todolistest is the masterList which is presented on screen 1*/
    //
    /* Todo: Move these to their own class which are invoked when the data changes
    *   It is not a necessary change but it does allow for the code to look cleaner
    *   I also need to implement a method that passes the relevant information to the
    *   closer look of the lists  */
    private val todoitemtest: MutableList<ToDoItem> = mutableListOf(ToDoItem("Finish homework",
        false), ToDoItem("Take out trash", false),
        ToDoItem("Finish homework", false))
    private val todolistest: MutableList<toDoList> = mutableListOf(toDoList("Test", todoitemtest))

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

        /*
        * Here I added a onclicklistener to inflate a DialogView
        * I don't really know any better method to create a dialogue box that allows data to be input
        * And creating a separate view/fragment for creating a list seemed like overkill
        * My primary concern is that i don't know the cost of this in terms of calculation
        * and as such I want to learn a better method in the future if possible
        * */
        binding.addList.setOnClickListener {
            val listdialogView = LayoutInflater.from(context).inflate(R.layout.alert_box_create_list, null)
            val listBuilder = AlertDialog.Builder(context).setView(listdialogView)
            val listAlertDialog = listBuilder.show()

            listAlertDialog.dismissAlert.setOnClickListener{
                listAlertDialog.dismiss()
            }

            listAlertDialog.savenewElement.setOnClickListener{
                /*
                * Here we pass the view to the function so
                * fragment transaction functions
                * not sure if this is a smart move but its all i know at the moment
                * please correct this if it is wrong
                * */
                if (listAlertDialog.newElementName.text.toString().isNotEmpty()){
                    addnewList(listAlertDialog.newElementName.text.toString(), view)
                }else{
                    Toast.makeText(context, "List needs a valid name", Toast.LENGTH_SHORT).show()

                }
            }

            /* Todo i need to add the second view where we go into the master list to add tasks
            Todo this is probably going to be done after I have finished moving my data around to
            Todo make the code look just a bit cleaner
            */
        }

        return view
    }

    fun addnewList(listName:String, view:View){

        // This first bit here is just to check for redundancy. I found
        // No good method to itterate through the mutable list while also specifically
        // Targeting the objects title outside of the forEach operation
        // I want to optimize this in the future but this works for now
        var repeatListing = false

        todolistest.forEach{
            if (listName == it.title){
                repeatListing = true
            }
        }

        // We inform the user that their list already exists
        // I use a tag based system for removing fragments and as such I can't support duplicates
        // Would love to explore that possibility in the future and how i would add
        // an identifier to allow duplicates, probably just tags but with extra steps
        if (repeatListing){
            Toast.makeText(context, "List already exists", Toast.LENGTH_SHORT).show()
        } else{
            // Because there is no overlap in lists we can add a new one to our list
            // We declare an empty ToDoItem list for our new list
            val todoitemsList:MutableList<ToDoItem> = mutableListOf()
            // We add the list to our existing array
            todolistest.add(toDoList(listName, todoitemsList))
            // This is just to keep an eye on the array data and make sure there are no errors
            print(todolistest)
            /*
            So here is where i am not quite sure what to do
            As far as android studio seems concerned I need to keep instantiating the
            fragmentmanager and fragmentTransaction every time i want to make a new fragment action
            This was not needed in our Piano project, but if i don't do this then android studio
            throws a bunch of errors in my face, so this is how its staying for now
             */
            val fragmentmanager = childFragmentManager
            var fragmentTransaction = fragmentmanager.beginTransaction()
            val newTodoList = ToDoMasterList.newInstance(0, listName)

            /* Here I define that on the keyUp of our deleteList button I instantiate
            the managers,transactions again and have them search for the fragment that was
            pressed on by its tag which is delivered as its name.

            I then remove it both as a fragment and from our list with a forEach search
            */

            /*
            * Todo add a dialogue asking if the user is sure they want to delete their list.
            * */
            newTodoList.onKeyUp =  {listname ->
                println(listname)
                val fragmentmanager = childFragmentManager
                val fragmentTransaction = fragmentmanager.beginTransaction()
                val fragment = childFragmentManager.findFragmentByTag(listname)
                if (fragment!= null) {
                    println(fragment.tag)
                    fragmentTransaction.remove(fragment).commit()
                    todolistest.forEach{
                        if (listName == it.title){
                            todolistest.remove(it)
                        }
                    }
                    println(todolistest)
                }
            }

            /*
            Moving back to the primary code of this section, this finally adds the list
            to our primary view and everything just works out pretty nicely now.
            I'm concerned about performance as I am fairly sure I could represent this better,
            however I wanted to follow the visuals of the project somewhat strictly
            and this feels like a pretty good approximation for creating it
            even if it potentially might not be optimal.
             */
            fragmentTransaction.add(view.LinearLayoutForMasterList.id, newTodoList, listName)
            fragmentTransaction.commit()

        }

    }

}
