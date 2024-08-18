package com.hasanbektas.yorumdanbull.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.navigation.Navigation
import com.hasanbektas.yorumdanbull.R
import com.hasanbektas.yorumdanbull.databinding.FragmentSscreenBinding


class SscreenFragment : Fragment() {

    private var _binding: FragmentSscreenBinding? = null
    private val binding get() = _binding!!

    private val splashScreenTime = 2500


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSscreenBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ybAnimation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.splash_screen_animation)

        val ybImageView = binding.ybimageView

        ybImageView.animation = ybAnimation

        Handler(Looper.getMainLooper()).postDelayed({

            val action = SscreenFragmentDirections.actionSscreenFragmentToMainFragment()
            Navigation.findNavController(view).navigate(action)

        }, splashScreenTime.toLong())
    }
}