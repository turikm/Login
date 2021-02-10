package com.example.puzzle2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.example.registrationandlogin.Retrofit.INodeJS
import com.rengwuxian.materialedittext.MaterialEditText
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity()
{
    lateinit var myAPI:INodeJS
    var compositionDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        login_button.setOnClickListener{
            login(edt_email.text.toString(),edt_password.text.toString())
        }

        register_button.setOnClickListener{
            register(edt_email.text.toString(),edt_password.text.toString())
        }
    }

    private fun register(email: String, password: String) {

        val enter_name_view = LayoutInflater.from(this@MainActivity)
                .inflate(R.layout.enter_name_layout,null)
        MaterialStyleDialog.Builder(this@MainActivity)
                .setTitle("Register")
                .setDescription("One more step")
                .setCustomView(enter_name_view)
                .setIcon(R.drawable.ic_user)
                .setNegativeText("Cancel")
           .onNegative { dialog, which -> dialog.dismiss() }
           .setPositiveText("Register")
           .onPositive { dialog,which->

               var edt_name =enter_name_view.findViewById<View>(R.id.edt_name) as MaterialEditText

               compositionDisposable.add(myAPI.registerUser(email,edt_name.text.toString(),password)
                   .subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .subscribe{message ->
                       if(message.contains("encrypted_password"))
                           Toast.makeText(this@MainActivity,"Login succes",Toast.LENGTH_SHORT).show()
                       else
                           Toast.makeText(this@MainActivity,message,Toast.LENGTH_SHORT).show()

                  })
           }
    }

    private fun login(email: String, password: String) {
        compositionDisposable.add(myAPI.loginUser(email,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{message ->
                    if(message.contains("encrypted_password"))
                    {
                        Toast.makeText(this@MainActivity,"Login Succesc",Toast.LENGTH_SHORT).show()
                    }
                    else
                    {
                        Toast.makeText(this@MainActivity,message,Toast.LENGTH_SHORT).show()
                    }
                } )
    }

    override fun onStop()
    {
        compositionDisposable.clear()
        super.onStop()
    }

    override fun onDestroy()
    {
        compositionDisposable.clear()
        super.onDestroy()
    }
}