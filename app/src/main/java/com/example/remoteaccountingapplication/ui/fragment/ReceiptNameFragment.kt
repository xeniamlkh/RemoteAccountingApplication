package com.example.remoteaccountingapplication.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.remoteaccountingapplication.R
import com.example.remoteaccountingapplication.RemoteAccountingApplication
import com.example.remoteaccountingapplication.databinding.FragmentReceiptNameBinding
import com.example.remoteaccountingapplication.ui.viewmodel.ReceiptNameFragmentViewModel
import com.example.remoteaccountingapplication.ui.viewmodel.ReceiptNameFragmentViewModelFactory

class ReceiptNameFragment : BaseFragment<FragmentReceiptNameBinding>() {

    private val viewModel: ReceiptNameFragmentViewModel by viewModels {
        ReceiptNameFragmentViewModelFactory(
            (activity?.application as RemoteAccountingApplication).repository
        )
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentReceiptNameBinding {
        return FragmentReceiptNameBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getNames().observe(this.viewLifecycleOwner) { namesList ->
            val operatorNames = namesList.map { it.name }
            val autoCompleteAdapter: ArrayAdapter<String> =
                ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, operatorNames)
            binding.nameText.setAdapter(autoCompleteAdapter)
        }

        binding.furtherBtn.setOnClickListener {
            val name = binding.nameText.text.toString()

            if (name.isEmpty()) {
                binding.nameLayout.error = getString(R.string.can_not_be_empty)
                binding.nameLayout.isErrorEnabled = true
            } else {
                binding.nameLayout.isErrorEnabled = false
            }

            view.findNavController().navigate(
                ReceiptNameFragmentDirections.actionReceiptNameFragmentToReceiptFragment(name)
            )
        }
    }
}