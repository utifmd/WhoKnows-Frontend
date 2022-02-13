package com.dudegenuine.whoknows.infrastructure.common.singleton

open class SingletonHolder<out T: Any, in A>(creator: (A) -> T) {
    private var creator: ((A) -> T)? = creator

    @Volatile private var instance: T? = null

    fun getInstance(context: A): T {
        val checkInstance = instance

        if (checkInstance != null) {
            return checkInstance
        }

        return synchronized(this) {
            val checkInstanceAgain = instance

            if (checkInstanceAgain != null) { checkInstanceAgain }
            else {
                val created = creator!!(context)

                instance = created
                creator = null

                created
            }
        }
    }
}