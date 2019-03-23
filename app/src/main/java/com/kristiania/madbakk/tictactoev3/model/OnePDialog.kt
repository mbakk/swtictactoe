package com.kristiania.madbakk.tictactoev3.model



import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.Toast
import com.kristiania.madbakk.tictactoev3.R
import kotlinx.android.synthetic.main.dialog_oneplayer.*
import kotlinx.android.synthetic.main.dialog_oneplayer.view.*


class OnePDialog: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_oneplayer,null)

        builder.setView(view)

        view.btn_start.setOnClickListener {
            val text = view.et_player.text
            if(text.isNotEmpty()){

                val myIntent = Intent(activity,MainActivity::class.java)
                startActivity(myIntent)
                this.dismiss()
            }else{
                Toast.makeText(activity,"Enter name to start the game!", Toast.LENGTH_SHORT).show()
            }
        }

        val dialog  = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))

        return dialog
    }

}

