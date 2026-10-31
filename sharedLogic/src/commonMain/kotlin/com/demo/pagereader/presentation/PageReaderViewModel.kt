package com.demo.pagereader.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.demo.pagereader.api.endpoints.PageReaderException
import com.demo.pagereader.api.DependencyContainer
import com.demo.pagereader.models.PageReaderError
import com.demo.pagereader.repositories.PageRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface ReadStatus {
    data object Idle : ReadStatus
    data object Loading : ReadStatus
    data class Success(val text: String) : ReadStatus
    data class Failure(val error: PageReaderError) : ReadStatus
}

class PageReaderViewModel internal constructor(
    private val repository: PageRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PageState())
    val uiState: StateFlow<PageState> = _uiState.asStateFlow()

    private var readJob: Job? = null

    fun onReadClick() {
        readJob?.cancel()

        val state = _uiState.value
        _uiState.update { it.copy(status = ReadStatus.Loading) }

        readJob = viewModelScope.launch {
            val status = try {
                ReadStatus.Success(repository.readText(state.url, state.selector))
            } catch (e: PageReaderException) {
                ReadStatus.Failure(e.error)
            }
            _uiState.update { it.copy(status = status) }
        }
    }

    fun onUrlChange(url: String) {
        _uiState.update { it.copy(url = url) }
    }

    fun onSelectorChange(selector: String) {
        _uiState.update { it.copy(selector = selector) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer { PageReaderViewModel(DependencyContainer.pageRepository) }
        }
    }
}