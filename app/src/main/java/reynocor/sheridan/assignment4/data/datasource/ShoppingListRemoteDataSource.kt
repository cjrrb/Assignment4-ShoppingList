package reynocor.sheridan.assignment4.data.datasource

import reynocor.sheridan.assignment4.data.model.ShoppingList
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import reynocor.sheridan.assignment4.data.repository.AuthRepository
import javax.inject.Inject

class ShoppingListRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val authRepository: AuthRepository
) {
    fun getShoppingLists(): Flow<List<ShoppingList>> {
        val userId = authRepository.currentUserId()
            ?: throw IllegalStateException("User is not logged in")

        return firestore.collection("shoppingList")
            .whereEqualTo("userId", userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .dataObjects()
    }

    suspend fun getShoppingList(listId: String): ShoppingList? {
        return firestore.collection("shoppingList")
            .document(listId)
            .get()
            .await()
            .toObject()
    }

    suspend fun create(list: ShoppingList): String {
        val userId = authRepository.currentUserId()
            ?: throw IllegalStateException("User is not logged in")

        val withUser = list.copy(userId = userId)
        return firestore.collection("shoppingList")
            .add(withUser)
            .await()
            .id
    }

    suspend fun update(list: ShoppingList) {
        firestore.collection("shoppingList")
            .document(list.id)
            .set(list)
            .await()
    }

    suspend fun delete(listId: String) {
        firestore.collection("shoppingList")
            .document(listId)
            .delete()
            .await()
    }
}