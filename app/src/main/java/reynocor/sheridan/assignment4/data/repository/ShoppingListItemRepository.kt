package reynocor.sheridan.assignment4.data.repository

import kotlinx.coroutines.flow.Flow
import reynocor.sheridan.assignment4.data.datasource.ShoppingListItemRemoteDataSource
import reynocor.sheridan.assignment4.data.model.ShoppingListItem
import javax.inject.Inject

class ShoppingListItemRepository @Inject constructor(
    private val shoppingListItemRemoteDataSource: ShoppingListItemRemoteDataSource
){
    fun getShoppingListItems(currentUserIdFlow: Flow<String>): Flow<List<ShoppingListItem>> {
        return shoppingListItemRemoteDataSource.getShoppingListItems(currentUserIdFlow)
    }

    suspend fun getShoppingListItem(itemId: String): ShoppingListItem? {
        return shoppingListItemRemoteDataSource.getShoppingListItem(itemId)
    }

    suspend fun create(shoppingListItem: ShoppingListItem) : String {
        return shoppingListItemRemoteDataSource.create(shoppingListItem)
    }

    suspend fun update(shoppingListItem: ShoppingListItem) {
        shoppingListItemRemoteDataSource.update(shoppingListItem)
    }

    suspend fun delete(itemId: String) {
        shoppingListItemRemoteDataSource.delete(itemId)
    }
}