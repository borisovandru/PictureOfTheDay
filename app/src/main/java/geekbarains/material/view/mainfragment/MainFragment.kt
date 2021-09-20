package geekbarains.material.view.mainfragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.main_fragment.*
import geekbarains.material.Constant
import geekbarains.material.Constant.MEDIA_TYPE_IMAGE
import geekbarains.material.Constant.WIKI_URL
import geekbarains.material.R
import geekbarains.material.util.OnSwipeTouchListener
import geekbarains.material.util.toast
import geekbarains.material.viewmodel.mainfragment.MainFragmentViewModel
import java.text.SimpleDateFormat
import java.util.*

class MainFragment : Fragment() {

    private val viewModel: MainFragmentViewModel by lazy {
        ViewModelProvider(this).get(MainFragmentViewModel::class.java)
    }

    private lateinit var mainFragmentView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(R.layout.main_fragment_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webView.webViewClient = MyWebViewClient()
        webView.settings.javaScriptEnabled = true

        viewModel.getData(null)
            .observe(viewLifecycleOwner, { renderData(it) })

        wiki_button.setOnTouchListener (object : OnSwipeTouchListener(requireActivity()) {
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                motionLayout.transitionToEnd()
            }

            override fun onSwipeRight() {
                super.onSwipeRight()
                motionLayout.transitionToStart()
            }
        })

        mainFragmentView = view

        chipGroup.setOnCheckedChangeListener { chipGroup, position ->
            chipGroup.findViewById<Chip>(position)?.let {
                val sdf = SimpleDateFormat(getString(R.string.dateFormat), Locale.US)
                val cal = Calendar.getInstance(TimeZone.getTimeZone(Constant.NASA_TIME_ZONE))

                when (position) {
                    1 -> cal.add(Calendar.DAY_OF_YEAR, -2)
                    2 -> cal.add(Calendar.DAY_OF_YEAR, -1)
                    3 -> cal.add(Calendar.DAY_OF_YEAR, 0)
                }
                val itemDate: String? = sdf.format(cal.time)

                viewModel.getData(itemDate)
                    .observe(viewLifecycleOwner, { renderData(it) })
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

    private fun renderData(data: geekbarains.material.model.AppState) {
        when (data) {
            is geekbarains.material.model.AppState.SuccessAPOD -> {
                val serverResponseData = data.serverResponseData

                //val bsc = mainFragmentView.findViewById(R.id.bottom_sheet_container) as View

                val url: String?

                if (serverResponseData.mediaType != MEDIA_TYPE_IMAGE
                    && serverResponseData.url != null) {
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
                    //bsc.visibility = View.GONE
                    toast(getString(R.string.emptyLink))
                } else {
                    /*bsc.visibility = View.VISIBLE
                    val header = bsc.findViewById<TextView>(R.id.bottom_sheet_description_header)
                    header.text = serverResponseData.title
                    val body = bsc.findViewById<TextView>(R.id.bottom_sheet_description)
                    body.text = serverResponseData.explanation*/
                    header.text = serverResponseData.title
                    description.text = serverResponseData.explanation
                }
            }
            is geekbarains.material.model.AppState.Loading -> {
            }
            is geekbarains.material.model.AppState.Error -> {
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