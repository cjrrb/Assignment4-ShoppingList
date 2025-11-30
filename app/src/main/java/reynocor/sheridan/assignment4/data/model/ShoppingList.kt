package reynocor.sheridan.assignment4.data.model

data class ShoppingList(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val items: List<ShoppingListItem> = emptyList()
)
