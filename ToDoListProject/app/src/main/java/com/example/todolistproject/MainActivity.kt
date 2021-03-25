package com.example.todolistproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.todolistproject.TodoListItem.toDoList
import com.example.todolistproject.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // this right here defines what view we are looking at, nothing else
        // keep this in mind for the future
        /* I created this project as a semi copy of our piano project
        * im sure i did not have to create my mainActivity like this
        * but im used to it now
        *  */
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Early attempt at having a Fragment changer from main activity
//        fun changeFragment(fragment: Fragment){
//            supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
//        }

        /*
        * Todo add the ability to sync with firebase in txt or json form
        * */
    }
}