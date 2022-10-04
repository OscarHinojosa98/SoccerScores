package com.example.gameday

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.http.*

interface FixtureService {
    @Headers("X-RapidAPI-Key:" + "7a3c7f6ae9msh8d8abb68de23261p1f3bc5jsn864e4d11ff31")
    @GET("/v3/fixtures?date=2022-08-07&league=39&season=2022")
    suspend fun getFixtures() :Response<Fixtures>

    @Headers("X-RapidAPI-Key:" + "7a3c7f6ae9msh8d8abb68de23261p1f3bc5jsn864e4d11ff31")
    @GET("/v3/fixtures")
    suspend fun getWeekFixtures(@Query(value = "league") league:String,
                                @Query(value = "season") season:String,
                                @Query(value = "from") from:String,
                                @Query(value = "to") to:String): Response<Fixtures>
}