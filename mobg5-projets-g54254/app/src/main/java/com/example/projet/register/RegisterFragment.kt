package com.example.projet.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.projet.R
import com.example.projet.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding

    private lateinit var viewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_register,
            container,
            false
        )

        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        binding.registerViewModel = viewModel

        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.registered.observe(viewLifecycleOwner, { registered ->
            if (registered == true) {
                Toast.makeText(context, R.string.sign_up_succeed, Toast.LENGTH_LONG).show()
                viewModel.registerUserCompleted()
                //maybe navigate to log in page
            } else if (registered == false) {
                Toast.makeText(context, R.string.sign_up_failed, Toast.LENGTH_LONG).show()
                viewModel.registerUserCompleted()
            }
        })

        return binding.root
    }

}