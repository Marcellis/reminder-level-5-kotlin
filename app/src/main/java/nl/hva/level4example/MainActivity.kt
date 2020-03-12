package nl.hva.level4example

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_main.*
import nl.hva.level4example.adapter.ReminderAdapter
import nl.hva.level4example.model.Reminder

const val ADD_REMINDER_REQUEST_CODE = 100
const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var reminders: ArrayList<Reminder>
    private lateinit var recyclerView: RecyclerView
    private lateinit var reminderAdapter: ReminderAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        recyclerView = findViewById(R.id.rvReminder)

//        populate some testData in Reminders
        reminders = arrayListOf(
            Reminder("Milk cows"),
            Reminder("Wash horses"),
            Reminder("Feed squirrels")
        )

        reminderAdapter = ReminderAdapter(reminders)
        viewManager = LinearLayoutManager(this)

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = reminderAdapter
        }

        fab.setOnClickListener {
            startAddActivity()
        }
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                ADD_REMINDER_REQUEST_CODE -> {
                    data?.let {safeData ->
                        val reminder = safeData.getParcelableExtra<Reminder>(EXTRA_REMINDER)
                        reminder?.let { safeReminder ->
                            reminders.add(safeReminder)
                            reminderAdapter.notifyDataSetChanged()
                        } ?: run {
                            Log.e(TAG, "reminder is null")
                        }
                    } ?: run {
                        Log.e(TAG, "empty intent data received")
                    }

                }
            }
        }
    }

    private fun startAddActivity() {
        val intent = Intent(this, AddReminderActivity::class.java)
        startActivityForResult(intent, ADD_REMINDER_REQUEST_CODE)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
