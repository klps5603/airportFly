package com.bonge.airportfly.utils

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bonge.airportfly.R
import org.json.JSONArray
import java.util.Stack


class TabUtil(
    savedInstanceState: Bundle?,
    private val fragmentManager: FragmentManager?,
    @param:IdRes @field:IdRes
    private val containerId: Int,
    baseFragments: MutableList<Fragment>
) {


    private val fragmentStacks: MutableList<Stack<Fragment>>?

    var selectTab: Tab? = null
        private set
    private var currentFragment: Fragment? = null
    private var mTagCount: Int = 0

    private//Attempt to used stored current fragment
    //if not, try to pull it from the stack
    val currentFrag: Fragment?
        get() {
            return if (currentFragment != null) {
                currentFragment
            } else {
                val fragmentStack = fragmentStacks!![getTabIndex(selectTab!!)]
                if (!fragmentStack.isEmpty()) {
                    fragmentManager!!.findFragmentByTag(
                        fragmentStacks[getTabIndex(selectTab!!)].peek().tag
                    )
                } else {
                    null
                }
            }
        }

    val size: Int
        get() {
            return fragmentStacks?.size ?: 0
        }

    val currentStack: Stack<Fragment>
        get() = fragmentStacks!![getTabIndex(selectTab!!)]

    init {
        fragmentStacks = ArrayList(baseFragments.size)

        if (savedInstanceState == null) {
            for (fragment in baseFragments) {
                val stack = Stack<Fragment>()
                stack.add(fragment)
                fragmentStacks.add(stack)
            }
        } else {
            onRestoreFromBundle(savedInstanceState, baseFragments)
        }
    }

    private fun getTabIndex(tab: Tab): Int = tab.ordinal

    fun switchTab(tab: Tab) {
        val index = getTabIndex(tab)
        if (index >= fragmentStacks!!.size) {
            throw IndexOutOfBoundsException(
                "Can't switch to a tab that hasn't been " +
                        "initialized, " +
                        "Index : " + index + ", current stack size : " + fragmentStacks.size +
                        ". Make sure to create all of the tabs you need in the Constructor"
            )
        }

        val fragmentStack = fragmentStacks[index]
        if (fragmentStack.isEmpty()) {
            return
        }

        if (selectTab != tab) {
            selectTab = tab

            val ft = fragmentManager!!.beginTransaction()

            detachCurrentFragment(ft)

            //Attempt to reattach previous fragment
            var fragment = reattachPreviousFragment(ft)
            if (fragment != null) {
                commit(ft)
            } else {
                fragment = fragmentStacks[getTabIndex(selectTab!!)].peek()
                ft.add(containerId, fragment!!, generateTag(fragment))
                commit(ft)
            }

            currentFragment = fragment
            //            if (mNavListener != null) {
            //                mNavListener.onTabTransaction(mCurrentFrag, mSelectedTabIndex);
            //            }
        }

    }

    /**
     * Attempts to detach any current fragment if it exists, and if none is found, returns;
     *
     * @param ft the current transaction being performed
     */
    private fun detachCurrentFragment(ft: FragmentTransaction) {
        val oldFrag = currentFrag
        if (oldFrag != null) {
            ft.detach(oldFrag)
        }
    }

    /**
     * Will attempt to reattach a previous fragment in the FragmentManager, or return null if not
     * able to,
     *
     * @param ft current fragment transaction
     * @return Fragment if we were able to find and reattach it
     */
    private fun reattachPreviousFragment(ft: FragmentTransaction): Fragment? {
        val fragmentStack = fragmentStacks!![getTabIndex(selectTab!!)]
        var fragment: Fragment? = null
        if (!fragmentStack.isEmpty()) {
            fragment = fragmentManager!!.findFragmentByTag(fragmentStack.peek().tag)
            if (fragment != null) {
                ft.attach(fragment)
            }
        }
        return fragment
    }

    /**
     * Create a unique fragment tag so that we CAN grab the fragment later from the FragmentManger
     *
     * @param fragment The fragment that we're creating a unique tag for
     * @return a unique tag using the fragment's class name
     */
    private fun generateTag(fragment: Fragment): String {
        //        return fragment.getClass().getName() + ++mTagCount;
        return fragment.javaClass.name + selectTab!!
    }

    /**
     * Restores this instance to the state specified by the contents of savedInstanceState
     *
     * @param savedInstanceState The bundle to restore from
     * @param baseFragments      List of base fragments from which to initialize empty stacks
     */
    private fun onRestoreFromBundle(savedInstanceState: Bundle, baseFragments: List<Fragment>) {

        // Restore selected tab

        val index = savedInstanceState.getInt(EXTRA_SELECTED_TAB_INDEX, -1)
        selectTab = if (index == -1) null else Tab.values()[index]

        // Restore tag count
        mTagCount = savedInstanceState.getInt(EXTRA_TAG_COUNT, 0)

        // Restore current fragment
        currentFragment =
            fragmentManager!!.findFragmentByTag(savedInstanceState.getString(EXTRA_CURRENT_FRAGMENT))

        // Restore fragment stacks
        try {
            val stackArrays = JSONArray(savedInstanceState.getString(EXTRA_FRAGMENT_STACK))

            for (x in 0 until stackArrays.length()) {
                val stackArray = stackArrays.getJSONArray(x)
                val stack = Stack<Fragment>()

                if (stackArray.length() == 1) {
                    val tag = stackArray.getString(0)
                    val fragment: Fragment?

                    if (tag == null || "null".equals(tag, ignoreCase = true)) {
                        fragment = baseFragments[x]
                    } else {
                        fragment = fragmentManager.findFragmentByTag(tag)
                    }

                    if (fragment != null) {
                        stack.add(fragment)
                    }
                } else {
                    for (y in 0 until stackArray.length()) {
                        val tag = stackArray.getString(y)

                        if (tag != null && !"null".equals(tag, ignoreCase = true)) {
                            val fragment = fragmentManager.findFragmentByTag(tag)

                            if (fragment != null) {
                                stack.add(fragment)
                            }
                        }
                    }
                }

                fragmentStacks!!.add(stack)
            }
        } catch (t: Throwable) {
            fragmentStacks!!.clear()
            for (fragment in baseFragments) {
                val stack = Stack<Fragment>()
                stack.add(fragment)
                fragmentStacks.add(stack)
            }
        }

    }

    /**
     * Call this in your Test1's onSaveInstanceState(Bundle outState) method to save the
     * instance's state.
     *
     * @param outState The Bundle to save state information to
     */
    fun onSaveInstanceState(outState: Bundle) {

        // Write tag count
        outState.putInt(EXTRA_TAG_COUNT, mTagCount)

        // Write select tab
        outState.putInt(EXTRA_SELECTED_TAB_INDEX, getTabIndex(selectTab!!))

        // Write current fragment
        if (currentFragment != null) {
            outState.putString(EXTRA_CURRENT_FRAGMENT, currentFragment!!.tag)
        }

        // Write stacks
        try {
            val stackArrays = JSONArray()

            for (stack in fragmentStacks!!) {
                val stackArray = JSONArray()

                for (fragment in stack) {
                    stackArray.put(fragment.tag)
                }

                stackArrays.put(stackArray)
            }

            outState.putString(EXTRA_FRAGMENT_STACK, stackArrays.toString())
        } catch (t: Throwable) {
            // Nothing we CAN do
        }

    }

    fun popToFirst() {
        val poppingFrag = currentFrag
        if (poppingFrag != null) {
            val ft = fragmentManager!!.beginTransaction()
            //            ft.remove(poppingFrag);

            //overly cautious fragment pop
            val fragmentStack = fragmentStacks!![getTabIndex(selectTab!!)]
            val n = fragmentStack.size
            if (n == 1) {
                return
            }
            for (i in 0 until n - 1) {
                fragmentStack.pop()
            }

            ft.setCustomAnimations(
                R.anim.slide_left_in, R.anim
                    .slide_right_out, R.anim.slide_left_in, R.anim
                    .slide_right_out
            )

            //Attempt reattach, if we CAN't, try to pop from the stack and push that on
            var fragment = reattachPreviousFragment(ft)
            if (fragment == null && !fragmentStack.isEmpty()) {
                fragment = fragmentStack.peek()
                ft.add(containerId, fragment, fragment.tag)
            }

            ft.remove(poppingFrag)


            //Commit our transactions
            commit(ft)
            fragmentManager.executePendingTransactions()

            currentFragment = fragment
            //            if (mNavListener != null) {
            //                mNavListener.onFragmentTransaction(mCurrentFrag);
            //            }
        }
    }

    /**
     * Pop the current fragment from the current tab
     */
    fun pop() {
        val poppingFrag = currentFrag
        if (poppingFrag != null) {
            val ft = fragmentManager!!.beginTransaction()
            //            ft.remove(poppingFrag);
            //overly cautious fragment pop
            val fragmentStack = fragmentStacks!![getTabIndex(selectTab!!)]
            if (!fragmentStack.isEmpty()) {
                fragmentStack.pop()
            }

            ft.setCustomAnimations(
                R.anim.slide_left_in, R.anim
                    .slide_right_out, R.anim.slide_left_in, R.anim
                    .slide_right_out
            )

            //Attempt reattach, if we CAN't, try to pop from the stack and push that on
            var fragment = reattachPreviousFragment(ft)
            if (fragment == null && !fragmentStack.isEmpty()) {
                fragment = fragmentStack.peek()
                ft.add(containerId, fragment!!, fragment.tag)
            }

            ft.remove(poppingFrag)


            //Commit our transactions
            commit(ft)
            fragmentManager.executePendingTransactions()

            currentFragment = fragment
            //            if (mNavListener != null) {
            //                mNavListener.onFragmentTransaction(mCurrentFrag);
            //            }
        }
    }

    /**
     * Push a fragment onto the current stack
     *
     * @param fragment The fragment that is to be pushed
     */
    fun push(fragment: Fragment?) {
        if (fragment != null) {

            val ft = fragmentManager!!.beginTransaction()
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)


            ft.setCustomAnimations(
                R.anim.slide_right_in, R.anim
                    .slide_left_out, R.anim.slide_right_in, R.anim
                    .slide_left_out
            )
            ft.add(containerId, fragment, generateTag(fragment))
            detachCurrentFragment(ft)
            commit(ft)

            fragmentManager.executePendingTransactions()
            fragmentStacks!![getTabIndex(selectTab!!)].push(fragment)

            currentFragment = fragment

        }
    }

    /**
     * 重新載入指定 fragment 到第一頁
     */
    fun reloadToFirstFragment(newFragment: Fragment) {
        val poppingFrag = currentFrag
        if (poppingFrag != null) {
            val ft = fragmentManager!!.beginTransaction()
            val fragmentStack = fragmentStacks!![getTabIndex(selectTab!!)]

            fragmentStack.clear()
            var fragment = reattachPreviousFragment(ft)
            if (fragment == null && !fragmentStack.isEmpty()) {
                fragment = fragmentStack.peek()
                ft.add(containerId, fragment!!, fragment.tag)
            }
            ft.remove(poppingFrag)

            //            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

            ft.add(containerId, newFragment, generateTag(newFragment))

            detachCurrentFragment(ft)
            ft.commitAllowingStateLoss()

            fragmentManager.executePendingTransactions()
            fragmentStacks[getTabIndex(selectTab!!)].push(newFragment)

            currentFragment = newFragment
        }
    }

    /**
     * 刷新指定 tab fragment 回第一頁
     */
    fun refreshAnotherTabFragment(tab: Tab) {
        if (tab == selectTab) {
            popToFirst()
        } else {
            val fragmentStack = fragmentStacks!![getTabIndex(tab)]
            val n = fragmentStack.size
            if (n == 1) {
                return
            }
            for (i in 0 until n - 1) {
                fragmentStack.pop()
            }
        }

    }

    private fun commit(ft: FragmentTransaction?) {
        if (fragmentManager != null && ft != null) {
            if (fragmentManager.isStateSaved) {
                ft.commitAllowingStateLoss()
            } else {
                ft.commit()
            }
        }
    }

    companion object {
        private val EXTRA_TAG_COUNT = TabUtil::class.java.name + ":EXTRA_TAG_COUNT"
        private val EXTRA_SELECTED_TAB_INDEX =
            (TabUtil::class.java.name + ":EXTRA_SELECTED_TAB_INDEX")
        private val EXTRA_CURRENT_FRAGMENT =
            (TabUtil::class.java.name + ":EXTRA_CURRENT_FRAGMENT")
        private val EXTRA_FRAGMENT_STACK = (TabUtil::class.java.name + ":EXTRA_FRAGMENT_STACK")
    }
}
