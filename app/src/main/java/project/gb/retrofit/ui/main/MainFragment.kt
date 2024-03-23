package project.gb.retrofit.ui.main

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import project.gb.retrofit.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private var _binding : FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() {
            return _binding!!
        }
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.user.collect {
                if (it != null) {
                    imageUser(it, view)
                }
            }
        }
    }

    private fun imageUser(usersModels: ViewUserData, view: View) {
        val imageView = usersModels.results.first().picture.large
        Glide
            .with(view)
            .load(imageView)
            .into(binding.imageView)
    }
}