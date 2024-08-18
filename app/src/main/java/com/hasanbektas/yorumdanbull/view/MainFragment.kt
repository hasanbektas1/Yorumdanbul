package com.hasanbektas.yorumdanbull.view

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.nativead.NativeAd
import com.hasanbektas.yorumdanbull.R
import com.hasanbektas.yorumdanbull.adapter.YorumdanbulMainAdapter
import com.hasanbektas.yorumdanbull.databinding.FragmentMainBinding
import com.hasanbektas.yorumdanbull.model.YorumdanbulCategoryModel
import com.hasanbektas.yorumdanbull.viewmodel.YorumdanbulViewModel

class MainFragment : Fragment() {

    private var _binding : FragmentMainBinding? = null
    private val binding get() = _binding!!

    var editSearchText = ""
    private var pageRefresh = 0
    private var selectedcategoryName = ""
    private var selectedCategoryId = ""

    private lateinit var categoryMainList : ArrayList<YorumdanbulCategoryModel>

    private var recyclerMainAdapter = YorumdanbulMainAdapter(arrayListOf(),this)
    private lateinit var layoutManager: GridLayoutManager

    private lateinit var mainViewModel : YorumdanbulViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mainViewModel = ViewModelProvider(this).get(YorumdanbulViewModel::class.java)
        mainViewModel.getCategoryDataFromAPI()

        timerBackgroundColor(ContextCompat.getColor(requireContext(),R.color.yb_background_button_color),binding.searchButton)

        observeLiveData()

        binding.autoCompleteTextView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

            selectedcategoryName = categoryMainList[position].categoryName
            selectedCategoryId = categoryMainList[position].categoryId

            if (selectedcategoryName.isNotEmpty() && editSearchText.length >=3){

                binding.searchButton.isClickable = true

                timerBackgroundColor(ContextCompat.getColor(requireContext(),R.color.yb_color),binding.searchButton)
            }
        }

        binding.searchText.addTextChangedListener {
            it?.let {
                if (it.toString().isNotEmpty()){

                    editSearchText =binding.searchText.text.toString()

                    if (selectedcategoryName.isNotEmpty() && editSearchText.length >=3){

                        binding.searchButton.isClickable = true

                        timerBackgroundColor(ContextCompat.getColor(requireContext(),R.color.yb_color),binding.searchButton)

                    }else {
                        binding.searchButton.isClickable = false
                        timerBackgroundColor(ContextCompat.getColor(requireContext(),R.color.yb_background_button_color),binding.searchButton)
                    }
                }
            }
        }

        binding.searchButton.setOnClickListener {
            timerBackgroundColor(ContextCompat.getColor(requireContext(),R.color.yb_background_button_color),binding.searchButton)

            binding.searchButton.isClickable = false

            Handler(Looper.getMainLooper()).postDelayed({

                binding.searchButton.isClickable = true

                timerBackgroundColor(ContextCompat.getColor(requireContext(),R.color.yb_color),binding.searchButton)

            },2500)

            pageRefresh = 0

            if (pageRefresh == 0){

                recyclerMainAdapter.productList.clear()

            }
            it.hideKeyboard()

            binding.categoryMenu.clearFocus()
            binding.searchTextView.clearFocus()

            editSearchText =binding.searchText.text.toString()

            if (selectedcategoryName.isNotEmpty()){

                if (editSearchText.length >=3){

                    searchResulTextStyle()

                    mainViewModel.getProductDataFromAPI(editSearchText,pageRefresh,selectedCategoryId)

                }else{
                    Toast.makeText(requireContext(),"En az üç karanter girin. Örn;eba,oyun" , Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(requireContext(),"Kategöri seçiniz" , Toast.LENGTH_LONG).show()
            }
        }

        binding.recyclerViewMain.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if(dy<-2){
                    timerBackgroundColor(ContextCompat.getColor(requireContext(),R.color.yb_background_button_color),binding.searchButton)
                    binding.searchButton.isClickable = false
                } else {

                    binding.searchButton.isClickable = true
                    timerBackgroundColor(ContextCompat.getColor(requireContext(),R.color.yb_color),binding.searchButton)
                }
                if (!recyclerView.canScrollVertically(-1)){

                    binding.searchButton.isClickable = true
                    timerBackgroundColor(ContextCompat.getColor(requireContext(),R.color.yb_color),binding.searchButton)

                }
                if (!recyclerView.canScrollVertically(1)){

                    pageRefresh = pageRefresh + 10

                    mainViewModel.getProductDataFromAPI(editSearchText,pageRefresh,selectedCategoryId)
                }
            }
        })

        if (editSearchText.isNotEmpty()){
            searchResulTextStyle()
        }

        layoutManager = GridLayoutManager(requireContext(),2)
        binding.recyclerViewMain.layoutManager = layoutManager
        binding.recyclerViewMain.adapter = recyclerMainAdapter
    }

    private fun observeLiveData(){

        mainViewModel.prodcutList.observe(viewLifecycleOwner, Observer { products ->

            products?.let {

                recyclerMainAdapter.updateProductList(products)

                val adLoader = AdLoader.Builder(requireContext(), "AdmobID")
                    .forNativeAd { ad : NativeAd ->

                        recyclerMainAdapter.setNativeAds(ad)
                    }.build()
                adLoader.loadAd(AdRequest.Builder().build())

                if (it.isEmpty()){

                    Toast.makeText(requireContext(),"Eşleşen ürün yorumu bulunamadı",Toast.LENGTH_LONG).show()

                }
            }
        })
        mainViewModel.ybError.observe(viewLifecycleOwner, Observer { error ->

            error.let {

                if (it){
                    binding.ybErrorButton.visibility = View.VISIBLE

                    binding.ybErrorButton.setOnClickListener {

                        val action = MainFragmentDirections.actionMainFragmentToSscreenFragment()
                        Navigation.findNavController(it).navigate(action)

                    }
                    Toast.makeText(requireContext(),"Veriler yüklenemedi internet bağlantınızı kontrol edip tekrar deneyin",Toast.LENGTH_LONG).show()
                } else {
                    binding.ybErrorButton.visibility = View.GONE
                }
            }
        })
        mainViewModel.ybLoading.observe(viewLifecycleOwner, Observer { loading ->

            loading?.let {
                if (it){
                    binding.ybProgressBar.visibility = View.VISIBLE
                    binding.ybErrorButton.visibility = View.GONE

                } else {
                    binding.ybProgressBar.visibility = View.GONE
                }
            }
        })

        mainViewModel.categoryList.observe(viewLifecycleOwner, Observer { categories ->

            categories?.let {

                categoryMainList = ArrayList(it)

                categoryMainList.let {

                    val categoryAdapter = ArrayAdapter(requireContext(),
                        R.layout.categorylist_row_item,categoryMainList.map {  dataList -> dataList.categoryName })
                    binding.autoCompleteTextView.setAdapter(categoryAdapter)

                }
            }
        })
    }

    fun searchResulTextStyle(){
        val resultText  = SpannableString("Arama sonucu içinde ${editSearchText} geçen sonuçlar")
        resultText.setSpan(StyleSpan(Typeface.BOLD),20,20+editSearchText.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        binding.searchTextResult.text = resultText
    }

    fun timerBackgroundColor (color:Int, colorButtonLayout: Button?)
    {
        if (colorButtonLayout != null){
            colorButtonLayout.setBackgroundColor(color)
        }
    }
    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

}