package geekbarains.material.view.mainfragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import geekbarains.material.Constant.MEDIA_TYPE
import geekbarains.material.Constant.TOAST_GRAVITY_OFFSET_X
import geekbarains.material.Constant.TOAST_GRAVITY_OFFSET_Y
import geekbarains.material.Constant.WIKI_URL
import geekbarains.material.R
import geekbarains.material.model.AppState
import geekbarains.material.view.BottomNavigationDrawerFragment
import geekbarains.material.view.MainActivity
import geekbarains.material.view.chips.ChipsFragment
import geekbarains.material.viewmodel.MainFragmentViewModel
import kotlinx.android.synthetic.main.fragment_chips.*
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.main_fragment.chipGroup
import java.text.SimpleDateFormat
import java.util.*

class MainFragment : Fragment() {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private val viewModel: MainFragmentViewModel by lazy {
        ViewModelProvider(this).get(MainFragmentViewModel::class.java)
    }

    private lateinit var mainFragmentView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getData(null)
            .observe(viewLifecycleOwner, { renderData(it) })

        setBottomSheetBehavior(view.findViewById(R.id.bottom_sheet_container))

        mainFragmentView = view

        chipGroup.setOnCheckedChangeListener { chipGroup, position ->
            chipGroup.findViewById<Chip>(position)?.let {
                val sdf = SimpleDateFormat(getString(R.string.dateFormat), Locale.US)
                val cal = Calendar.getInstance()

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
        setBottomAppBar(view)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_fav -> toast("Favourite")
            R.id.app_bar_settings -> activity?.supportFragmentManager?.beginTransaction()
                ?.add(R.id.container, ChipsFragment())?.addToBackStack(null)?.commit()
            android.R.id.home -> {
                activity?.let {
                    BottomNavigationDrawerFragment().show(it.supportFragmentManager, "tag")
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun renderData(data: AppState) {
        when (data) {
            is AppState.Success -> {
                val serverResponseData = data.serverResponseData
                val url: String? = if (serverResponseData.mediaType == MEDIA_TYPE) {
                    serverResponseData.url
                } else {
                    serverResponseData.thumbs
                }
                val bsc = mainFragmentView.findViewById(R.id.bottom_sheet_container) as View
                if (url.isNullOrEmpty()) {
                    bsc.visibility = View.GONE
                    toast(getString(R.string.emptyLink))
                } else {

                    image_view.load(url) {
                        lifecycle(this@MainFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                    }

                    bsc.visibility = View.VISIBLE
                    val header = bsc.findViewById<TextView>(R.id.bottom_sheet_description_header)
                    header.text = serverResponseData.title
                    val body = bsc.findViewById<TextView>(R.id.bottom_sheet_description)
                    body.text = serverResponseData.explanation
                }
            }
            is AppState.Loading -> {
            }
            is AppState.Error -> {
                toast(data.error.message)
            }
        }
    }

    private fun setBottomAppBar(view: View) {
        val context = activity as MainActivity
        context.setSupportActionBar(view.findViewById(R.id.bottom_app_bar))
        setHasOptionsMenu(true)
        fab.setOnClickListener {
            if (isMain) {
                isMain = false
                bottom_app_bar.navigationIcon = null
                bottom_app_bar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_back_fab))
                bottom_app_bar.replaceMenu(R.menu.menu_bottom_bar_other_screen)
            } else {
                isMain = true
                bottom_app_bar.navigationIcon =
                    ContextCompat.getDrawable(context, R.drawable.ic_hamburger_menu_bottom_bar)
                bottom_app_bar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_plus_fab))
                bottom_app_bar.replaceMenu(R.menu.menu_bottom_bar)
            }
        }
    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

    }

    private fun Fragment.toast(string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.BOTTOM, TOAST_GRAVITY_OFFSET_X, TOAST_GRAVITY_OFFSET_Y)
            show()
        }
    }

    companion object {
        fun newInstance() = MainFragment()
        private var isMain = true
    }
}