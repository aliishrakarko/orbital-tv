package com.premium.tvbrowser.engine

import android.webkit.WebView
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

data class BrowserTab(val id: String = UUID.randomUUID().toString(), var url: String = "https://www.google.com", var title: String = "New Tab", var isPrivate: Boolean = false, var preview: String? = null)
data class Shortcut(val title: String, val domain: String, val url: String, val icon: String)
data class HistoryEntry(val title: String, val domain: String, val url: String, val ts: Long = System.currentTimeMillis())
data class BrowserUiState(
    val tabs: List<BrowserTab> = listOf(BrowserTab()),
    val currentTabId: String = "",
    val history: List<HistoryEntry> = listOf(HistoryEntry("YouTube","youtube.com","https://youtube.com"), HistoryEntry("Wikipedia","wikipedia.org","https://wikipedia.org")),
    val shortcuts: List<Shortcut> = listOf(
        Shortcut("YouTube","youtube.com","https://youtube.com","▶️"),
        Shortcut("Google","google.com","https://google.com","G"),
        Shortcut("Wikipedia","wikipedia.org","https://wikipedia.org","W"),
        Shortcut("Reddit","reddit.com","https://reddit.com","R"),
        Shortcut("Netflix","netflix.com","https://netflix.com","N"),
        Shortcut("X","x.com","https://x.com","𝕏")
    ),
    val isBrowsing: Boolean = false,
    val showTabManager: Boolean = false,
    val showKeyboard: Boolean = false,
    val showMenu: Boolean = false,
    val currentInput: String = ""
){
    val currentTab get() = tabs.find { it.id == currentTabId } ?: tabs.firstOrNull()
}

class BrowserViewModel : ViewModel() {
    private val _ui = MutableStateFlow(BrowserUiState(currentTabId = BrowserUiState().tabs.first().id))
    val uiState: StateFlow<BrowserUiState> = _ui
    private var webView: WebView? = null

    fun attachWebView(wv: WebView){
        webView = wv
        wv.webViewClient = OrbitalWebViewClient { title, url ->
            updateTab(title, url)
        }
        wv.webChromeClient = OrbitalChromeClient()
        wv.loadUrl(_ui.value.currentTab?.url ?: "https://google.com")
    }

    private fun updateTab(title: String, url: String){
        _ui.value = _ui.value.copy(tabs = _ui.value.tabs.map { if(it.id==_ui.value.currentTabId) it.copy(title=title, url=url) else it }, history = listOf(HistoryEntry(title, url.substringAfter("://").substringBefore("/"), url)) + _ui.value.history.take(50))
    }

    fun navigate(url: String){
        val finalUrl = if(url.startsWith("http")) url else "https://www.google.com/search?q=${url}"
        _ui.value = _ui.value.copy(isBrowsing = true)
        webView?.loadUrl(finalUrl)
    }

    fun goBack(){ if(webView?.canGoBack()==true) webView?.goBack() else _ui.value = _ui.value.copy(isBrowsing=false) }
    fun goForward(){ webView?.goForward() }
    fun reload(){ webView?.reload() }
    fun toggleBookmark(){}
    fun openMenu(){ _ui.value = _ui.value.copy(showMenu=true) }
    fun showTabs(){ _ui.value = _ui.value.copy(showTabManager=true) }
    fun newTab(){
        val tab = BrowserTab()
        _ui.value = _ui.value.copy(tabs = _ui.value.tabs + tab, currentTabId = tab.id, showTabManager=false)
        webView?.loadUrl(tab.url)
    }
    fun showAddShortcut(){}
    fun removeShortcut(s: Shortcut){ _ui.value = _ui.value.copy(shortcuts = _ui.value.shortcuts.filter { it!=s }) }
    fun typeKey(k: String){ _ui.value = _ui.value.copy(currentInput = _ui.value.currentInput + k) }
    fun backspace(){ _ui.value = _ui.value.copy(currentInput = _ui.value.currentInput.dropLast(1)) }
    fun submitInput(){ navigate(_ui.value.currentInput); _ui.value = _ui.value.copy(showKeyboard=false, currentInput="") }
}
