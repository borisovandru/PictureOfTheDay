package geekbarains.material.ui.view.planets

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.ChangeBounds
import androidx.transition.ChangeImageTransform
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import coil.load
import geekbarains.material.R
import geekbarains.material.ui.Constant.NASA_TIME_ZONE
import geekbarains.material.ui.Constant.SIGNAL_ARRIVAL_TIME_FROM_MARS
import geekbarains.material.ui.model.AppState
import geekbarains.material.ui.model.retrofit.response.MarsServerResponseData
import geekbarains.material.ui.model.rover.Rover
import geekbarains.material.ui.viewmodel.planets.MarsFragmentViewModel
import geekbarains.material.util.toast
import kotlinx.android.synthetic.main.fragment_mars.*
import java.text.SimpleDateFormat
import java.util.*

class MarsFragment : Fragment() {

    private var isExpanded = false
    private var itemImage = 0
    private var marsDayToPhoto = 0
    private lateinit var serverResponseData: MarsServerResponseData

    private val viewModel: MarsFragmentViewModel by lazy {
        ViewModelProvider(this).get(MarsFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_mars, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sendData()

        imageViewMars.setOnClickListener {
            isExpanded = !isExpanded
            TransitionManager.beginDelayedTransition(
                marsContainer,
                TransitionSet()
                    .addTransition(ChangeBounds())
                    .addTransition(ChangeImageTransform())
            )

            val params: ViewGroup.LayoutParams = imageViewMars.layoutParams
            params.height =
                if (isExpanded) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT
            imageViewMars.layoutParams = params
            imageViewMars.scaleType =
                if (isExpanded) ImageView.ScaleType.CENTER_CROP else ImageView.ScaleType.FIT_CENTER
        }

        setFAB()
    }


    private fun setFAB() {
        setInitialState()
        fab.setOnClickListener {
            if (isExpanded) {
                collapseFab()
            } else {
                expandFAB()
            }
        }
    }

    private fun setInitialState() {
        transparent_background.apply {
            alpha = 0f
        }
        option_two_container.apply {
            alpha = 0f
            isClickable = false
        }
        option_one_container.apply {
            alpha = 0f
            isClickable = false
        }
    }

    private fun nextImage(direction: Int) {
        if (itemImage + direction < serverResponseData.photos.size && itemImage + direction >= 0) {
            itemImage += direction
        }
        countMarsImage.text =
            getString(R.string.itemImage, (itemImage + 1), serverResponseData.photos.size)
        dateMarsImage.text =
            getString(R.string.itemImageDate, serverResponseData.photos[itemImage].earth_date)
        imageViewMars.load(serverResponseData.photos[itemImage].img_src)
    }

    private fun expandFAB() {
        isExpanded = true
        ObjectAnimator.ofFloat(plus_imageview, "rotation", 0f, 225f).start()
        ObjectAnimator.ofFloat(option_two_container, "translationY", -130f).start()
        ObjectAnimator.ofFloat(option_one_container, "translationY", -250f).start()

        option_two_container.animate()
            .alpha(1f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    option_two_container.isClickable = true
                    option_two_container.setOnClickListener {
                        if (serverResponseData.photos.isNotEmpty()) {
                            nextImage(-1)
                        }
                    }
                }
            })
        option_one_container.animate()
            .alpha(1f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    option_one_container.isClickable = true
                    option_one_container.setOnClickListener {
                        if (serverResponseData.photos.isNotEmpty()) {
                            nextImage(1)
                        }
                    }
                }
            })
        transparent_background.animate()
            .alpha(0.9f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    transparent_background.isClickable = true
                }
            })
    }

    private fun collapseFab() {
        isExpanded = false
        ObjectAnimator.ofFloat(plus_imageview, "rotation", 0f, -180f).start()
        ObjectAnimator.ofFloat(option_two_container, "translationY", 0f).start()
        ObjectAnimator.ofFloat(option_one_container, "translationY", 0f).start()

        option_two_container.animate()
            .alpha(0f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    option_two_container.isClickable = false
                    option_one_container.setOnClickListener(null)
                }
            })
        option_one_container.animate()
            .alpha(0f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    option_one_container.isClickable = false
                }
            })
        transparent_background.animate()
            .alpha(0f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    transparent_background.isClickable = false
                }
            })
    }

    private fun sendData() {
        val sdf = SimpleDateFormat(getString(R.string.dateFormat), Locale.US)
        sdf.timeZone = TimeZone.getTimeZone(NASA_TIME_ZONE)
        val cal = Calendar.getInstance(TimeZone.getTimeZone(NASA_TIME_ZONE))

        cal.add(Calendar.DAY_OF_YEAR, SIGNAL_ARRIVAL_TIME_FROM_MARS - marsDayToPhoto)

        val itemDate: String = sdf.format(cal.time)

        val roversName = resources.getStringArray(R.array.rovers_name)
        val roversDateStart = resources.getStringArray(R.array.rovers_date_start)
        val roversDateEnd = resources.getStringArray(R.array.rovers_date_end)

        val rovers = mutableListOf<Rover>()
        for (i in roversName.indices) {
            val arrayName = "camera_rover_$i"
            val arrayNameID =
                resources.getIdentifier(arrayName, "array", requireActivity().packageName)
            val cameras = resources.getStringArray(arrayNameID).toList()
            rovers.add(Rover(roversName[i], roversDateStart[i], roversDateEnd[i], cameras))
        }

        viewModel.getData(rovers.last().name, itemDate, rovers.last().cameras.first())
            .observe(
                viewLifecycleOwner, {
                    renderData(it)
                }
            )
    }

    private fun renderData(data: AppState) {
        when (data) {
            is AppState.SuccessMars -> {
                serverResponseData = data.serverResponseData
                if (serverResponseData.photos.isNotEmpty()) {
                    countMarsImage.text = getString(
                        R.string.itemImage,
                        (itemImage + 1),
                        serverResponseData.photos.size
                    )
                    dateMarsImage.text = getString(
                        R.string.itemImageDate,
                        serverResponseData.photos.first().earth_date
                    )
                    imageViewMars.load(serverResponseData.photos.first().img_src)
                } else {
                    marsDayToPhoto++
                    sendData()
                }
            }
            is AppState.Loading -> {
            }
            is AppState.Error -> {
                toast(data.error.message)
            }
        }
    }
}