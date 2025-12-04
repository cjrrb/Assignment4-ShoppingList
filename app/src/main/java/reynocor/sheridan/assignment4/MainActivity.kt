package reynocor.sheridan.assignment4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import reynocor.sheridan.assignment4.data.model.ErrorMessage
import reynocor.sheridan.assignment4.ui.item.ShoppingListItemScreen
import reynocor.sheridan.assignment4.ui.list.ShoppingListScreen
import reynocor.sheridan.assignment4.ui.theme.Assignment4ShoppingListTheme

private const val ROUTE_LIST = "shoppingList"
private const val ROUTE_ITEM = "shoppingListItem"


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setSoftInputMode()

        setContent {
            val scope = rememberCoroutineScope()
            val snackbarHostState = remember { SnackbarHostState() }
            val navController = rememberNavController()

            Assignment4ShoppingListTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                   Scaffold(
                       modifier = Modifier.fillMaxSize(),
                       snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                   ) { innerPadding ->
                       NavHost(
                           navController = navController,
                           startDestination = ROUTE_LIST,
                           modifier = Modifier.padding(innerPadding)
                       ) {
                           //Main Shopping List Screen
                           composable(ROUTE_LIST) {
                               ShoppingListScreen(
                                   openShoppingListItem = { itemId: String ->
                                       navController.navigate("$ROUTE_ITEM/$itemId") {
                                           launchSingleTop = true
                                       }
                                   },
                                   showErrorSnackbar = { errorMessage: ErrorMessage ->
                                       val message = getErrorMessage(errorMessage)
                                       scope.launch { snackbarHostState.showSnackbar(message) }
                                   }
                               )
                           }

                           //Single Item Screen (create and edit)
                           composable("ROUTE_ITEM/{itemId}"){ backStackEntry ->
                               val itemId = backStackEntry.arguments?.getString("itemId") ?: ""

                               ShoppingListItemScreen(
                                   itemId = itemId,
                                   openShoppingListScreen = {
                                       navController.navigate(ROUTE_LIST) { launchSingleTop = true }
                                   },
                                   showErrorSnackbar = { errorMessage: ErrorMessage ->
                                       val message = getErrorMessage(errorMessage)
                                       scope.launch { snackbarHostState.showSnackbar(message) }
                                   }
                               )
                           }
                       }
                   }
                }
            }
        }
    }

    private fun setSoftInputMode(){
        window.setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }
    private fun getErrorMessage(error: ErrorMessage): String {
        return when (error) {
            is ErrorMessage.StringError -> error.message
            is ErrorMessage.IdError -> this@MainActivity.getString(error.message)
        }
    }
}