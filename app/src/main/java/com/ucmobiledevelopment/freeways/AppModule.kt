package com.ucmobiledevelopment.freeways

import com.ucmobiledevelopment.freeways.service.IIncidentService
import com.ucmobiledevelopment.freeways.service.IncidentService
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@JvmField
val AppModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { ApplicationViewModel(androidApplication())}
    single<IIncidentService> { IncidentService() }
}
