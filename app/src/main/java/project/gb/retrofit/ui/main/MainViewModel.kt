package project.gb.retrofit.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    val textViewName = MutableStateFlow("")
    val textViewSecondName = MutableStateFlow("")
    val textViewEmail = MutableStateFlow("")
    val textViewPhone = MutableStateFlow("")
    val textViewLocation = MutableStateFlow("")

    val state = MutableStateFlow<State>(State.Success)
    private val _user: MutableStateFlow<ViewUserData?> = MutableStateFlow(null)
    val user = _user.asStateFlow()

    private val _dataInitialized = MutableStateFlow(false)
    val dataInitialized: StateFlow<Boolean> = _dataInitialized // авто заполнение
    private val _dataUpdateRequested = MutableStateFlow(false) // заполнение через кнопку

    fun onSignInClick() {
        viewModelScope.launch {
            if (!_dataInitialized.value || _dataUpdateRequested.value) {
                state.value = State.Loading
                delay(2_000)
                val result = getData()
                if (result != null) {
                    _user.value = result
                    fill(result)
                    _dataInitialized.value = true
                    _dataUpdateRequested.value = false
                }
                state.value = State.Success
            }
        }
    }

    fun updateData() {
        _dataUpdateRequested.value = true
        onSignInClick()
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