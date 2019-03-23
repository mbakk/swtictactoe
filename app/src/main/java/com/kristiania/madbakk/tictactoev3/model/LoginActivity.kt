package com.kristiania.madbakk.tictactoev3.model

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.kristiania.madbakk.tictactoev3.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val manager = supportFragmentManager
        val ft = manager.beginTransaction()
        ft.replace(R.id.fragment_container,LoginFragment())
        ft.commit()
    }
}