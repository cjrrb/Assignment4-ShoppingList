package reynocor.sheridan.assignment4.ui.list

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import reynocor.sheridan.assignment4.R
import reynocor.sheridan.assignment4.data.model.ErrorMessage
import reynocor.sheridan.assignment4.data.model.ShoppingListItem
import reynocor.sheridan.assignment4.ui.common.CenterTopAppBar
import reynocor.sheridan.assignment4.ui.common.LoadingIndicator
import kotlin.collections.emptyList

@Composable
fun ShoppingListScreen(
    openShoppingListItem: (String) -> Unit,
    showErrorSnackbar: (ErrorMessage) -> Unit,
    viewModel: ShoppingListViewModel = hiltViewModel()
) {
    val isLoadingUser by viewModel.isLoadingUser.collectAsState()
    val shoppingItems by viewModel.shoppingItems.collectAsState(initial = emptyList())

    if (isLoadingUser){
        LoadingIndicator()
    } else {
        ShoppingListScreenContent(
            shoppingItems = shoppingItems,
            openShoppingListItem = openShoppingListItem,
            updateItem = viewModel::updateItem,
            deleteItem = viewModel::deleteItem
        )
    }

    LaunchedEffect(true) {
        viewModel.loadCurrentUser()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreenContent(
    shoppingItems: List<ShoppingListItem>,
    openShoppingListItem: (String) -> Unit,
    updateItem: (ShoppingListItem) -> Unit,
    deleteItem: (ShoppingListItem) -> Unit
){
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            CenterTopAppBar(
                title = stringResource(R.string.app_name),
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { openShoppingListItem("") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ){
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.add_item)
                )
            }
        }
    ){ innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = innerPadding.calculateTopPadding(),
                        start = 8.dp,
                        end = 8.dp,
                        bottom = 8.dp
                    ),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                items(shoppingItems, key = { it.id }) { item ->
                    ShoppingListRow(
                        item = item,
                        onCheckedChange = { checked ->
                            updateItem(item.copy(isChecked = checked))
                        },
                        onClick = {
                            if (item.id.isNotBlank()) {
                                openShoppingListItem(item.id)
                            }
                        }
                    )
                }
            }
        }
}

@Composable
private fun ShoppingListRow(
    item: ShoppingListItem,
    onCheckedChange: (Boolean) -> Unit,
    onClick: () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Checkbox(
            checked = item.isChecked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary,
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .clickable{onClick()}
        ) {
            Text(
                item.title.ifBlank { stringResource(R.string.untitled_item) },
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = stringResource(
                    R.string.item_details_format,
                    item.quantity,
                    item.price
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

