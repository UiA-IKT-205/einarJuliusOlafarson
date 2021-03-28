package com.example.todolistproject.MasterList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistproject.ListHolder
import com.example.todolistproject.databinding.ToDoMasterListLayoutBinding

class ToDoMasterListAdapter(private var todomasterlists:List<toDoList>,
                            private val onlistClicked:((toDoList) -> Unit)
):
    RecyclerView.Adapter<ToDoMasterListAdapter.ViewHolder>() {

    class ViewHolder (val binding:ToDoMasterListLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(MasterList: toDoList, onListClicked:(toDoList)->Unit){
            binding.ListName.text = MasterList.title
            binding.listProgress.progress = MasterList.GiveCompletedAmount().toInt()

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
        return ViewHolder(ToDoMasterListLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    public fun updateList(newLists: List<toDoList>){
        todomasterlists = newLists
        notifyDataSetChanged()
        println(todomasterlists)
    }

    public fun deleteList(position: Int){
        ToDoMasterListDepositoryManager.instance.deleteList(position)
        updateList(todomasterlists)
    }

    public fun removeItem(position: Int){

    }
}