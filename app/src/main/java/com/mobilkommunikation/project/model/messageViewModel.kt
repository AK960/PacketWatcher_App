import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MessageViewModel : ViewModel() {
    // Monitor list with messages
    private val _messages = MutableStateFlow<List<String>>(emptyList())
    val messages = _messages.asStateFlow()

    // Append message to list
    fun addMessage(clientInfo: String, message: String) {
        val formattedMessage = "$clientInfo: $message"
        _messages.update { currentList ->
            listOf(formattedMessage) + currentList
        }
    }
}
