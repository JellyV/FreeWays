package com.ucmobiledevelopment.freeways

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@JvmField
val AppModule = module {
    viewModel { MainViewModel() }
}
