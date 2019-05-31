package com.lovelessgeek.housemanager.ui.main

import android.content.Context
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.lovelessgeek.housemanager.R
import com.lovelessgeek.housemanager.databinding.ItemTaskBinding
import com.lovelessgeek.housemanager.ext.getDrawableCompat
import com.lovelessgeek.housemanager.ext.goneIf
import com.lovelessgeek.housemanager.ext.inflateBinding
import com.lovelessgeek.housemanager.shared.models.Category
import com.lovelessgeek.housemanager.shared.models.Category.Cleaning
import com.lovelessgeek.housemanager.shared.models.Category.Default
import com.lovelessgeek.housemanager.shared.models.Category.Laundry
import com.lovelessgeek.housemanager.shared.models.Category.Trash
import com.lovelessgeek.housemanager.shared.models.Task
import com.lovelessgeek.housemanager.ui.diff
import com.lovelessgeek.housemanager.ui.isOverDue
import com.lovelessgeek.housemanager.ui.lessThanOneDayLeft
import com.lovelessgeek.housemanager.ui.main.TaskListAdapter.ViewHolder
import com.lovelessgeek.housemanager.ui.toReadableDateString
import com.lovelessgeek.housemanager.ui.toSecond

class TaskListAdapter : RecyclerView.Adapter<ViewHolder>() {

    private var onClickDelete: ((Task) -> Unit)? = null

    private val items: MutableList<Task> = mutableListOf()

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflateBinding(R.layout.item_task))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
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

        val context: Context
            get() = itemView.context

        private val normalProgressDrawable =
            context.getDrawableCompat(R.drawable.progress_task_normal)

        private val overDueProgressDrawable =
            context.getDrawableCompat(R.drawable.progress_task_overdue)

        private val todayProgressDrawable =
            context.getDrawableCompat(R.drawable.progress_task_today)

        fun bind(task: Task) {
            with(binding) {
                nameText.text = task.name
                categoryText.text = task.category.readableName
                icon.setImageResource(categoryIcon(task.category))

                repeatIcon.goneIf(!task.isRepeat)
                periodText.goneIf(!task.isRepeat)

                if (task.isRepeat) {
                    periodText.text = task.period.toReadableDateString()
                    taskProgress.max = task.period.toSecond()

                    task.time.time.let { due ->
                        when {
                            due.isOverDue() -> {
                                taskProgress.progressDrawable = overDueProgressDrawable
                                taskProgress.progress = taskProgress.max
                            }
                            due.lessThanOneDayLeft() -> {
                                taskProgress.progressDrawable = todayProgressDrawable
                                taskProgress.progress = taskProgress.max
                            }
                            else -> {
                                taskProgress.progressDrawable = normalProgressDrawable
                                taskProgress.progress = due.diff()
                            }
                        }

                    }

                }
            }
        }

        @DrawableRes
        private fun categoryIcon(category: Category): Int = when (category) {
            Default -> R.drawable.ic_vacuum_cleaner
            Trash -> R.drawable.ic_category_trash
            Cleaning -> R.drawable.ic_category_cleaning
            Laundry -> R.drawable.ic_category_laundry
        }
    }
}