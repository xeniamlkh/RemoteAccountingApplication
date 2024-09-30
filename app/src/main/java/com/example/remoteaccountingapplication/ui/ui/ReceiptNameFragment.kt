package com.example.remoteaccountingapplication.ui.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.remoteaccountingapplication.R
import com.example.remoteaccountingapplication.RemoteAccountingApplication
import com.example.remoteaccountingapplication.databinding.FragmentReceiptNameBinding
import com.example.remoteaccountingapplication.ui.viewmodel.RemoteAccountingViewModel
import com.example.remoteaccountingapplication.ui.viewmodel.RemoteAccountingViewModelFactory

class ReceiptNameFragment : Fragment() {

    private var _binding: FragmentReceiptNameBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RemoteAccountingViewModel by activityViewModels {
        RemoteAccountingViewModelFactory(
            (activity?.application as RemoteAccountingApplication).repository,
            activity?.application as RemoteAccountingApplication
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReceiptNameBinding.inflate(inflater, container, false)
        return binding.root
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}