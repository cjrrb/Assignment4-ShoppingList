package reynocor.sheridan.assignment4.ui.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import reynocor.sheridan.assignment4.R
import reynocor.sheridan.assignment4.data.model.ErrorMessage
import reynocor.sheridan.assignment4.data.model.ShoppingListItem
import reynocor.sheridan.assignment4.ui.common.CenterTopAppBar
import reynocor.sheridan.assignment4.ui.common.LoadingIndicator
import reynocor.sheridan.assignment4.ui.common.PrimaryButton

@Composable
fun ShoppingListItemScreen(
    itemId: String,
    openShoppingListScreen: () -> Unit,
    showErrorSnackbar: (ErrorMessage) -> Unit,
    viewModel: ShoppingListItemViewModel = hiltViewModel()
){
    val navigateToShoppingList by viewModel.navigateToShoppingList.collectAsState()

    if (navigateToShoppingList){
        openShoppingListScreen()
    } else {
        val shoppingItem by viewModel.shoppingItem.collectAsState()

        ShoppingListItemScreen(
            shoppingItem = shoppingItem,
            showErrorSnackBar = showErrorSnackbar,
            saveItem = viewModel::saveItem,
            deleteItem = viewModel::deleteItem,
            loadItem = viewModel::loadItem
        )
    }

    LaunchedEffect(itemId) {
        viewModel.loadItem()
    }
}

@Composable
private fun ShoppingListItemScreen(
    shoppingItem: ShoppingListItem?,
    showErrorSnackBar: (ErrorMessage) -> Unit,
    saveItem: (ShoppingListItem, (ErrorMessage) -> Unit) -> Unit,
    deleteItem: (ShoppingListItem) -> Unit,
    loadItem: () -> Unit
){
    if (shoppingItem == null){
        LoadingIndicator()
    } else {
        ShoppingListItemScreenContent(
            shoppingItem = shoppingItem,
            showErrorSnackbar = showErrorSnackBar,
            saveItem = saveItem,
            deleteItem = deleteItem
        )
    }

    LaunchedEffect(true){
        loadItem()
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ShoppingListItemScreenContent(
    shoppingItem: ShoppingListItem,
    showErrorSnackbar: (ErrorMessage) -> Unit,
    saveItem: (ShoppingListItem, (ErrorMessage) -> Unit) -> Unit,
    deleteItem: (ShoppingListItem) -> Unit
) {
    val editableItem = remember {mutableStateOf(shoppingItem)}

    val quantityText = remember {mutableStateOf(editableItem.value.quantity.toString())}
    val priceText = remember {mutableStateOf(
        if(editableItem.value.price == 0.0) "" else editableItem.value.price.toString()
    )}

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val isNewItem = shoppingItem.id.isBlank()
    val titleRes = if (isNewItem) R.string.create_shopping_item else R.string.edit_shopping_item

    Scaffold(
        topBar = {
            CenterTopAppBar(
                title = stringResource(titleRes),
                icon = Icons.Filled.Check,
                iconDescription = stringResource(R.string.save_item),
                action = {
                    saveItem(editableItem.value, showErrorSnackbar)
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    start = 24.dp,
                    end = 24.dp,
                    bottom = 24.dp
                )
        ){
            Spacer(Modifier.size(16.dp))

            //Title
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = editableItem.value.title,
                onValueChange = {
                    editableItem.value = editableItem.value.copy(title = it)
                },
                label = {
                    Text(text = stringResource(R.string.title))
                },
                singleLine = true
            )

            Spacer(Modifier.size(16.dp))

            //Quantity
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = quantityText.value,
                onValueChange = { text ->
                    quantityText.value = text
                    val quantityInt = text.toIntOrNull()
                    if (quantityInt != null){
                        editableItem.value = editableItem.value.copy(quantity = quantityInt)
                    }
                },
                label = { Text(stringResource(R.string.quantity)) },
                singleLine = true
            )

            Spacer(Modifier.size(16.dp))

            //Price
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = priceText.value,
                onValueChange = { text ->
                    priceText.value = text
                    val priceDouble = text.toDoubleOrNull()
                    if (priceDouble != null){
                        editableItem.value = editableItem.value.copy(price = priceDouble)
                    }
                },
                label = { Text(stringResource(R.string.price)) },
                singleLine = true
            )

            Spacer(Modifier.size(16.dp))

            if (!isNewItem){
                PrimaryButton(
                    label = R.string.delete_item,
                    onClick = {
                        deleteItem(shoppingItem)
                    },
                    modifier = Modifier
                )
            }
        }
    }
}


