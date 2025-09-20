package com.coursecampus.athleteconnect.di

import com.coursecampus.athleteconnect.data.repository.AthleteRepositoryImpl
import com.coursecampus.athleteconnect.domain.repository.AthleteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAthleteRepository(
        athleteRepositoryImpl: AthleteRepositoryImpl
    ): AthleteRepository
}

