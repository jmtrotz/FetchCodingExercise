package com.jefftrotz.fetchcodingexercise.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jefftrotz.fetchcodingexercise.R
import com.jefftrotz.fetchcodingexercise.data.model.Item
import com.jefftrotz.fetchcodingexercise.databinding.RecyclerViewRowBinding

/**
 * Main adapter for the RecyclerView.
 * @param itemList A List of Items to display in the RecyclerView.
 */
class MainAdapter(private val itemList: List<Item>):
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: RecyclerViewRowBinding):
        RecyclerView.ViewHolder(binding.root)

    /**
     * Called when the RecyclerView needs a new ViewHolder.
     * @param parent The ViewGroup into which the new View will be added after it
     * is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerViewRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return ViewHolder(binding)
    }

    /**
     * Called by the RecyclerView to display the data at the specified position.
     * @param holder The ViewHolder that needs to be updated.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(itemList[position]) {
                if (position % 2 == 0) {
                    binding.constraintLayoutMain.setBackgroundColor(
                        ContextCompat.getColor(
                            holder.itemView.context,
                            R.color.light_grey
                        ))
                }

                binding.textViewListId.text = this.listId.toString()
                binding.textViewName.text = this.name
            }
        }
    }

    /**
     * Get the total number of items in the data set held by the adapter.
     * @return The total number of items in this adapter as an Int.
     */
    override fun getItemCount(): Int {
        return itemList.size
    }
}