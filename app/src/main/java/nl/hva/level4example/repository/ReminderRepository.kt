package nl.hva.level4example.repository

import android.content.Context
import nl.hva.level4example.ReminderRoomDatabase
import nl.hva.level4example.dao.ReminderDao
import nl.hva.level4example.model.Reminder

class ReminderRepository(context: Context) {
    private var reminderDao: ReminderDao?

    init {
        val reminderRoomDatabase = ReminderRoomDatabase.getDatabase(context)
        reminderDao = reminderRoomDatabase?.reminderDao()
    }

    suspend fun getAllReminders(): List<Reminder> {
        return reminderDao?.getAllReminders() ?: run {
            return emptyList()
        }
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