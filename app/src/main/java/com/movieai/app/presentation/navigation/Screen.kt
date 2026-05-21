package com.movieai.app.presentation.navigation

sealed class Screen(val route: String) {
    data object Search    : Screen("search")
    data object Favorites : Screen("favorites")
    data object Recommend : Screen("recommend")

    data object Detail : Screen("detail/{id}") {
        const val ARG_ID = "id"
        fun build(id: Long) = "detail/$id"
    }

    companion object {
        // Note: literal strings — referencing Search.route here would NPE during
        // class init (companion runs before nested data objects are constructed).
        val topLevelRoutes: Set<String> = setOf("search", "favorites", "recommend")
    }
}
