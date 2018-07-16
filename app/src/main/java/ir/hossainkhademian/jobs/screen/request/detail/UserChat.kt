package ir.hossainkhademian.jobs.screen.request.detail

import ir.hossainkhademian.jobs.data.model.User

internal typealias UserChat = Pair<User, Int>
internal val UserChat.user get() = first
internal val UserChat.unreadCount get() = second
