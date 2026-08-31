package com.premium.tvbrowser.ui

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.premium.tvbrowser.engine.BrowserViewModel

@Composable
fun BrowserApp(vm: BrowserViewModel = viewModel()) {
    val state by vm.uiState.collectAsState()
    if (state.isBrowsing) {
        BrowserScreen(state, vm)
    } else {
        HomeScreen(state, vm)
    }
}
