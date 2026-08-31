package com.premium.tvbrowser.engine

import android.graphics.Bitmap
import android.webkit.*

class OrbitalWebViewClient(private val onUpdate: (String,String)->Unit) : WebViewClient() {
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        if(url!=null) onUpdate(view?.title ?: url, url)
    }
    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        view?.let { onUpdate(it.title ?: url ?: "", url ?: "") }
    }
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        return false
    }
}

class OrbitalChromeClient : WebChromeClient() {
    override fun onShowCustomView(view: android.view.View?, callback: CustomViewCallback?) {
        // fullscreen video handling
        super.onShowCustomView(view, callback)
    }
}
