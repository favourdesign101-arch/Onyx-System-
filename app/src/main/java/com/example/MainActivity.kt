package com.example

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.ObsidianBackground

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        Scaffold(
          modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBackground)
        ) { innerPadding ->
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(ObsidianBackground)
              .safeDrawingPadding()
          ) {
            OnyxWebView(modifier = Modifier.fillMaxSize())
          }
        }
      }
    }
  }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun OnyxWebView(modifier: Modifier = Modifier) {
  var webViewRef by remember { mutableStateOf<WebView?>(null) }
  var canGoBack by remember { mutableStateOf(false) }

  BackHandler(enabled = canGoBack) {
    webViewRef?.goBack()
  }

  AndroidView(
    modifier = modifier.testTag("onyx_web_view"),
    factory = { context ->
      WebView(context).apply {
        layoutParams = ViewGroup.LayoutParams(
          ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.MATCH_PARENT
        )
        setBackgroundColor(Color.parseColor("#090A0F"))
        setLayerType(View.LAYER_TYPE_HARDWARE, null)

        settings.apply {
          javaScriptEnabled = true
          domStorageEnabled = true
          databaseEnabled = true
          allowFileAccess = true
          loadsImagesAutomatically = true
          mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
          useWideViewPort = true
          loadWithOverviewMode = true
          cacheMode = WebSettings.LOAD_DEFAULT
        }

        webChromeClient = WebChromeClient()

        webViewClient = object : WebViewClient() {
          override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            canGoBack = view?.canGoBack() ?: false
          }

          override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            canGoBack = view?.canGoBack() ?: false
          }

          override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
          ) {
            super.onReceivedError(view, request, error)
          }
        }

        loadUrl("file:///android_asset/index.html")
        webViewRef = this
      }
    },
    update = { webView ->
      webViewRef = webView
    }
  )
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  androidx.compose.material3.Text(
    text = "ONYX Systems: $name",
    modifier = modifier,
    color = com.example.ui.theme.CyanAccent
  )
}


