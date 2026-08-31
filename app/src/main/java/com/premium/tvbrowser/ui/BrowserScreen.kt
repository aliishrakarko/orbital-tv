package com.premium.tvbrowser.ui

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.premium.tvbrowser.engine.*

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun BrowserScreen(state: BrowserUiState, vm: BrowserViewModel) {
    Box(Modifier.fillMaxSize()) {
        AndroidView(factory = { ctx ->
            WebView(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.mediaPlaybackRequiresUserGesture = false
                vm.attachWebView(this)
            }
        }, modifier = Modifier.fillMaxSize())

        // Floating toolbar
        var showToolbar by remember { mutableStateOf(true) }
        AnimatedVisibility(visible = showToolbar, modifier = Modifier.align(Alignment.BottomCenter).padding(24.dp), enter = slideInVertically { it } + fadeIn(), exit = slideOutVertically { it } + fadeOut()) {
            GlassToolbar(state, vm)
        }

        if (state.showTabManager) TabOverlay(state, vm)
        if (state.showKeyboard) TvKeyboard(vm)
    }
}

@Composable
fun GlassToolbar(state: BrowserUiState, vm: BrowserViewModel) {
    Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color(0xCC151A22)), elevation = CardDefaults.cardElevation(16.dp)) {
        Row(Modifier.padding(12.dp).height(48.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButton(onClick = { vm.goBack() }) { Text("←", color = Color.White) }
            IconButton(onClick = { vm.goForward() }) { Text("→", color = Color.White) }
            IconButton(onClick = { vm.reload() }) { Text("⟳", color = Color.White) }
            Card(Modifier.width(420.dp).fillMaxHeight(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.08f))) {
                Box(Modifier.fillMaxSize().padding(horizontal=16.dp), contentAlignment = Alignment.CenterStart){
                    Text(state.currentTab?.url ?: "", color=Color.White.copy(0.8f), maxLines=1)
                }
            }
            IconButton(onClick = { vm.toggleBookmark() }) { Text("☆", color=Color.White) }
            IconButton(onClick = { vm.openMenu() }) { Text("⋮", color=Color.White) }
            IconButton(onClick = { vm.showTabs() }) { Text("◫ ${state.tabs.size}", color=Color.White) }
        }
    }
}

@Composable
fun TabOverlay(state: BrowserUiState, vm: BrowserViewModel){
    Box(Modifier.fillMaxSize().background(Color(0xE60A0E13)), contentAlignment = Alignment.Center){
        Column {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)){
                state.tabs.forEach { tab ->
                    Card(Modifier.width(320.dp).height(200.dp), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = if(tab.id==state.currentTabId) Color(0xFF1F2A38) else Color.White.copy(0.06f))){
                        Column(Modifier.padding(16.dp)){ Text(tab.title, color=Color.White); Text(tab.url, color=Color.White.copy(0.5f)) }
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
            Button(onClick = { vm.newTab() }){ Text("New Tab") }
        }
    }
}

@Composable
fun TvKeyboard(vm: BrowserViewModel){
    Box(Modifier.fillMaxSize().background(Color(0xCC000000)), contentAlignment = Alignment.BottomCenter){
        Card(Modifier.fillMaxWidth().padding(24.dp), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF1A222E))){
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)){
                val rows = listOf("q w e r t y u i o p", "a s d f g h j k l", "z x c v b n m . /")
                rows.forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)){
                        row.split(" ").forEach { key ->
                            Button(onClick = { vm.typeKey(key) }, modifier = Modifier.size(64.dp,48.dp)){ Text(key) }
                        }
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)){
                    Button(onClick = { vm.typeKey(" ") }, modifier=Modifier.width(200.dp)){ Text("Space") }
                    Button(onClick = { vm.backspace() }){ Text("⌫") }
                    Button(onClick = { vm.submitInput() }){ Text("Go") }
                }
            }
        }
    }
}
