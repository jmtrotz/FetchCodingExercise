package com.jefftrotz.fetchcodingexercise.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefftrotz.fetchcodingexercise.model.Result
import com.jefftrotz.fetchcodingexercise.model.Item
import com.jefftrotz.fetchcodingexercise.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch

/**
 * Main ViewModel for this application. Acts as a link
 * between the MainActivity and the MainRepository.
 */
class MainViewModel(
    private val mainRepository: MainRepository
): ViewModel() {
    private val _items = MutableStateFlow<List<Item>?>(null)
    val items: StateFlow<List<Item>?> = _items

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    init {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.getRemoteData().collect {
                when (it) {
                    is Result.Loading -> {
                        _isLoading.emit(true)
                    }
                    is Result.Success -> {
                        _isLoading.emit(false)
                        _items.emit(it.value())
                    }
                    is Result.Error -> {
                        _isLoading.emit(false)
                        _errorMessage.emit("Failed to download data")
                    }
                }
            }
        }
    }
}