package garagem.ideias.enerjai.model

import com.google.firebase.firestore.PropertyName

data class FoodItem(
    val id: String = "",
    @get:PropertyName("Nome do alimento")
    @PropertyName("Nome do alimento")
    val alimento: String = "",
    @get:PropertyName("Calorias")
    @PropertyName("Calorias")
    val calorias: Double = 0.0,
    @get:PropertyName("Proteina")
    @PropertyName("Proteina")
    val proteinas: Double = 0.0,
    @get:PropertyName("Carboidratos")
    @PropertyName("Carboidratos")
    val carboidratos: Double = 0.0,
    @get:PropertyName("Lipidos")
    @PropertyName("Lipidos")
    val gorduras: Double = 0.0,
    @get:PropertyName("Fibra")
    @PropertyName("Fibra")
    val fibra: Double = 0.0
)