package nl.hva.level4example

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import nl.hva.level4example.dao.ReminderDao
import nl.hva.level4example.model.Reminder

@Database(entities = [Reminder::class], version = 1)
abstract class ReminderRoomDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao

    companion object {
        private const val DATABASE_NAME = "REMINDER_DATABASE"

        @Volatile
        private var reminderRoomDatabaseInstance: ReminderRoomDatabase? = null

        fun getDatabase(context: Context): ReminderRoomDatabase? {
            synchronized(ReminderRoomDatabase::class.java) {
                if (reminderRoomDatabaseInstance == null) {
                    reminderRoomDatabaseInstance = Room.databaseBuilder(
                            context.applicationContext,
                            ReminderRoomDatabase::class.java, DATABASE_NAME
                        )
                        .build()
                }
            }
            return reminderRoomDatabaseInstance
        }
    }

}