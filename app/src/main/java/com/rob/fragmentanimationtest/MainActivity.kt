package com.rob.fragmentanimationtest

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.Fade
import androidx.transition.TransitionInflater
import androidx.transition.TransitionSet
import kotlinx.android.synthetic.main.fragment1.*

class MainActivity : AppCompatActivity() {
    private val mDelayedTransactionHandler = Handler()
    private val MOVE_DEFAULT_TIME: Long = 1000
    private val FADE_DEFAULT_TIME: Long = 300

    private val runnable: Runnable = Runnable {
        performTransition()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadInitialFragment()
        mDelayedTransactionHandler.postDelayed(runnable, 2000)
    }

    private fun loadInitialFragment() {
        val initialFragment = Fragment1.newInstance()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, initialFragment)
        fragmentTransaction.commit()
    }

    private fun performTransition() {
        if (isDestroyed) {
            return
        }
        val previousFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        val nextFragment = Fragment2.newInstance()

        val fragmentTransaction = supportFragmentManager.beginTransaction()

        // 1. Exit for Previous Fragment
        val exitFade = Fade()
        exitFade.duration = FADE_DEFAULT_TIME
        previousFragment!!.exitTransition = exitFade

        // 2. Shared Elements Transition
        val enterTransitionSet = TransitionSet()
        enterTransitionSet.addTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.move))
        enterTransitionSet.duration = MOVE_DEFAULT_TIME
        enterTransitionSet.startDelay = FADE_DEFAULT_TIME
        nextFragment.sharedElementEnterTransition = enterTransitionSet

        val logo = fragment1_logo
        fragmentTransaction.addSharedElement(logo, logo.transitionName)
        fragmentTransaction.replace(R.id.fragment_container, nextFragment)
        fragmentTransaction.commitAllowingStateLoss()
    }

    override fun onDestroy() {
        super.onDestroy()
        mDelayedTransactionHandler.removeCallbacks(runnable)
    }
}