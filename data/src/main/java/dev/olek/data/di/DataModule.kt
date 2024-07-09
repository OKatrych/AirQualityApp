package dev.olek.data.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan("dev.olek.data") // Tell Koin to find all DI annotations in this package
class DataModule