package dev.olek.payback.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan("dev.olek.payback") // Tell Koin to find all DI annotations in this package
class AppModule