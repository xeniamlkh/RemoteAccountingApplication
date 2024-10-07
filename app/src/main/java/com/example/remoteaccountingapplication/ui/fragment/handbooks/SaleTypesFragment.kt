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
import com.example.remoteaccountingapplication.databinding.FragmentSaleTypesBinding
import com.example.remoteaccountingapplication.ui.alertdialogs.DeleteAlertDialog
import com.example.remoteaccountingapplication.ui.fragment.BaseFragment
import com.example.remoteaccountingapplication.ui.recyclerview.OnMenuClickListener
import com.example.remoteaccountingapplication.ui.recyclerview.SaleTypesRecyclerViewAdapter
import com.example.remoteaccountingapplication.ui.viewmodel.SaleTypesFragmentViewModel
import com.example.remoteaccountingapplication.ui.viewmodel.SaleTypesFragmentViewModelFactory

class SaleTypesFragment : BaseFragment<FragmentSaleTypesBinding>(), OnMenuClickListener {

    private val viewModel: SaleTypesFragmentViewModel by viewModels {
        SaleTypesFragmentViewModelFactory(
            (activity?.application as RemoteAccountingApplication).repository
        )
    }

    private lateinit var adapter: SaleTypesRecyclerViewAdapter

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSaleTypesBinding {
        return FragmentSaleTypesBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSaleTypes().observe(this.viewLifecycleOwner) { saleTypesList ->
            if (binding.recyclerViewTypes.adapter == null) {
                adapter = SaleTypesRecyclerViewAdapter(saleTypesList, this)
                binding.recyclerViewTypes.adapter = adapter
            } else {
                (binding.recyclerViewTypes.adapter as SaleTypesRecyclerViewAdapter)
                    .updateSaleTypesList(saleTypesList)
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
                        DeleteAlertDialog.newInstance(4, itemId)
                            .show(parentFragmentManager, "DELETE")
                        true
                    }

                    R.id.edit_btn -> {
                        view
                            ?.findNavController()
                            ?.navigate(
                                SaleTypesFragmentDirections
                                    .actionSaleTypesFragmentToEditHandbookFragment(
                                        4,
                                        itemId,
                                        getString(R.string.sale_types_edit_dynamic_title)
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