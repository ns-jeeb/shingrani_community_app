package com.locked.shingranicommunity.di

import dagger.Module

@Module(subcomponents = [RegisterComponent::class, LoginComponent::class,UserComponent::class,DashboardComponent::class])
class CommAppSubComponent {
}