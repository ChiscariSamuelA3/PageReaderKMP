package com.demo.pagereader

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import com.demo.pagereader.presentation.PageState
import com.demo.pagereader.presentation.PageReaderViewModel
import kotlinx.coroutines.launch

class PageReaderViewModelOwner : ViewModelStoreOwner {

    override val viewModelStore = ViewModelStore()

    val viewModel: PageReaderViewModel =
        ViewModelProvider.create(this, PageReaderViewModel.Factory)[PageReaderViewModel::class]

    val currentState: PageState
        get() = viewModel.uiState.value

    fun observe(onEach: (PageState) -> Unit) {
        viewModel.viewModelScope.launch {
            viewModel.uiState.collect { onEach(it) }
        }
    }
}
