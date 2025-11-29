package reynocor.sheridan.assignment4.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import reynocor.sheridan.assignment4.data.model.ShoppingListItem
import javax.inject.Inject

class ShoppingListItemRemoteDataSource @Inject constructor(private val firestore: FirebaseFirestore){
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getShoppingListItems(currentUserIdFlow: Flow<String>): Flow<List<ShoppingListItem>> {

}