package reynocor.sheridan.assignment4.data.repository

import reynocor.sheridan.assignment4.data.datasource.ShoppingListRemoteDataSource

class ShoppingListRepository @Inject constructor(
    private val shoppingListRemoteDataSource: ShoppingListRemoteDataSource
){

}