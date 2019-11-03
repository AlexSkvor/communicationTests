package ru.lingstra.communications.domain.result

import io.reactivex.Observable
import ru.lingstra.communications.data.database.entities.UserEntity
import javax.inject.Inject

class ResultsInteractor @Inject constructor(

) {

    fun getResults(user: UserEntity): Observable<ResultsPartialState>{

    }

}