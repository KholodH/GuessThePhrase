package com.example.guessthephrase

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var clMain: ConstraintLayout
    private lateinit var userguess: EditText
    private lateinit var guessbtn: Button
    private lateinit var phrase: TextView
    private lateinit var messages: ArrayList<String>
    private  var count:Int = 0
    private var text= "I like the rain"
    private var MapForStras= mutableMapOf<Int, Char>()
    private var stars=""
    private var guessPhrase = true
    private var guessedLetters = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
            for (i in text.indices){
                if (text[i]!=' '){
                    MapForStras[i]=' '
                    stars +=' '
                }
                else{
                    MapForStras[i]='*'
                    stars +='*'
                }
            }
        clMain = findViewById(R.id.clMain)
        messages = ArrayList()

        rvRecord.adapter = MessageAdapter(this, messages)
        rvRecord.layoutManager = LinearLayoutManager(this)

        userguess = findViewById(R.id.etAdd)
        guessbtn = findViewById(R.id.btnAdd)
        phrase = findViewById(R.id.tvAccount)
        guessbtn.setOnClickListener { guesses() }

        updateText()

    }

    private fun guesses(){
        val msg = userguess.text.toString()

        if(guessPhrase){
            if(msg == text){
                disableEntry()
                showAlertDialog("You win!\n\nPlay again?")
            }else{
                messages.add("Wrong guess: $msg")
                guessPhrase = false
                updateText()
            }
        }else{
            if(msg.isNotEmpty() && msg.length==1){
                stars = ""
                guessPhrase = true
                checkLetters(msg[0])
            }else{
                Snackbar.make(clMain, "Please enter one letter only", Snackbar.LENGTH_LONG).show()
            }
        }

        userguess.text.clear()
        guessbtn.clearFocus()
        rvRecord.adapter?.notifyDataSetChanged()
    }

    private fun disableEntry(){
        guessbtn.isEnabled = false
        userguess.isClickable = false
        userguess.isEnabled = false
        userguess.isClickable = false
    }

    private fun updateText(){
        tvAccount.text = "Phrase:  " + stars.toUpperCase()
        tvLetter.text = "Guessed Letters:  " + guessedLetters
        if(guessPhrase){
            userguess.hint = "Guess the full phrase"
        }else{
            userguess.hint = "Guess a one letter"
        }
    }

    private fun checkLetters(guessedLetter: Char){
        var found = 0
        for(i in text.indices){
            if(text[i] == guessedLetter){
                MapForStras[i] = guessedLetter
                found++
            }
        }
        for(i in MapForStras){stars += MapForStras[i.key]}
        if(stars==text){
            disableEntry()
            showAlertDialog("You win!\n\nPlay again?")
        }
        if(guessedLetters.isEmpty()){guessedLetters+=guessedLetter}else{guessedLetters+=", "+guessedLetter}
        if(found>0){
            messages.add("Found $found ${guessedLetter.toUpperCase()}(s)")
        }else{
            messages.add("No ${guessedLetter.toUpperCase()}s found")
        }
        count++
        val guessesLeft = 10 - count
        if(count<10){messages.add("$guessesLeft guesses remaining")}
        updateText()
        rvRecord.scrollToPosition(messages.size - 1)
    }

    private fun showAlertDialog(title: String) {
        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setMessage(title)
            .setCancelable(false)
            .setPositiveButton("Yes", DialogInterface.OnClickListener {
                    dialog, id -> this.recreate()
            })
            .setNegativeButton("No", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })

        val alert = dialogBuilder.create()
        alert.setTitle("Game Over")
        alert.show()
    }

}