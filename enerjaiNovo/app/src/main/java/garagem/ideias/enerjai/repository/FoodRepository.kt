package garagem.ideias.enerjai.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import garagem.ideias.enerjai.model.FoodItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.tasks.await
import java.util.*

class FoodRepository {
    private val db = FirebaseFirestore.getInstance()
    private val foodCollection = db.collection("foods")

    suspend fun searchFood(query: String): List<FoodItem> = withContext(Dispatchers.IO) {
        try {
            Log.d("FoodRepository", "Searching for query: $query")
            
            // First, let's check what's in the collection
            val allDocs = foodCollection.get().await()
            Log.d("FoodRepository", "Total documents in collection: ${allDocs.documents.size}")
            allDocs.documents.firstOrNull()?.let {
                Log.d("FoodRepository", "Sample document fields: ${it.data}")
            }
            
            val snapshot = foodCollection
                .orderBy("Nome do alimento")
                .whereGreaterThanOrEqualTo("Nome do alimento", query.capitalize())
                .whereLessThanOrEqualTo("Nome do alimento", query.capitalize() + '\uf8ff')
                .get()
                .await()

            Log.d("FoodRepository", "Found ${snapshot.documents.size} documents for query")
            
            return@withContext snapshot.documents.mapNotNull { document ->
                document.toObject(FoodItem::class.java)?.copy(id = document.id).also { food ->
                    Log.d("FoodRepository", """
                        Mapped food item: 
                        - Name: ${food?.alimento}
                        - Calories: ${food?.calorias}
                        - Proteins: ${food?.proteinas}
                        - Fats: ${food?.gorduras}
                        - Carbs: ${food?.carboidratos}
                        - Fiber: ${food?.fibra}
                        Raw data: ${document.data}
                    """.trimIndent())
                }
            }
        } catch (e: Exception) {
            Log.e("FoodRepository", "Error searching food: ${e.message}")
            Log.e("FoodRepository", "Stack trace: ", e)
            return@withContext emptyList()
        }
    }

    private fun String.capitalize(): String {
        return this.replaceFirstChar { 
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) 
            else it.toString() 
        }
    }
} 