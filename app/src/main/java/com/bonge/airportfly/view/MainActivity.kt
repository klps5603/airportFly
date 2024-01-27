package com.bonge.airportfly.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bonge.airportfly.R
import com.bonge.airportfly.databinding.ActivityMainBinding
import com.bonge.airportfly.utils.Tab
import com.bonge.airportfly.utils.TabUtil
import com.bonge.airportfly.view.fragment.BaseFragment
import com.bonge.airportfly.view.fragment.ExchangeRateFragment
import com.bonge.airportfly.view.fragment.FlightFragment
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), BaseFragment.FragmentNavigation {
    override val selectTab: Tab
        get() = tabUtil.selectTab!!

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var tabUtil: TabUtil
    private var lastBackPressTime: Long = 0

    private val list = mutableListOf<Fragment>(
        FlightFragment(), ExchangeRateFragment()
    )

    override fun pushFragment(fragment: Fragment) {
        tabUtil.push(fragment)
    }

    override fun popFragment() {
        tabUtil.pop()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            supportActionBar?.hide()
            setContentView(root)
            tabUtil =
                TabUtil(savedInstanceState, supportFragmentManager, frameLayout.id, list)
            if (savedInstanceState == null) {
                tabUtil.switchTab(Tab.航班)
            }

            bottomNavigation.setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.airport -> {
                        tabUtil.switchTab(Tab.航班)
                    }

                    R.id.exchange_rate -> {
                        tabUtil.switchTab(Tab.匯率)
                    }

                }
                true
            }

        }

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (tabUtil.currentStack.size == 1) {
                    if (System.currentTimeMillis() - lastBackPressTime > 2000) {
                        Toast.makeText(
                            this@MainActivity,
                            getString(R.string.confirm_exit_app),
                            Toast.LENGTH_SHORT
                        ).show()
                        lastBackPressTime = System.currentTimeMillis()
                    } else {
                        exitProcess(0)
                    }
                } else {
                    popFragment()
                }
            }

        })

    }

}