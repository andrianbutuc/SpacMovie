package com.example.projet.connexion

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.projet.HomeActivity
import com.example.projet.R
import com.example.projet.databinding.FragmentLogInBinding

class LogInFragment : Fragment() {
    private lateinit var binding: FragmentLogInBinding

    private lateinit var viewModel: LogInViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_log_in,
            container,
            false
        )

        viewModel = ViewModelProvider(this).get(LogInViewModel::class.java)


        binding.logInViewModel = viewModel

        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.connected.observe(viewLifecycleOwner, { connected ->
            if (connected == true) {
                val intent = Intent(activity, HomeActivity::class.java)
                startActivity(intent)
                activity?.finish()
                viewModel.logInUserComplete()
            } else if (connected == false) {
                Toast.makeText(context, R.string.failed_log_in, Toast.LENGTH_SHORT).show()
                viewModel.logInUserComplete()

            }
        })


        return binding.root
    }
}