package reynocor.sheridan.assignment4.data.datasource

import com.google.android.engage.shopping.datamodel.ShoppingList
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShoppingListRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    fun getShoppingList(userId: String): Flow<List<ShoppingList>> {
        return firestore.collection("shoppingList")
            .whereEqualTo("userId", userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .dataObjects()
    }


}