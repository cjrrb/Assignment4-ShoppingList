package reynocor.sheridan.assignment4.data.repository

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import reynocor.sheridan.assignment4.data.datasource.AuthRemoteDataSource
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource
) {
    val currentUser: FirebaseUser? get() = authRemoteDataSource.currentUser
    val currentUserIdFlow: Flow<String?> get() = authRemoteDataSource.currentUserIdFlow

    suspend fun createGuestAccount(){
        authRemoteDataSource.createGuestAccount()
    }

    suspend fun signedInAnon(): String {
        currentUser?.uid?.let { return it }
        authRemoteDataSource.createGuestAccount()
        return authRemoteDataSource.currentUser?.uid
            ?: throw IllegalStateException("Anon login failed.")
    }

    fun currentUserId(): String? = currentUser?.uid
}