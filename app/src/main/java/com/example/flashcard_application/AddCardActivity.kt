package com.example.flashcard_application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView

class AddCardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)

        val questionEditText = findViewById<EditText>(R.id.question_field)
        val answerEditText = findViewById<EditText>(R.id.answer_field)



        val saveButton = findViewById<ImageView>(R.id.save_card)
        saveButton.setOnClickListener {
            val questionString = questionEditText.text.toString()
            val answerString = answerEditText.text.toString()
            val data = Intent()
            data.putExtra("QUESTION_KEY", questionString)
            data.putExtra("ANSWER_KEY", answerString)

            setResult(RESULT_OK, data)
            finish()

        }

        val cancelButton = findViewById<ImageView>(R.id.cancel_button)
        cancelButton.setOnClickListener {
            finish()
        }
    }


}