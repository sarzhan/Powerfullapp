package sarzhan.dp.powerfullapp.ui.auth

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import sarzhan.dp.powerfullapp.R
import sarzhan.dp.powerfullapp.ui.DataState
import sarzhan.dp.powerfullapp.ui.DataStateChangeListener
import sarzhan.dp.powerfullapp.ui.Response
import sarzhan.dp.powerfullapp.ui.ResponseType
import sarzhan.dp.powerfullapp.util.Constants

/**
 * A simple [Fragment] subclass.
 */
class ForgotPasswordFragment : BaseAuthFragment() {

    lateinit var webView: WebView

    lateinit var stateChangeListener: DataStateChangeListener

    val webInteractionCallback = object: WebAppInterface.OnWebInteractionCallback {

        override fun onError(errorMessage: String) {
            Log.e(TAG, "onError: $errorMessage")

            val dataState = DataState.error<Any>(
                response = Response(errorMessage, ResponseType.Dialog())
            )
            stateChangeListener.onDataStateChange(
                dataState = dataState
            )
        }

        override fun onSuccess(email: String) {
            Log.d(TAG, "onSuccess: a reset link will be sent to $email.")
            onPasswordResetLinkSent()
        }

        override fun onLoading(isLoading: Boolean) {
            Log.d(TAG, "onLoading... ")
            CoroutineScope(Dispatchers.Main).launch {
                stateChangeListener.onDataStateChange(
                    DataState.loading(isLoading = isLoading, cachedData = null)
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webView = view.findViewById(R.id.webview)

        loadPasswordResetWebView()

        return_to_launcher_fragment.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun loadPasswordResetWebView(){
        stateChangeListener.onDataStateChange(
            DataState.loading(isLoading = true, cachedData = null)
        )
        webView.webViewClient = object: WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                stateChangeListener.onDataStateChange(
                    DataState.loading(isLoading = false, cachedData = null)
                )
            }
        }
        webView.loadUrl(Constants.PASSWORD_RESET_URL)
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(WebAppInterface(webInteractionCallback), "AndroidTextListener")
    }



    class WebAppInterface
    constructor(
        private val callback: OnWebInteractionCallback
    ) {

        private val TAG: String = "AppDebug"

        @JavascriptInterface
        fun onSuccess(email: String) {
            callback.onSuccess(email)
        }

        @JavascriptInterface
        fun onError(errorMessage: String) {
            callback.onError(errorMessage)
        }

        @JavascriptInterface
        fun onLoading(isLoading: Boolean) {
            callback.onLoading(isLoading)
        }

        interface OnWebInteractionCallback{

            fun onSuccess(email: String)

            fun onError(errorMessage: String)

            fun onLoading(isLoading: Boolean)
        }
    }

    fun onPasswordResetLinkSent(){
        CoroutineScope(Dispatchers.Main).launch{
            parent_view.removeView(webView)
            webView.destroy()

            val animation = TranslateAnimation(
                password_reset_done_container.width.toFloat(),
                0f,
                0f,
                0f
            )
            animation.duration = 500
            password_reset_done_container.startAnimation(animation)
            password_reset_done_container.visibility = View.VISIBLE
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            stateChangeListener = context as DataStateChangeListener
        }catch(e: ClassCastException){
            Log.e(TAG, "$context must implement DataStateChangeListener" )
        }
    }
}
