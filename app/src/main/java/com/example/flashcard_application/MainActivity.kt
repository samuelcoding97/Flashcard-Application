package com.example.flashcard_application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val flashcardQuestion = findViewById<TextView>(R.id.flashcard_question)
        val flashcardAnswer = findViewById<TextView>(R.id.flashcard_answer)

        flashcardQuestion.setOnClickListener {
            flashcardQuestion.visibility = View.INVISIBLE
            flashcardAnswer.visibility = View.VISIBLE
        }

        Snackbar.make(flashcardQuestion, "Question Button was clicked", Snackbar.LENGTH_SHORT).show()


        val resultLauncher =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {
            result ->

            val data: Intent? = result.data

            if (data!= null) {
                val questionString = data.getStringExtra("QUESTION_KEY")
                val answerString = data.getStringExtra("ANSWER_KEY")

                flashcardQuestion.text = questionString
                flashcardAnswer.text = answerString
            }
        }

        val addQuestionButton = findViewById<ImageView>(R.id.plus_sign)
        addQuestionButton.setOnClickListener{
            val intent = Intent( this, AddCardActivity::class.java)
            resultLauncher.launch(intent)
        }
    }
}
