package reynocor.sheridan.assignment4

import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import reynocor.sheridan.assignment4.data.model.ErrorMessage

open class MainViewModel {
    fun launchCatching(
        showErrorSnackbar: (ErrorMessage) -> Unit = {},
        block: suspend CoroutineScope.() -> Unit
    ) =
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                Firebase.crashlytics.recordException(throwable)
                val error = if (throwable.message.isNullOrBlank()) {
                    ErrorMessage.IdError(R.string.generic_error)
                } else {
                    ErrorMessage.StringError(throwable.message!!)
                }
                showErrorSnackbar(error)
            },
            block = block
        )
}