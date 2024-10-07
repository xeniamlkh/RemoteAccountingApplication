package com.example.remoteaccountingapplication.ui.fragment.handbooks

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.remoteaccountingapplication.R
import com.example.remoteaccountingapplication.RemoteAccountingApplication
import com.example.remoteaccountingapplication.databinding.FragmentProductsBinding
import com.example.remoteaccountingapplication.ui.alertdialogs.DeleteAlertDialog
import com.example.remoteaccountingapplication.ui.fragment.BaseFragment
import com.example.remoteaccountingapplication.ui.recyclerview.OnMenuClickListener
import com.example.remoteaccountingapplication.ui.recyclerview.ProductsRecyclerViewAdapter
import com.example.remoteaccountingapplication.ui.viewmodel.ProductFragmentViewModel
import com.example.remoteaccountingapplication.ui.viewmodel.ProductFragmentViewModelFactory

class ProductsFragment : BaseFragment<FragmentProductsBinding>(), OnMenuClickListener {

    private val viewModel: ProductFragmentViewModel by viewModels {
        ProductFragmentViewModelFactory(
            (activity?.application as RemoteAccountingApplication).repository
        )
    }

    private lateinit var adapter: ProductsRecyclerViewAdapter

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProductsBinding {
        return FragmentProductsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getProducts().observe(this.viewLifecycleOwner) { products ->
            if (binding.recyclerViewProduct.adapter == null) {
                adapter = ProductsRecyclerViewAdapter(products, this)
                binding.recyclerViewProduct.adapter = adapter
            } else {
                (binding.recyclerViewProduct.adapter as ProductsRecyclerViewAdapter)
                    .updateProductsList(products)
            }
        }
    }

    override fun onMenuClick(menuView: View, itemId: Int) {
        val popupMenu = PopupMenu(requireContext(), menuView)
        popupMenu.inflate(R.menu.options_menu)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popupMenu.setForceShowIcon(true)
        }

        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                return when (item?.itemId) {
                    R.id.delete_btn -> {
                        DeleteAlertDialog.newInstance(2, itemId)
                            .show(parentFragmentManager, "DELETE")
                        true
                    }

                    R.id.edit_btn -> {
                        view
                            ?.findNavController()
                            ?.navigate(
                                ProductsFragmentDirections
                                    .actionProductsFragmentToEditHandbookFragment(
                                        2,
                                        itemId,
                                        getString(R.string.label_fragment_edit_handbook)
                                    )
                            )
                        true
                    }

                    else -> {
                        false
                    }
                }
            }
        })
        popupMenu.show()
    }
}