package dev.jlpt.daily

import android.app.Application
import android.content.Context
import dev.zacsweers.metro.*
import soil.query.*
import soil.query.annotation.ExperimentalSoilQueryApi

@DependencyGraph(AppScope::class)
interface AppGraph {
    val store: StudyStore
    val subscription: SubscriptionKey<List<Attempt>>
    val client: SwrClientPlus
    @Provides @SingleIn(AppScope::class)
    fun database(context: Context): StudyDatabase = StudyDatabase.open(context)
    @Provides fun dao(db: StudyDatabase): StudyDao = db.dao()
    @Provides @SingleIn(AppScope::class)
    fun store(dao: StudyDao): StudyStore = StudyStore(dao)
    @Provides @SingleIn(AppScope::class)
    fun subscription(dao: StudyDao): SubscriptionKey<List<Attempt>> = buildSubscriptionKey(
        id = SubscriptionId("study-attempts"), subscribe = { dao.observe() }
    )
    @OptIn(ExperimentalSoilQueryApi::class)
    @Provides @SingleIn(AppScope::class)
    fun client(): SwrClientPlus = SwrCachePlus(SwrCachePlusPolicy(coroutineScope = SwrCacheScope()))
    @DependencyGraph.Factory
    fun interface Factory { fun create(@Provides context: Context): AppGraph }
}
class StudyApplication : Application() {
    val graph by lazy { createGraphFactory<AppGraph.Factory>().create(applicationContext) }
}
