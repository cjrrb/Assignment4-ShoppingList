package reynocor.sheridan.assignment4.ui.list

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import reynocor.sheridan.assignment4.MainViewModel
import reynocor.sheridan.assignment4.data.model.ShoppingListItem
import reynocor.sheridan.assignment4.data.repository.AuthRepository
import reynocor.sheridan.assignment4.data.repository.ShoppingListItemRepository
import reynocor.sheridan.assignment4.data.repository.ShoppingListRepository
import javax.inject.Inject

@HiltViewModel
class ShoppingListViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val shoppingListItemRepository: ShoppingListItemRepository
) : MainViewModel() {
    private val _isLoadingUser = MutableStateFlow(true)
    val isLoadingUser: StateFlow<Boolean> get() = _isLoadingUser.asStateFlow()

    private val currentUserIdFlow = MutableStateFlow("")

    val shoppingItems = shoppingListItemRepository.getShoppingListItems(authRepository.currentUserIdFlow)

    fun loadCurrentUser(){
        launchCatching {
            if (authRepository.currentUser == null){
                authRepository.createGuestAccount()
            }

            _isLoadingUser.value = false
        }
    }

    fun updateItem(item: ShoppingListItem) {
        launchCatching {
            shoppingListItemRepository.update(item)
        }
    }

    fun deleteItem(item: ShoppingListItem) {
        launchCatching {
            if (item.id.isNotBlank()) {
                shoppingListItemRepository.delete(item.id)
            }
        }
    }
}