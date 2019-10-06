package ru.lingstra.avitocopy.data.repository

import android.content.res.Resources
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import ru.lingstra.avitocopy.data.prefs.AppPrefs
import ru.lingstra.avitocopy.domain.hand_shakes.User
import ru.lingstra.avitocopy.system.network.NetworkApi
import ru.lingstra.avitocopy.system.schedulers.SchedulersProvider
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HandShakesRepository @Inject constructor(
    private val api: NetworkApi,
    private val prefs: AppPrefs,
    private val scheduler: SchedulersProvider
) {

    private val queriesRelay: PublishRelay<Unit> = PublishRelay.create()
    fun queries(): Observable<Unit> = queriesRelay.hide().observeOn(scheduler.ui())

    private var usersSetFirst: MutableSet<SimplestUser> = mutableSetOf()
    private var usersSetSecond: MutableSet<SimplestUser> = mutableSetOf()
    private var started = false
    private var loadingsNumber = 0
    private var turn = true //true -> second!

    fun searchHandShakes(firstUrl: String, secondUrl: String) =
        Single.zip(
            getFriendItemFromUrl(firstUrl.substringAfter("https://vk.com/")),
            getFriendItemFromUrl(secondUrl.substringAfter("https://vk.com/")),
            BiFunction<SimplestUser, SimplestUser, Pair<SimplestUser, SimplestUser>> { t1, t2 ->
                t1 to t2.copy(side = true)
            }
        ).subscribeOn(scheduler.io())
            .toObservable()
            .observeOn(scheduler.ui())
            .doOnNext { usersSetFirst = mutableSetOf(it.first) }
            .doOnNext { usersSetSecond = mutableSetOf(it.second) }
            .doOnNext { started = true }
            .ignoreElements()
            .andThen(timed())

    private fun getFriendItemFromUrl(path: String): Single<SimplestUser> =
        api.userInfo(userId = path, token = prefs.token)
            .doAfterSuccess { queriesRelay.accept(Unit) }
            .map { it.response.first() }
            .map { SimplestUser(it) }

    private fun timed() =
        Observable.interval(700L, 400L, TimeUnit.MILLISECONDS)
            .observeOn(scheduler.ui())
            .filter { started }
            .doOnNext { turn = !turn }
            .map { if (turn) usersSetFirst else usersSetSecond }
            .observeOn(scheduler.computation())
            .doOnNext {
                if (loadingsNumber == 0 && it.none { u -> u.shouldCheck }) throw Resources.NotFoundException()
            }
            .filter { it.any { user -> user.shouldCheck } }
            .observeOn(scheduler.ui())
            .doOnNext { loadingsNumber++ }
            .observeOn(scheduler.computation())
            .map { it.filter { user -> user.shouldCheck }.minBy { user -> user.level } }
            .map { it.copy(checked = true) }
            .observeOn(scheduler.ui())
            .doOnNext { replaceInSet(it) }
            .map { if (it.level < 3) it else throw Resources.NotFoundException() }
            .observeOn(scheduler.io())
            .flatMap { getFriendsList(it) }
            .map { it.toSet() }
            .observeOn(scheduler.ui())
            .doOnNext { putToSet(it) }
            .map { usersSetFirst to usersSetSecond }
            .observeOn(scheduler.computation())
            .map { buildPath(it.first, it.second) }
            .observeOn(scheduler.ui())
            .doOnNext { loadingsNumber-- }
            .filter { it.isNotEmpty() }
            .first(listOf()).toObservable()

    private fun buildPath(first: Set<SimplestUser>, second: Set<SimplestUser>): List<User> =
        first.intersect(second).firstOrNull()
            ?.let { commonUser ->
                val answer =
                    first.first { it.item.id == commonUser.item.id }.toListChildStart().reversed() +
                            second.first { it.item.id == commonUser.item.id }.toListChildStart()
                answer.distinct().map { User(it) }
            } ?: emptyList()

    private fun putToSet(it: Set<SimplestUser>) {
        if (it.isEmpty()) return
        if (it.first().side) usersSetSecond.addAll(it)
        else usersSetFirst.addAll(it)
    }

    private fun replaceInSet(user: SimplestUser) {
        if (user.side) {
            usersSetSecond.remove(user)
            usersSetSecond.add(user)
        } else {
            usersSetFirst.remove(user)
            usersSetFirst.add(user)
        }
    }

    private fun getFriendsList(user: SimplestUser) =
        api.friendsList(user.item.id, token = prefs.token)
            .doAfterSuccess { queriesRelay.accept(Unit) }
            .observeOn(scheduler.computation())
            .filter { it.response != null }
            .map { it.response?.items ?: listOf() }
            .flattenAsFlowable { it }
            .map {
                SimplestUser(
                    item = it,
                    parent = user,
                    level = user.level + 1,
                    side = user.side
                )
            }.toList().toObservable()

}