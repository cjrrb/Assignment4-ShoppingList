package reynocor.sheridan.assignment4.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await
import reynocor.sheridan.assignment4.data.model.ShoppingListItem
import javax.inject.Inject

class ShoppingListItemRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getShoppingListItems(currentUserIdFlow: Flow<String>): Flow<List<ShoppingListItem>> {
        return currentUserIdFlow.flatMapLatest { currentUserId ->
            firestore
                .collection("shoppingListItems")
                .whereEqualTo("userId", currentUserId)
                .dataObjects<ShoppingListItem>()
        }
    }

    suspend fun getShoppingListItem(itemId: String): ShoppingListItem? {
        return firestore
            .collection("shoppingListItems")
            .document(itemId)
            .get()
            .await()
            .toObject<ShoppingListItem>()
    }

    suspend fun create(shoppingListItem: ShoppingListItem): String {
        return firestore
            .collection("shoppingListItems")
            .add(shoppingListItem)
            .await()
            .id
    }

    suspend fun update(shoppingListItem: ShoppingListItem) {
        firestore
            .collection("shoppingListItems")
            .document(shoppingListItem.id)
            .set(shoppingListItem)
            .await()
    }

    suspend fun delete(itemId: String) {
        firestore
            .collection("shoppingListItems")
            .document(itemId)
            .delete()
            .await()
    }
}