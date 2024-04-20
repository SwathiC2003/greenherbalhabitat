package com.example.greenherbalhabitat.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.greenherbalhabitat.Constants
import com.example.greenherbalhabitat.R
import com.example.greenherbalhabitat.adapters.AdapterCategory
import com.example.greenherbalhabitat.databinding.FragmentHomeBinding
import com.example.greenherbalhabitat.models.Category
import com.example.greenherbalhabitat.viewmodels.UserViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel : UserViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        setAllCategories()
        navigatingToSearchFragment()
        get()
        onAccountsClicked()
        return binding.root
    }

    private fun get() {
        viewModel.getAll().observe(viewLifecycleOwner){
            for (i in it){
                Log.d("vvv",i.productTitle.toString())
                Log.d("vvv",i.productCount.toString())

            }
        }
    }


    private fun onAccountsClicked() {
        binding.ivProfile.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }
    }




    private fun navigatingToSearchFragment() {
        binding.searchCv.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchingFragment)
        }
    }


    private fun setAllCategories() {
        val categoryList = ArrayList<Category>()

        for (i in 0 until Constants.allProductsCategoryIcons.size) { categoryList.add(Category(Constants.allProductsCategory[i], Constants.allProductsCategoryIcons[i]))
        }
        binding.rvCategories.adapter = AdapterCategory(categoryList,::onCategoryIconClicked)

    }
    fun onCategoryIconClicked(category: Category){
        val bundle = Bundle()
        bundle.putString("category", category.title)
        findNavController().navigate(R.id.action_homeFragment_to_categoryFragment, bundle)
    }




}