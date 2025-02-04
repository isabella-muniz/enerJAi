package garagem.ideias.enerjai.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import garagem.ideias.enerjai.databinding.ActivityFoodSearchBinding
import garagem.ideias.enerjai.ui.adapter.FoodAdapter
import garagem.ideias.enerjai.viewmodel.FoodSearchViewModel

class FoodSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFoodSearchBinding
    private val viewModel: FoodSearchViewModel by viewModels()
    private lateinit var adapter: FoodAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        setupObservers()
    }

    private fun setupViews() {
        adapter = FoodAdapter { food ->
            Toast.makeText(this, "Selected: ${food.alimento} (${food.calorias} kcal)", Toast.LENGTH_SHORT).show()
        }
        
        binding.recyclerViewResults.apply {
            layoutManager = LinearLayoutManager(this@FoodSearchActivity)
            adapter = this@FoodSearchActivity.adapter
        }

        binding.searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let { 
                        Log.d("FoodSearchActivity", "Submitting search for: $it")
                        viewModel.searchFood(it) 
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (!newText.isNullOrBlank()) {
                        Log.d("FoodSearchActivity", "Text changed to: $newText")
                        viewModel.searchFood(newText)
                    } else {
                        adapter.submitList(emptyList())
                    }
                    return true
                }
            })
            
            // Set focus and show keyboard
            requestFocus()
        }
    }

    private fun setupObservers() {
        viewModel.searchResults.observe(this) { foods ->
            Log.d("FoodSearchActivity", "Received ${foods.size} foods to display")
            adapter.submitList(foods)
            binding.emptyView.isVisible = foods.isEmpty()
            binding.recyclerViewResults.isVisible = foods.isNotEmpty()
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

        viewModel.error.observe(this) { error ->
            if (error.isNotEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }
    }
} 