package geekbarains.material.view.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import geekbarains.material.R
import geekbarains.material.model.Settings
import geekbarains.material.util.sharedpref.SharedPref
import kotlinx.android.synthetic.main.fragment_chips.*

class SettingsFragment : Fragment() {

    private var checkTheme = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        checkTheme = true
        return inflater.inflate(R.layout.fragment_chips, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkTheme = false

        chipGroup.setOnCheckedChangeListener { chipGroup, position ->
            chipGroup.findViewById<Chip>(position)?.let {
                Toast.makeText(context, "Выбран ${it.text}", Toast.LENGTH_SHORT).show()
            }
        }

        chip_close.setOnCloseIconClickListener {
            Toast.makeText(
                context,
                "Close is Clicked",
                Toast.LENGTH_SHORT
            ).show()
        }

        view.findViewById<ChipGroup>(R.id.chipGroupTheme)

        chipMars.setOnClickListener {
            setTheme(R.style.AppThemeMars)
        }

        chipEarth.setOnClickListener {
            setTheme(R.style.AppThemeEarth)
        }

        chipMoon.setOnClickListener {
            setTheme(R.style.AppThemeMoon)
        }
    }

    private fun styleToConst(style: Int): Int {
        return when (style) {
            R.style.AppThemeMars -> 0
            R.style.AppThemeEarth -> 1
            R.style.AppThemeMoon -> 2
            else -> 0
        }
    }

    private fun setTheme(style: Int) {
        SharedPref(requireContext()).saveSettings(Settings(styleToConst(style)))
        requireActivity().setTheme(style)
        requireActivity().recreate()
    }
}