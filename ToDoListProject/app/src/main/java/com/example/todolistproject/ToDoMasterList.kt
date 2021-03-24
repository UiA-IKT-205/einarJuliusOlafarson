package com.example.todolistproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.todolistproject.databinding.FragmentToDoMasterListBinding

private lateinit var listname: String

class ToDoMasterList : Fragment() {

    var onKeyUp:((name:String) -> Unit)? = null

    private var _binding:FragmentToDoMasterListBinding ?= null
    private val binding get() = _binding!!
    private var progress:Int = 0


    /*
    * To represent the list we only need the progress of the list and string
    * the only problem is that if the string is passed incorrectly we get a major error
    * with removing the fragment later, as the listname and tag are equal
    * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            progress = it.getInt("PROGRESS")
            listname = it.getString("LISTNAME")?: "?"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentToDoMasterListBinding.inflate(inflater)
        val view = binding.root

        binding.ListName.text = listname
        binding.listProgress.progress = progress


        // Don't really know what to do with this error
        // I want to add support for people who need the feedback from the device
        // But I also dont know how to do this method differently
        // Would love some feedback on it so I can implement this properly for
        // people that require phone feedback
        binding.deleteMasterList.setOnTouchListener(object: View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean{
                when(event?.action){
                    MotionEvent.ACTION_UP -> tag?.let { this@ToDoMasterList.onKeyUp?.invoke(it) }
                }
                return true
            }
        })

        // Todo make this actually go into the listing to add todo items
        binding.MasterListGoToList.setOnClickListener {
            Toast.makeText(context, "You are going into $tag", Toast.LENGTH_SHORT).show()
        }


        // Inflate the layout for this fragment
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(progress: Int, listname: String) =
            ToDoMasterList().apply {
                arguments = Bundle().apply {
                    putInt("PROGRESS", progress)
                    putString("LISTNAME", listname)
                }
            }
    }
}