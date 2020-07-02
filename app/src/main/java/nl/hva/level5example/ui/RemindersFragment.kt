package nl.hva.level5example.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_reminders.*
import nl.hva.level5example.R
import nl.hva.level5example.adapter.ReminderAdapter
import nl.hva.level5example.model.Reminder
import nl.hva.level5example.repositories.ReminderRepository
import nl.hva.level5example.vm.ReminderViewModel


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class RemindersFragment : Fragment() {

//    private lateinit var reminderRepository: ReminderRepository

    //populate some test data in Reminders list
    private var reminders: ArrayList<Reminder> = arrayListOf()

    private lateinit var reminderAdapter: ReminderAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val viewModel: ReminderViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reminders, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        reminderRepository = ReminderRepository(requireContext())
//        getRemindersFromDatabase()

        initRv()
        observeAddReminderResult()
    }


    private fun initRv() {

        reminderAdapter = ReminderAdapter(reminders)
        viewManager = LinearLayoutManager(activity)

        createItemTouchHelper().attachToRecyclerView(rvReminder)

        rvReminder.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = reminderAdapter
        }
    }

    private fun observeAddReminderResult() {
        viewModel.reminders.observe(viewLifecycleOwner, Observer { reminders ->
            this@RemindersFragment.reminders.clear()
            this@RemindersFragment.reminders.addAll(reminders)
            reminderAdapter.notifyDataSetChanged()
        })
    }


//        private fun observeAddReminderResult() {
//            setFragmentResultListener(REQ_REMINDER_KEY) { key, bundle ->
//                bundle.getString(BUNDLE_REMINDER_KEY)?.let {
//                    val reminder = Reminder(it)
//
//                    CoroutineScope(Dispatchers.Main).launch {
//                        withContext(Dispatchers.IO) {
//                            reminderRepository.insertReminder(reminder)
//                        }
//                        getRemindersFromDatabase()
//                    }
//                } ?: Log.e("ReminderFragment", "Request triggered, but empty reminder text!")
//
//            }
//        }

//    private fun getRemindersFromDatabase() {
//        CoroutineScope(Dispatchers.Main).launch {
//            val reminders = withContext(Dispatchers.IO) {
//                reminderRepository.getAllReminders()
//            }
//            this@MainActivity.reminders.clear()
//            this@MainActivity.reminders.addAll(reminders)
//            reminderAdapter.notifyDataSetChanged()
//        }
//    }


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
                val reminderToDelete = reminders[position]

//                CoroutineScope(Dispatchers.Main).launch {
//                    withContext(Dispatchers.IO) {
//                        reminderRepository.deleteReminder(reminderToDelete)
//                    }
//                }

                viewModel.deleteReminder(reminderToDelete)


            }
        }
        return ItemTouchHelper(callback)
    }


}
