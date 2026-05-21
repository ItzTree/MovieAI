package com.movieai.app.domain.usecase

import com.movieai.app.domain.model.MovieDetail
import com.movieai.app.domain.repository.MovieRepository
import javax.inject.Inject

/** Fetch full detail (overview, runtime, cast) for a single movie. */
class GetMovieDetailUseCase @Inject constructor(
    private val repository: MovieRepository,
) {
    suspend operator fun invoke(id: Long): Result<MovieDetail> =
        repository.getMovieDetail(id)
}
