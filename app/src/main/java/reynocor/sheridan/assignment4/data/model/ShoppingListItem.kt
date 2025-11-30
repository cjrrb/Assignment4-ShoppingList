package reynocor.sheridan.assignment4.data.model

import com.google.firebase.firestore.DocumentId

data class ShoppingListItem(
    @DocumentId val id: String = "",
    val title: String = "",
    val quantity: Int = 1,
    val price: Double = 0.0,
    val isChecked: Boolean = false
)
