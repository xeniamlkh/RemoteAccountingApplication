package com.example.remoteaccountingapplication.ui.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.remoteaccountingapplication.R
import com.example.remoteaccountingapplication.RemoteAccountingApplication
import com.example.remoteaccountingapplication.databinding.FragmentBackupBinding
import com.example.remoteaccountingapplication.domain.Backup
import com.example.remoteaccountingapplication.ui.viewmodel.BackupViewModel
import com.example.remoteaccountingapplication.ui.viewmodel.BackupViewModelFactory
import com.google.android.material.snackbar.Snackbar

class BackupFragment : Fragment() {

    private var _binding: FragmentBackupBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BackupViewModel by viewModels {
        BackupViewModelFactory((activity?.application as RemoteAccountingApplication).repository)
    }

    private lateinit var backup: Backup

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBackupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backup = Backup(viewModel, this)

        binding.backupBtn.setOnClickListener {
            backup.createBackUp()

            binding.salesCheck.visibility = View.VISIBLE
            binding.productsCheck.visibility = View.VISIBLE
            binding.paymentTypesCheck.visibility = View.VISIBLE
            binding.saleTypesCheck.visibility = View.VISIBLE
            binding.namesCheck.visibility = View.VISIBLE
            binding.receiptCheck.visibility = View.VISIBLE

            Snackbar
                .make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.db_backup_created),
                    Snackbar.LENGTH_SHORT
                )
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}