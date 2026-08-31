package com.premium.tvbrowser.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.premium.tvbrowser.engine.*

@Composable
fun HomeScreen(state: BrowserUiState, vm: BrowserViewModel) {
    Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFF0A0E13), Color(0xFF151A22))))) {
        Column(Modifier.fillMaxSize().padding(48.dp), verticalArrangement = Arrangement.spacedBy(32.dp)) {
            // Top capsule search
            SearchCapsule(vm)

            Text("Quick Access", color = Color.White.copy(0.9f), fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                items(state.shortcuts) { site ->
                    ShortcutCard(site, onClick = { vm.navigate(site.url) }, onLong = { vm.removeShortcut(site) })
                }
                item { AddShortcutCard { vm.showAddShortcut() } }
            }

            Text("Continue Browsing", color = Color.White.copy(0.7f), fontSize = 20.sp)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(state.history.take(8)) { h ->
                    HistoryCard(h) { vm.navigate(h.url) }
                }
            }
        }
    }
}

@Composable
fun SearchCapsule(vm: BrowserViewModel) {
    var focused by remember { mutableStateOf(false) }
    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Card(
            modifier = Modifier.width(if(focused) 720.dp else 640.dp).height(64.dp).onFocusChanged{focused=it.isFocused},
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(if(focused) 0.12f else 0.08f)),
            elevation = CardDefaults.cardElevation(defaultElevation = if(focused) 12.dp else 0.dp)
        ) {
            Box(Modifier.fillMaxSize().padding(horizontal = 24.dp), contentAlignment = Alignment.CenterStart) {
                Text("🔍  Search or enter website", color = Color.White.copy(0.7f), fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun ShortcutCard(site: Shortcut, onClick: ()->Unit, onLong: ()->Unit) {
    Card(Modifier.size(160.dp, 120.dp), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.06f))) {
        Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center) {
            Text(site.icon, fontSize = 28.sp)
            Spacer(Modifier.height(8.dp))
            Text(site.title, color = Color.White, fontWeight = FontWeight.Medium)
            Text(site.domain, color = Color.White.copy(0.5f), fontSize = 12.sp)
        }
    }
}

@Composable
fun AddShortcutCard(onClick: ()->Unit){ Card(Modifier.size(160.dp,120.dp), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.04f))) { Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){ Text("+", color=Color.White.copy(0.6f), fontSize=32.sp) } } }

@Composable
fun HistoryCard(h: HistoryEntry, onClick: ()->Unit){
    Card(Modifier.width(260.dp).height(100.dp), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.06f))) {
        Column(Modifier.padding(16.dp)){ Text(h.title, color=Color.White, maxLines=1); Text(h.domain, color=Color.White.copy(0.5f), fontSize=12.sp) }
    }
}
