package com.locked.shingranicommunity.members

import dagger.Binds
import dagger.Module

@Module
abstract class MemberModule {
    @Binds
    abstract fun memberModuleProvider(memberApiRequest: MemberApiRequestt): MemberApiRequestListener
}