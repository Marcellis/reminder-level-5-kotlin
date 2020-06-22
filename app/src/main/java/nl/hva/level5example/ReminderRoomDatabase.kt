package nl.hva.level5example

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import nl.hva.level5example.dao.ReminderDao
import nl.hva.level5example.model.Reminder

@Database(entities = [Reminder::class], version = 1, exportSchema = false)
abstract class ReminderRoomDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao

    companion object {
        private const val DB_NAME = "reminder_DB"

        @Volatile
        private var reminderRoomDatabase : ReminderRoomDatabase? = null

        fun getReminderRoomDatabase(context: Context) : ReminderRoomDatabase? {
            if(reminderRoomDatabase != null) return reminderRoomDatabase

            synchronized(ReminderRoomDatabase::class.java) {
                if(reminderRoomDatabase == null) {
                    reminderRoomDatabase = Room.databaseBuilder(
                            context.applicationContext,
                            ReminderRoomDatabase::class.java, DB_NAME
                        )
                        .build()

                }
            }

            return reminderRoomDatabase
        }
    }
}