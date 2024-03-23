package project.gb.retrofit.ui.main

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    // region Тестируем двухсторонний биндинг
    val imageView = MutableStateFlow("")
    val textViewName = MutableStateFlow("")
    val textViewSecondName = MutableStateFlow("")
    val textViewEmail = MutableStateFlow("")
    val textViewPhone = MutableStateFlow("")
    val textViewLocation = MutableStateFlow("")
    // endregion

    val state = MutableStateFlow<State>(State.Success)
    private val _user: MutableStateFlow<ViewUserData?> = MutableStateFlow(null)
    val user = _user.asStateFlow()

    fun onSignInClick() {
        viewModelScope.launch {
            state.value = State.Loading
//            delay(2_000)
            val result = getData()
            if (result != null) {
                _user.value = result
                fill(result)
            }
            state.value = State.Success
        }
    }

    private fun fill(result: ViewUserData) {
        textViewName.value = result.results.first().name.first
        textViewSecondName.value = result.results.first().name.last
        textViewEmail.value = result.results.first().email
        textViewPhone.value = result.results.first().phone
        textViewLocation.value = result.results.first().location.country
    }

    private suspend fun getData() : ViewUserData? {
        return RetrofitInstance.searchUserApi.getUserInfo().body()
    }
}