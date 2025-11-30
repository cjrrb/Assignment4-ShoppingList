package reynocor.sheridan.assignment4.data.repository

import kotlinx.coroutines.flow.Flow
import reynocor.sheridan.assignment4.data.datasource.ShoppingListRemoteDataSource
import reynocor.sheridan.assignment4.data.model.ShoppingList
import javax.inject.Inject

class ShoppingListRepository @Inject constructor(
    private val shoppingListRemoteDataSource: ShoppingListRemoteDataSource
){
    val shoppingList: Flow<List<ShoppingList>> = shoppingListRemoteDataSource.getShoppingLists("")

    suspend fun getShoppingList(listId: String): ShoppingList? {
        return shoppingListRemoteDataSource.getShoppingList(listId)
    }

    suspend fun create(shoppingList: ShoppingList): String {
        return shoppingListRemoteDataSource.create(shoppingList)
    }

    suspend fun update(shoppingList: ShoppingList) {
        shoppingListRemoteDataSource.update(shoppingList)
    }

    suspend fun delete(listId: String) {
        shoppingListRemoteDataSource.delete(listId)
    }
}