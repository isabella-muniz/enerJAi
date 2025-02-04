package garagem.ideias.enerjai.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import garagem.ideias.enerjai.R
import garagem.ideias.enerjai.model.FoodItem

class FoodAdapter(
    private val onItemClick: (FoodItem) -> Unit
) : ListAdapter<FoodItem, FoodAdapter.FoodViewHolder>(FoodDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_food, parent, false)
        return FoodViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FoodViewHolder(view: View, private val onItemClick: (FoodItem) -> Unit) : RecyclerView.ViewHolder(view) {
        private val foodName: TextView = view.findViewById(R.id.foodName)
        private val nutritionInfo: TextView = view.findViewById(R.id.nutritionInfo)

        fun bind(food: FoodItem) {
            foodName.text = food.alimento
            nutritionInfo.text = String.format("Calorias: %.1f kcal | Proteínas: %.1fg | Carboidratos: %.1fg | Gorduras: %.1fg",
                food.calorias,
                food.proteinas,
                food.carboidratos,
                food.gorduras
            )
            itemView.setOnClickListener { onItemClick(food) }
        }
    }

    private class FoodDiffCallback : DiffUtil.ItemCallback<FoodItem>() {
        override fun areItemsTheSame(oldItem: FoodItem, newItem: FoodItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FoodItem, newItem: FoodItem): Boolean {
            return oldItem == newItem
        }
    }
} 