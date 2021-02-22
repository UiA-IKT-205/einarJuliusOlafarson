package no.uia.ikt205.piano

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_piano_layout.view.*
import no.uia.ikt205.piano.databinding.FragmentPianoLayoutBinding

class PianoLayout : Fragment() {


    private var _binding:FragmentPianoLayoutBinding ? = null
    private val binding get() = _binding!!
    private val whitekeys = listOf("C", "D", "E", "F", "G", "A", "B", "C2", "D2", "E2", "F2", "G2")
    private val blackkeys = listOf("C#", "D#", "F#", "G#", "A#", "C2#", "D2#", "F2#", "G2#")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPianoLayoutBinding.inflate(layoutInflater)
        val view = binding.root

        val fragmentmanager = childFragmentManager
        var fragmentTransaction = fragmentmanager.beginTransaction()

        whitekeys.forEach{
            val whitePianoKey = WhiteKeysFragment.newInstance(it)

            whitePianoKey.onKeyDown = {
                println("White piano key down $it")
            }

            whitePianoKey.onKeyUp = {
                println("White piano key up $it")
            }

            fragmentTransaction.add(view.whiteKeysLayout.id, whitePianoKey, "note_$it")
        }

        blackkeys.forEach{
            val blackPianoKey = blackKeysFragment.newInstance(it)

            blackPianoKey.onKeyDown = {
                println("Black piano key down $it")
            }

            blackPianoKey.onKeyUp = {
                println("Black piano key up $it")
            }

            fragmentTransaction.add(view.blackKeysLayout.id, blackPianoKey, "note_$it")
        }

        fragmentTransaction.commit()
        // Inflate the layout for this fragment
        return view
    }
}