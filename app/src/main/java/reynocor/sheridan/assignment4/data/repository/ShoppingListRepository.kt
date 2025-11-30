package reynocor.sheridan.assignment4.data.repository

import kotlinx.coroutines.flow.Flow
import reynocor.sheridan.assignment4.data.datasource.ShoppingListRemoteDataSource
import reynocor.sheridan.assignment4.data.model.ShoppingListItem
import javax.inject.Inject

class ShoppingListRepository @Inject constructor(
    private val shoppingListRemoteDataSource: ShoppingListRemoteDataSource
){
    val shoppingList: Flow<List<ShoppingListItem>> = shoppingListRemoteDataSource.getShoppingList
}