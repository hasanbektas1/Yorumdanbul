package com.hasanbektas.yorumdanbull.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.nativead.NativeAd
import com.hasanbektas.yorumdanbull.databinding.AdmobNativeRowBinding
import com.hasanbektas.yorumdanbull.databinding.RecyclerRowMainBinding
import com.hasanbektas.yorumdanbull.model.YorumdanbulModel
import com.hasanbektas.yorumdanbull.view.MainFragment
import com.hasanbektas.yorumdanbull.view.MainFragmentDirections
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_row_main.view.*

class YorumdanbulMainAdapter (val productList: ArrayList<YorumdanbulModel?>,
                              var mainFragment: MainFragment
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var clickItem = 1

    private var nativeAd : NativeAd? = null

    private val productItem = 1
    private val nativeAdItem = 2

    override fun getItemViewType(position: Int): Int {

        val yorumdanbulListPosition = productList[position]

        return when (yorumdanbulListPosition) {
            null -> nativeAdItem
            else -> productItem
        }
    }

    inner class YorumdanBulNativeHolder(val binding : AdmobNativeRowBinding): RecyclerView.ViewHolder(binding.root) {

        fun bindNative() {

            with(binding) {
                nativeAd?.let {
                    nativeAdView.mediaView = adMedia
                    nativeAdView.headlineView = adHeadline
                    nativeAdView.bodyView = adBody
                    nativeAdView.callToActionView = adButtonAction
                    nativeAdView.iconView = adAppIcon
                    nativeAdView.priceView = adPrice
                  //  nativeAdView.starRatingView = adStars
                    nativeAdView.storeView = adStore
                    nativeAdView.mediaView?.setMediaContent(it.mediaContent!!)
                    nativeAdView.mediaView?.setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                    (nativeAdView.headlineView as TextView).text = it.headline
                    (nativeAdView.bodyView as TextView).text = it.body
                    (nativeAdView.callToActionView as Button).text = it.callToAction
                    (nativeAdView.iconView as ImageView).setImageDrawable(it.icon?.drawable)
                    (nativeAdView.priceView as TextView).text = it.price
                    (nativeAdView.storeView as TextView).text = it.store
                 //   (nativeAdView.starRatingView as RatingBar).rating = it.starRating!!.toFloat()
                    nativeAdView.setNativeAd(it)
                }
            }
        }
    }

    inner class YorumdanBulProductHolder(val binding : RecyclerRowMainBinding): RecyclerView.ViewHolder(binding.root){

        fun bindProduct(){

            Picasso.get().load(productList[layoutPosition]?.productImageUrl).into(itemView.recyclerRowImage)
            itemView.recyclerViewUrunTextInfo.text = productList[layoutPosition]?.productName
            itemView.recyclerViewCategoryTextInfo.text=productList[layoutPosition]?.productCategory
            itemView.recyclerRowYorumSayısıText.text= "Yorum Sayısı: ${productList[layoutPosition]?.commentTotal}"
            itemView.recyclerRowUrunPuanıText.text= "Ürün Puanı: ${productList[layoutPosition]?.productRating}"

            itemView.setOnClickListener {

                clickItem = 0

                val action = MainFragmentDirections.actionMainFragmentToDetailFragment(productList[layoutPosition]!!,mainFragment.editSearchText)
                Navigation.findNavController(it).navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            nativeAdItem -> {
                YorumdanBulNativeHolder(AdmobNativeRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            else -> {
                YorumdanBulProductHolder(RecyclerRowMainBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder){

            is YorumdanBulNativeHolder -> {

                holder.bindNative()

            }
            is YorumdanBulProductHolder -> {

                holder.bindProduct()

            }
        }
    }

    fun setNativeAds(nativeAd: NativeAd) {
        this.nativeAd = nativeAd
    }

    fun updateProductList(newProductList : List<YorumdanbulModel?>){

        if (clickItem == 1) {

            for (i in newProductList.indices) {
                if (i == 4 || i == 8) {
                    productList.add(null)
                }
                productList.add(newProductList[i])
            }
        }
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        clickItem = 1
        return productList.size
    }
}