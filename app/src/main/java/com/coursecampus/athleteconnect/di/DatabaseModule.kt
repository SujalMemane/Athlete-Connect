package com.coursecampus.athleteconnect.di

import android.content.Context
import androidx.room.Room
import com.coursecampus.athleteconnect.data.local.FitnessLabDatabase
import com.coursecampus.athleteconnect.data.local.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideFitnessLabDatabase(@ApplicationContext context: Context): FitnessLabDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            FitnessLabDatabase::class.java,
            "fitness_lab_database"
        ).build()
    }

    @Provides
    fun provideAthleteDao(database: FitnessLabDatabase): AthleteDao {
        return database.athleteDao()
    }

    @Provides
    fun provideTestResultDao(database: FitnessLabDatabase): TestResultDao {
        return database.testResultDao()
    }

    @Provides
    fun provideOpportunityDao(database: FitnessLabDatabase): OpportunityDao {
        return database.opportunityDao()
    }

    @Provides
    fun provideMessageDao(database: FitnessLabDatabase): MessageDao {
        return database.messageDao()
    }

    @Provides
    fun provideConversationDao(database: FitnessLabDatabase): ConversationDao {
        return database.conversationDao()
    }

    @Provides
    fun provideFitnessTestDao(database: FitnessLabDatabase): FitnessTestDao {
        return database.fitnessTestDao()
    }

    @Provides
    fun provideLeaderboardDao(database: FitnessLabDatabase): LeaderboardDao {
        return database.leaderboardDao()
    }

    @Provides
    fun provideAchievementDao(database: FitnessLabDatabase): AchievementDao {
        return database.achievementDao()
    }
}

