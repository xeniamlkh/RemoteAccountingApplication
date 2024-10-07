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
import com.example.remoteaccountingapplication.databinding.FragmentNamesBinding
import com.example.remoteaccountingapplication.ui.alertdialogs.DeleteAlertDialog
import com.example.remoteaccountingapplication.ui.fragment.BaseFragment
import com.example.remoteaccountingapplication.ui.recyclerview.NamesRecyclerViewAdapter
import com.example.remoteaccountingapplication.ui.recyclerview.OnMenuClickListener
import com.example.remoteaccountingapplication.ui.viewmodel.NamesFragmentViewModel
import com.example.remoteaccountingapplication.ui.viewmodel.NamesFragmentViewModelFactory

class NamesFragment : BaseFragment<FragmentNamesBinding>(), OnMenuClickListener {

    private val viewModel: NamesFragmentViewModel by viewModels {
        NamesFragmentViewModelFactory(
            (activity?.application as RemoteAccountingApplication).repository
        )
    }

    private lateinit var adapter: NamesRecyclerViewAdapter

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNamesBinding {
        return FragmentNamesBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getNames().observe(this.viewLifecycleOwner) { namesList ->
            if (binding.recyclerViewNames.adapter == null) {
                adapter = NamesRecyclerViewAdapter(namesList, this)
                binding.recyclerViewNames.adapter = adapter
            } else {
                (binding.recyclerViewNames.adapter as NamesRecyclerViewAdapter)
                    .updateNamesList(namesList)
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
                        DeleteAlertDialog.newInstance(5, itemId)
                            .show(parentFragmentManager, "DELETE")
                        true
                    }

                    R.id.edit_btn -> {
                        view
                            ?.findNavController()
                            ?.navigate(
                                NamesFragmentDirections
                                    .actionNamesFragmentToEditHandbookFragment(
                                        5,
                                        itemId,
                                        getString(R.string.name_edit_dynamic_title)
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