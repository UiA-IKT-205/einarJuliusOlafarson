package com.example.todolistproject.TodoListItem

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistproject.databinding.FragmentToDoMasterListBinding

class ToDoMasterListAdapter(private var todomasterlists:List<toDoList>,
                            private val onlistClicked:(toDoList) -> Unit):
    RecyclerView.Adapter<ToDoMasterListAdapter.ViewHolder>() {

    class ViewHolder (val binding:FragmentToDoMasterListBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(MasterList: toDoList, onListClicked:(toDoList)->Unit){
            binding.ListName.text = MasterList.title
            binding.listProgress.progress = MasterList.GiveCompletedAmount()

            binding.MasterListGoToList.setOnClickListener {
                onListClicked(MasterList)
            }
        }
    }

    override fun getItemCount(): Int = todomasterlists.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val masterList = todomasterlists[position]
        holder.bind(masterList, onlistClicked)
        holder.binding.deleteMasterList.setOnClickListener{
            deleteList(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FragmentToDoMasterListBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    public  fun updateList(newLists: List<toDoList>){
        todomasterlists = newLists
        notifyDataSetChanged()
    }

    public fun deleteList(position: Int){
        ToDoMasterListDepositoryManager.instance.deleteList(position)
        updateList(todomasterlists)
    }
}