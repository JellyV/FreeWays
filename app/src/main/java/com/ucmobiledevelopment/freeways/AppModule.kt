package com.ucmobiledevelopment.freeways

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@JvmField
val AppModule = module {
    viewModel { MainViewModel() }
    //TO DO: single<IIncidentService> { IncidentService }
    //TO DO: add "get()" as the parameter in the MainViewModel in line 8
}
