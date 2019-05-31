package com.lovelessgeek.housemanager.ui.main

import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.lovelessgeek.housemanager.R
import com.lovelessgeek.housemanager.databinding.ItemTaskBinding
import com.lovelessgeek.housemanager.ext.goneIf
import com.lovelessgeek.housemanager.ext.inflateBinding
import com.lovelessgeek.housemanager.shared.models.Category
import com.lovelessgeek.housemanager.shared.models.Category.Cleaning
import com.lovelessgeek.housemanager.shared.models.Category.Default
import com.lovelessgeek.housemanager.shared.models.Category.Laundry
import com.lovelessgeek.housemanager.shared.models.Category.Trash
import com.lovelessgeek.housemanager.shared.models.Task
import com.lovelessgeek.housemanager.ui.main.TaskListAdapter.ViewHolder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskListAdapter() : RecyclerView.Adapter<ViewHolder>() {

    private var onClickDelete: ((Task) -> Unit)? = null

    private val items: MutableList<Task> = mutableListOf()

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflateBinding(R.layout.item_task))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
//        holder.deleteButton.setOnClickListener {
//            val task = mItems[position]
//            mItems.removeAt(position)
//            notifyItemRemoved(position)
//            notifyItemRangeChanged(position, itemCount)
//
//            onClickDelete?.invoke(task)
//        }
    }

    fun onClickDelete(onClickDelete: ((Task) -> Unit)?) {
        this.onClickDelete = onClickDelete
    }

    fun add(item: Task) {
        items.add(item)
        notifyItemInserted(items.lastIndex)
    }

    fun addAll(newItems: List<Task>) {
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    /**
     * ViewHolder
     */
    inner class ViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // TODO: Progress
        fun bind(task: Task) {
            with(binding) {
                nameText.text = task.name
                categoryText.text = task.category.readableName
                icon.setImageResource(categoryIcon(task.category))

                repeatIcon.goneIf(!task.isRepeat)
                periodText.goneIf(!task.isRepeat)

                if (task.isRepeat)
                    periodText.text = task.period.toReadableDateString()

                // Temporary
                taskProgress.max = 100
                taskProgress.progress = (0..100).random()
            }
        }

        @DrawableRes
        private fun categoryIcon(category: Category): Int = when (category) {
            Default -> R.drawable.ic_vacuum_cleaner
            Trash -> R.drawable.ic_category_trash
            Cleaning -> R.drawable.ic_category_cleaning
            Laundry -> R.drawable.ic_category_laundry
        }

        // TODO: 이쁘게 해주는 라이브러리 있음
        private fun Int.toReadableDateString(): String {
            val dateFormat = SimpleDateFormat("dd일", Locale.KOREA)
            return dateFormat.format(Date(toLong()))
        }
    }
}