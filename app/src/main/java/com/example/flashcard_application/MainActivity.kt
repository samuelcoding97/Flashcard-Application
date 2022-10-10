package com.example.flashcard_application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import android.util.Log

class MainActivity : AppCompatActivity() {


    lateinit var flashcardDatabase: FlashcardDatabase
    var allFlashcards = mutableListOf<Flashcard>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        flashcardDatabase = FlashcardDatabase(this)
        allFlashcards = flashcardDatabase.getAllCards().toMutableList()

        if (allFlashcards.size > 0) {
            findViewById<TextView>(R.id.question).text = allFlashcards[0].question
            findViewById<TextView>(R.id.answer).text = allFlashcards[0].answer
        }

        val flashcardQuestion = findViewById<TextView>(R.id.question)
        val flashcardAnswer = findViewById<TextView>(R.id.answer)

        flashcardQuestion.setOnClickListener {
            flashcardQuestion.visibility = View.INVISIBLE
            flashcardAnswer.visibility = View.VISIBLE
        }

        flashcardAnswer.setOnClickListener  {
            flashcardQuestion.visibility = View.VISIBLE
            flashcardAnswer.visibility = View.INVISIBLE
        }

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data: Intent? = result.data
            val extras = data?.extras

            if (extras != null) { // Check that we have data returned
                val question = extras.getString("QUESTION_KEY")
                val answer = extras.getString("ANSWER_KEY")

                // Log the value of the strings for easier debugging
                Log.i("MainActivity", "question: $question")
                Log.i("MainActivity", "answer: $answer")

                // Display newly created flashcard
                findViewById<TextView>(R.id.question).text = question
                findViewById<TextView>(R.id.answer).text = answer

                // Save newly created flashcard to database
                if (question != null && answer != null) {
                    flashcardDatabase.insertCard(Flashcard(question, answer))
                    // Update set of flashcards to include new card
                    allFlashcards = flashcardDatabase.getAllCards().toMutableList()
                } else {
                    Log.e("TAG", "Missing question or answer to input into database. Question is $question and answer is $answer")
                }
            } else {
                Log.i("MainActivity", "Returned null data from AddCardActivity")
            }


            var currentCardDisplayedIndex = 0
            val nextButton = findViewById<ImageView>(R.id.next_button)
            nextButton.setOnClickListener {
            // don't try to go to next card if you have no cards to begin with
            if (allFlashcards.size == 0) {
                // return here, so that the rest of the code in this onClickListener doesn't execute
                return@setOnClickListener
            }

            // advance our pointer index so we can show the next card
            currentCardDisplayedIndex++

            // make sure we don't get an IndexOutOfBoundsError if we are viewing the last indexed card in our list
            if(currentCardDisplayedIndex >= allFlashcards.size) {
                Snackbar.make(
                    findViewById<TextView>(R.id.question), // This should be the TextView for displaying your flashcard question
                    "You've reached the end of the cards, going back to start.",
                    Snackbar.LENGTH_SHORT)
                    .show()
                currentCardDisplayedIndex = 0
            }

            // set the question and answer TextViews with data from the database
            allFlashcards = flashcardDatabase.getAllCards().toMutableList()
            val (question, answer) = allFlashcards[currentCardDisplayedIndex]

            findViewById<TextView>(R.id.answer).text = answer
            findViewById<TextView>(R.id.question).text = question
        }

        Snackbar.make(flashcardQuestion, "Question Button was clicked", Snackbar.LENGTH_SHORT).show()

        }

        val addQuestionButton = findViewById<ImageView>(R.id.plus_sign)
        addQuestionButton.setOnClickListener{
            val intent = Intent( this, AddCardActivity::class.java)
            resultLauncher.launch(intent)
        }
    }

}
