package com.frank.hilt

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import javax.inject.Inject

@Module
@InstallIn(FragmentComponent::class)
class RequestBuilderDI @Inject constructor() {

//    @Provides
//    fun getRequestBuilder(): RequestBuilder {
//        return RequestBuilder()
//    }

}
