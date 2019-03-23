package com.kristiania.madbakk.tictactoev3.model


import android.media.MediaPlayer
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kristiania.madbakk.tictactoev3.R
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment() {

    private lateinit var mp : MediaPlayer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mp = MediaPlayer.create(context, R.raw.swtheme)

        iv_sound.setOnClickListener {
            if(mp.isPlaying){
                iv_sound.setImageResource(R.drawable.mute)
                mp.pause()
            }else{
                iv_sound.setImageResource(R.drawable.speaker)
                mp.start()
            }

        }

        btn_lb.setOnClickListener {
            val manager = fragmentManager
            val ft = manager!!.beginTransaction()
            ft.replace(R.id.fragment_container,LeaderboardFragment())
            ft.addToBackStack(null)
            ft.commit()
        }

        btn_1p.setOnClickListener {
            showDialog(OnePDialog())
        }

        btn_2p.setOnClickListener {
            showDialog(TwoPDialog())
        }
    }

    private fun showDialog(df: DialogFragment){
        df.show(fragmentManager,"dialog")
    }

    override fun onStop() {
        super.onStop()
        mp.stop()
    }

    override fun onStart() {
        super.onStart()

        mp = MediaPlayer.create(context, R.raw.swtheme)
        mp.start()
        mp.setOnCompletionListener {
            mp.start()
        }
    }
}
