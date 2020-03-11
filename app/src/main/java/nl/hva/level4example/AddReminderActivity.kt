package nl.hva.level4example

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_add_reminder.*
import kotlinx.android.synthetic.main.content_add_reminder.*
import nl.hva.level4example.model.Reminder

const val EXTRA_REMINDER = "EXTRA_REMINDER"

class AddReminderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_reminder)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fab.setOnClickListener { onAddReminder() }
    }

    private fun onAddReminder() {
        //verzamelen van textinputlayout gegevens
        val reminderText = tilReminderText.text.toString()

        if(reminderText.isNotBlank()) {
            //maken van model
            val reminder = Reminder(reminderText);

            //terugsturen naar MainActivity
            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_REMINDER, reminder)
            setResult(Activity.RESULT_OK, resultIntent)

            finish()
        } else {
            Toast.makeText(this, R.string.not_valid_reminder, Toast.LENGTH_SHORT).show();
        }

    }

}
