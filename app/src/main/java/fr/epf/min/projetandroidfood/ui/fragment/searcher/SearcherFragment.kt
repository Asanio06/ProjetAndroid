package fr.epf.min.projetandroidfood.ui.fragment.searcher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import fr.epf.min.projetandroidfood.databinding.FragmentSearcherBinding

class SearcherFragment : Fragment() {

    private lateinit var searcherViewModel: SearcherViewModel
    private var _binding: FragmentSearcherBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searcherViewModel =
            ViewModelProvider(this).get(SearcherViewModel::class.java)

        _binding = FragmentSearcherBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSearcher
        searcherViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}