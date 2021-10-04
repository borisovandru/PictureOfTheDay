package geekbarains.material.ui.view.mainfragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.view.*
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.material.chip.Chip
import geekbarains.material.R
import geekbarains.material.ui.Constant
import geekbarains.material.ui.Constant.MEDIA_TYPE_IMAGE
import geekbarains.material.ui.Constant.WIKI_URL
import geekbarains.material.ui.viewmodel.mainfragment.MainFragmentViewModel
import geekbarains.material.util.OnSwipeTouchListener
import geekbarains.material.util.toast
import kotlinx.android.synthetic.main.fragment_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainFragment : Fragment() {

    private val viewModel: MainFragmentViewModel by lazy {
        ViewModelProvider(this).get(MainFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(R.layout.fragment_main_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val canvas: Canvas

        webView.webViewClient = MyWebViewClient()
        webView.settings.javaScriptEnabled = true

        viewModel.getData(null)
            .observe(viewLifecycleOwner, { appState ->
                renderData(appState)
            })

        wiki_button.setOnTouchListener(object : OnSwipeTouchListener(requireActivity()) {
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                motionLayout.transitionToEnd()
            }

            override fun onSwipeRight() {
                super.onSwipeRight()
                motionLayout.transitionToStart()
            }
        })

        chipGroup.setOnCheckedChangeListener { group, checkedId ->
            group.findViewById<Chip>(checkedId)?.let {

                val sdf = SimpleDateFormat(getString(R.string.dateFormat), Locale.US)
                sdf.timeZone = TimeZone.getTimeZone(Constant.NASA_TIME_ZONE)
                val cal = Calendar.getInstance(TimeZone.getTimeZone(Constant.NASA_TIME_ZONE))

                when (it.text) {
                    resources.getString(R.string.two_yesterday) -> cal.add(Calendar.DAY_OF_YEAR, -2)
                    resources.getString(R.string.yesterday) -> cal.add(Calendar.DAY_OF_YEAR, -1)
                    resources.getString(R.string.today) -> cal.add(Calendar.DAY_OF_YEAR, 0)
                }
                val itemDate: String? = sdf.format(cal.time)

                viewModel.getData(itemDate)
                    .observe(viewLifecycleOwner, { appState ->
                        renderData(appState)
                    })
            }
        }

        input_layout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(WIKI_URL + input_edit_text.text.toString())
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }


    private fun renderData(data: geekbarains.material.ui.model.AppState) {
        when (data) {
            is geekbarains.material.ui.model.AppState.SuccessAPOD -> {
                val serverResponseData = data.serverResponseData

                val url: String?

                if (serverResponseData.mediaType != MEDIA_TYPE_IMAGE && serverResponseData.url != null) {
                    webView.visibility = View.VISIBLE
                    webView.loadUrl(serverResponseData.url)
                    image_view.visibility = View.INVISIBLE
                    url = serverResponseData.thumbs
                } else {
                    webView.visibility = View.INVISIBLE
                    image_view.visibility = View.VISIBLE
                    image_view.load(serverResponseData.url)
                    url = serverResponseData.url
                }

                if (url.isNullOrEmpty()) {
                    toast(getString(R.string.emptyLink))
                } else {
                    header.text = serverResponseData.title
                    description.text = serverResponseData.explanation

                    val spannableString = SpannableString(serverResponseData.explanation)
                    val nasaStr = "PictureOfTheDay"
                    var nasaStrItem = 0
                    var nasaStrItemColor = false
                    serverResponseData.explanation?.let {
                        for (i in it.toCharArray().indices) {
                            if (it.toCharArray()[i].uppercaseChar() == nasaStr.toCharArray()[nasaStrItem].uppercaseChar()) {

                                if (nasaStrItem < 3) {
                                    nasaStrItem++
                                } else {
                                    nasaStrItem = 0
                                    nasaStrItemColor = !nasaStrItemColor
                                }
                                val itemColor = if (nasaStrItemColor) {
                                    Color.CYAN
                                } else {
                                    Color.RED
                                }
                                spannableString.setSpan(
                                    BackgroundColorSpan(itemColor),
                                    i,
                                    i + 1,
                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                                )

                                description.setText(spannableString, TextView.BufferType.SPANNABLE)
                            }
                        }
                    }
                }
            }
            is geekbarains.material.ui.model.AppState.Loading -> {
            }
            is geekbarains.material.ui.model.AppState.Error -> {
                toast(data.error.message)
            }
        }
    }

    companion object;

    private inner class MyWebViewClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            return true
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?,
        ): Boolean {
            return true
        }

        override fun onRenderProcessGone(
            view: WebView?,
            detail: RenderProcessGoneDetail?,
        ): Boolean {
            return super.onRenderProcessGone(view, detail)
        }

        override fun onUnhandledKeyEvent(view: WebView?, event: KeyEvent?) {
            super.onUnhandledKeyEvent(view, event)
        }
    }
}