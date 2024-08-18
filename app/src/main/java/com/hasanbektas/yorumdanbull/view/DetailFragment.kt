package com.hasanbektas.yorumdanbull.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.hasanbektas.yorumdanbull.R
import com.hasanbektas.yorumdanbull.adapter.YorumdanbulDetailAdapter
import com.hasanbektas.yorumdanbull.databinding.FragmentDetailBinding
import com.hasanbektas.yorumdanbull.viewmodel.YorumdanbulViewModel
import com.squareup.picasso.Picasso

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    lateinit var adViewTop : AdView
    lateinit var adViewBottom : AdView

    var searchTextactionColor = ""

    private lateinit var detailViewModel : YorumdanbulViewModel

    private var recyclerDetailAdapter = YorumdanbulDetailAdapter(arrayListOf(),this)

    var goToProduct : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailViewModel = ViewModelProvider(requireActivity()).get(YorumdanbulViewModel::class.java)

        MobileAds.initialize(requireContext()) {}

        adViewTop = binding.adMobViewTop
        val adRequestFirst = AdRequest.Builder().build()
        adViewTop.loadAd(adRequestFirst)

        adViewBottom = binding.adMobViewBottom
        val adRequestSecond = AdRequest.Builder().build()
        adViewBottom.loadAd(adRequestSecond)

        arguments?.let {

            val  productData = DetailFragmentArgs.fromBundle(it).actionList

            searchTextactionColor = DetailFragmentArgs.fromBundle(it).actionSearchText

            productData.let {

                val commentList = ArrayList(it.comment_content_list.commentData)

                binding.recyclerViewCommentDetail.layoutManager = LinearLayoutManager(requireContext())
                recyclerDetailAdapter = YorumdanbulDetailAdapter(commentList,this)
                binding.recyclerViewCommentDetail.adapter = recyclerDetailAdapter

                binding.productNameTextDetail.text = it.productName

                Picasso.get().load(it.productImageUrl).into(binding.productImageDetail)

                goToProduct = it.productSkuId

            }
        }

        binding.goToProductButon.setOnClickListener {

            val url = "https://www.hepsiburada.com/ara?q=$goToProduct"
            val goToHepsiBurada = Intent(Intent.ACTION_VIEW)
            goToHepsiBurada.data = Uri.parse(url)
            startActivity(goToHepsiBurada)
        }

    }
}