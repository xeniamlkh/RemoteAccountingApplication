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
import com.example.remoteaccountingapplication.databinding.FragmentPaymentTypesBinding
import com.example.remoteaccountingapplication.ui.alertdialogs.DeleteAlertDialog
import com.example.remoteaccountingapplication.ui.fragment.BaseFragment
import com.example.remoteaccountingapplication.ui.recyclerview.OnMenuClickListener
import com.example.remoteaccountingapplication.ui.recyclerview.PaymentTypesRecyclerViewAdapter
import com.example.remoteaccountingapplication.ui.viewmodel.BaseViewModel
import com.example.remoteaccountingapplication.ui.viewmodel.BaseViewModelFactory

class PaymentTypesFragment : BaseFragment<FragmentPaymentTypesBinding>(), OnMenuClickListener {

    private val viewModel: BaseViewModel by viewModels {
        BaseViewModelFactory(
            (activity?.application as RemoteAccountingApplication).repository
        )
    }

    private lateinit var adapter: PaymentTypesRecyclerViewAdapter

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPaymentTypesBinding {
        return FragmentPaymentTypesBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getPaymentTypes().observe(this.viewLifecycleOwner) { typesList ->
            if (binding.recyclerViewTypes.adapter == null) {
                adapter = PaymentTypesRecyclerViewAdapter(typesList, this)
                binding.recyclerViewTypes.adapter = adapter
            } else {
                (binding.recyclerViewTypes.adapter as PaymentTypesRecyclerViewAdapter)
                    .updatePaymentTypesList(typesList)
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
                        DeleteAlertDialog.newInstance(3, itemId)
                            .show(parentFragmentManager, "DELETE")
                        true
                    }

                    R.id.edit_btn -> {
                        view
                            ?.findNavController()
                            ?.navigate(
                                PaymentTypesFragmentDirections
                                    .actionPaymentTypesFragmentToEditHandbookFragment(
                                        3,
                                        itemId,
                                        getString(R.string.payment_types_edit_dynamic_title)
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