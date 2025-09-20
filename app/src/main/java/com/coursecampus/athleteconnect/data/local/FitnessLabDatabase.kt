package com.coursecampus.athleteconnect.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.coursecampus.athleteconnect.data.local.converter.MapConverter
import com.coursecampus.athleteconnect.data.local.converter.StringListConverter
import com.coursecampus.athleteconnect.data.local.dao.*
import com.coursecampus.athleteconnect.data.local.entity.AchievementEntity
import com.coursecampus.athleteconnect.data.local.entity.AthleteEntity
import com.coursecampus.athleteconnect.data.local.entity.TestResultEntity
import com.coursecampus.athleteconnect.data.local.entity.FitnessTestEntity
import com.coursecampus.athleteconnect.data.local.entity.LeaderboardEntryEntity
import com.coursecampus.athleteconnect.data.local.entity.OpportunityEntity
import com.coursecampus.athleteconnect.data.local.entity.MessageEntity
import com.coursecampus.athleteconnect.data.local.entity.ConversationEntity

@Database(
    entities = [
        AthleteEntity::class,
        TestResultEntity::class,
        OpportunityEntity::class,
        MessageEntity::class,
        ConversationEntity::class,
        FitnessTestEntity::class,
        LeaderboardEntryEntity::class,
        AchievementEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(MapConverter::class, StringListConverter::class)
abstract class FitnessLabDatabase : RoomDatabase() {
    abstract fun athleteDao(): AthleteDao
    abstract fun testResultDao(): TestResultDao
    abstract fun opportunityDao(): OpportunityDao
    abstract fun messageDao(): MessageDao
    abstract fun conversationDao(): ConversationDao
    abstract fun fitnessTestDao(): FitnessTestDao
    abstract fun leaderboardDao(): LeaderboardDao
    abstract fun achievementDao(): AchievementDao

    companion object {
        @Volatile
        private var INSTANCE: FitnessLabDatabase? = null

        fun getDatabase(context: Context): FitnessLabDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FitnessLabDatabase::class.java,
                    "fitness_lab_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

