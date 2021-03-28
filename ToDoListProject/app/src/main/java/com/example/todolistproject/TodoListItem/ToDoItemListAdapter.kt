package com.example.todolistproject.TodoListItem

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistproject.MasterList.ToDoMasterListDepositoryManager
import com.example.todolistproject.MasterList.toDoList
import com.example.todolistproject.databinding.ToDoItemLayoutBinding
import kotlin.reflect.KFunction1

class ToDoItemListAdapter(private var todoItemList:List<ToDoItem>,
):
    RecyclerView.Adapter<ToDoItemListAdapter.ViewHolder>() {

    class ViewHolder(val binding:ToDoItemLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(toDoItem: ToDoItem){
            binding.ItemDescription.text = toDoItem.description
            binding.itemBoolean.isChecked = toDoItem.isDone
        }
    }

    override fun getItemCount(): Int = todoItemList.size

    override fun onBindViewHolder(holder: ToDoItemListAdapter.ViewHolder, position: Int) {
        val itemList = todoItemList[position]
        holder.bind(itemList)
        holder.binding.itemBoolean.setOnClickListener{
            setItemBoolean(position, holder.binding.ItemDescription.text.toString())
        }

        holder.binding.deleteItem.setOnClickListener{
            deleteItem(position, holder.binding.ItemDescription.text.toString())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoItemListAdapter.ViewHolder {
        return ToDoItemListAdapter.ViewHolder(ToDoItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    fun setItemBoolean(position: Int, listName: String) {
        ToDoItemListDepositoryManager.instance.setBoolean(position, listName)
        notifyDataSetChanged()
    }

    fun updateItems(newItems: List<ToDoItem>){
        todoItemList = newItems
        notifyDataSetChanged()
    }

   fun deleteItem(position: Int, listName: String){
        ToDoItemListDepositoryManager.instance.deleteItem(position, listName)
        notifyDataSetChanged()
    }

}