package nl.hva.level5example

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.hva.level5example.adapter.ReminderAdapter
import nl.hva.level5example.model.Reminder
import nl.hva.level5example.repositories.ReminderRepository

const val ADD_REMINDER_REQUEST_CODE = 100
const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var reminders: ArrayList<Reminder>
    private lateinit var recyclerView: RecyclerView
    private lateinit var reminderAdapter: ReminderAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var reminderRepository: ReminderRepository
    private var count: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        countBtn.setOnClickListener {
            count++
            countTxt.text = String.format("Days in  isolation: %d" , count)
        }

        recyclerView = findViewById(R.id.rvReminder)

        reminderRepository = ReminderRepository(this)

        reminders = arrayListOf()

        reminderAdapter = ReminderAdapter(reminders)
        viewManager = LinearLayoutManager(this)
        createItemTouchHelper().attachToRecyclerView(recyclerView)

        getRemindersFromDatabase()

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = reminderAdapter
        }

        fab.setOnClickListener {
            startAddActivity()
        }
    }

    private fun getRemindersFromDatabase() {
        CoroutineScope(Dispatchers.Main).launch {
            val reminders = withContext(Dispatchers.IO) {
                reminderRepository.getAllReminders()
            }
            this@MainActivity.reminders.clear()
            this@MainActivity.reminders.addAll(reminders)
            reminderAdapter.notifyDataSetChanged()
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
                            CoroutineScope(Dispatchers.Main).launch {
                                withContext(Dispatchers.IO) {
                                    reminderRepository.insertReminder(safeReminder)
                                }
                                getRemindersFromDatabase()
                            }
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

    /**
     * Create a touch helper to recognize when a user swipes an item from a recycler view.
     * An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
     * and uses callbacks to signal when a user is performing these actions.
     */
    private fun createItemTouchHelper(): ItemTouchHelper {

        // Callback which is used to create the ItemTouch helper. Only enables left swipe.
        // Use ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) to also enable right swipe.
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // Enables or Disables the ability to move items up and down.
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            // Callback triggered when a user swiped an item.
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
//
                val reminderToDelete = reminders[position]


                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                        reminderRepository.deleteReminder(reminderToDelete)
                    }
                    getRemindersFromDatabase()
                }


            }
        }
        return ItemTouchHelper(callback)
    }

}
