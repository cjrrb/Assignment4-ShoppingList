package reynocor.sheridan.assignment4.ui.item

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import reynocor.sheridan.assignment4.MainViewModel
import reynocor.sheridan.assignment4.R
import reynocor.sheridan.assignment4.data.model.ErrorMessage
import reynocor.sheridan.assignment4.data.model.ShoppingListItem
import reynocor.sheridan.assignment4.data.repository.AuthRepository
import reynocor.sheridan.assignment4.data.repository.ShoppingListItemRepository
import javax.inject.Inject

@HiltViewModel
class ShoppingListItemViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository,
    private val shoppingListItemRepository: ShoppingListItemRepository
) : MainViewModel() {
    private val _navigateToShoppingList = MutableStateFlow(false)
    val navigateToShoppingList: StateFlow<Boolean> get() = _navigateToShoppingList.asStateFlow()

    private val itemId: String = savedStateHandle["itemId"] ?: ""

    private val _shoppingItem = MutableStateFlow<ShoppingListItem?>(null)
    val shoppingItem: StateFlow<ShoppingListItem?> get() = _shoppingItem.asStateFlow()

    fun loadItem(){
        launchCatching {
            if(itemId.isBlank()){
                _shoppingItem.value = ShoppingListItem()
            } else {
                _shoppingItem.value = shoppingListItemRepository.getShoppingListItem(itemId)
            }
        }
    }

    fun saveItem(
        item: ShoppingListItem,
        showErrorSnackbar: (ErrorMessage) -> Unit
    ){
        val userId = authRepository.currentUserId()

        if(userId.isNullOrBlank()){
            showErrorSnackbar(ErrorMessage.IdError(R.string.user_not_logged_in))
            return
        }

        if (item.title.isBlank()){
            showErrorSnackbar(ErrorMessage.IdError(R.string.title_required))
            return
        }

        launchCatching(showErrorSnackbar) {
            if(item.id.isBlank()){
                shoppingListItemRepository.create(item.copy(userId = userId))
            } else {
                shoppingListItemRepository.update(item)
            }
            _navigateToShoppingList.value = true
        }
    }

    fun deleteItem(item: ShoppingListItem){
        launchCatching {
            if(item.id.isNotBlank()){
                shoppingListItemRepository.delete(item.id)
            }
            _navigateToShoppingList.value = true
        }
    }
}