package garagem.ideias.enerjai.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import garagem.ideias.enerjai.model.FoodItem
import garagem.ideias.enerjai.repository.FoodRepository
import kotlinx.coroutines.launch

class FoodSearchViewModel : ViewModel() {
    private val repository = FoodRepository()
    
    private val _searchResults = MutableLiveData<List<FoodItem>>()
    val searchResults: LiveData<List<FoodItem>> = _searchResults
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error
    
    fun searchFood(query: String) {
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }
        
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = ""
                
                val results = repository.searchFood(query)
                results.forEach { food ->
                    Log.d("FoodSearchViewModel", """
                        Food item in ViewModel:
                        - Name: ${food.alimento}
                        - Calories: ${food.calorias}
                        - Proteins: ${food.proteinas}
                        - Carbs: ${food.carboidratos}
                        - Fats: ${food.gorduras}
                    """.trimIndent())
                }
                _searchResults.value = results
                
                if (results.isEmpty()) {
                    _error.value = "No foods found for '$query'"
                }
            } catch (e: Exception) {
                Log.e("FoodSearchViewModel", "Error searching: ${e.message}")
                _error.value = "Error: ${e.message}"
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
} 