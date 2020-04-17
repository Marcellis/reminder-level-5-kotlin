package nl.hva.level5example.repositories

import android.content.Context
import nl.hva.level5example.ReminderRoomDatabase
import nl.hva.level5example.dao.ReminderDao
import nl.hva.level5example.model.Reminder

class ReminderRepository(context: Context) {
    private var reminderDao: ReminderDao?

    init {
        val reminderRoomDatabase = ReminderRoomDatabase.getReminderRoomDatabase(context)
        reminderDao = reminderRoomDatabase?.reminderDao()
    }

    suspend fun getAllReminders() : List<Reminder> {
        return reminderDao?.getAllReminders() ?: emptyList()
    }

    suspend fun insertReminder(reminder: Reminder) {
        reminderDao?.insertReminder(reminder)
    }

    suspend fun deleteReminder(reminder: Reminder) {
        reminderDao?.deleteReminder(reminder)
    }

    suspend fun updateReminder(reminder: Reminder) {
        reminderDao?.updateReminder(reminder)
    }

}