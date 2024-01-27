package com.bonge.airportfly.view.fragment


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.airbnb.mvrx.BaseMvRxFragment
import com.bonge.airportfly.utils.Tab
import com.bonge.airportfly.view.MainActivity


abstract class BaseFragment<T : ViewBinding>(
    private val clazz: Class<T>,
    private val layoutResId: Int
) : BaseMvRxFragment() {

    lateinit var fragmentNavigation: FragmentNavigation
    var binding: T? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (binding == null) {
            val view = inflater.inflate(layoutResId, container, false)
            binding = bind(view)
        }
        return binding?.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentNavigation) {
            fragmentNavigation = context
        }
    }

    fun pushFragment(fragment: Fragment) {
        (activity as? MainActivity)?.pushFragment(fragment)
    }

    fun popFragment() {
        (activity as? MainActivity)?.popFragment()
    }

    interface FragmentNavigation {

        val selectTab: Tab

        fun pushFragment(fragment: Fragment)

        fun popFragment()

    }


    @Suppress("UNCHECKED_CAST")
    private fun <T : ViewBinding> bind(view: View): T {
        val bindMethod = clazz.getDeclaredMethod("bind", View::class.java)
        return bindMethod.invoke(null, view) as T
    }

}