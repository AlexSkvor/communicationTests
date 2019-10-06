package ru.lingstra.avitocopy.data.repository

import ru.lingstra.avitocopy.domain.hand_shakes.FriendItem

data class SimplestUser(
    val item: FriendItem,
    val checked: Boolean = false,
    val parent: SimplestUser? = null,
    val level: Int = 0,
    val side: Boolean = false
) {

    val shouldCheck: Boolean
        get() = !checked && (item.can_access_closed || !item.is_closed)

    override fun equals(other: Any?): Boolean {
        if (other !is SimplestUser) return false
        return other.item.id == item.id
    }

    fun toListChildStart(): List<SimplestUser> {
        val list = mutableListOf<SimplestUser>()
        var cur: SimplestUser? = this
        while (cur != null) {
            list.add(cur)
            cur = cur.parent
        }
        return list
    }

    override fun hashCode(): Int {
        return item.id
    }
}