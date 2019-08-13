package io.legado.app.ui.main.bookshelf

import android.app.Application
import io.legado.app.App
import io.legado.app.base.BaseViewModel
import io.legado.app.constant.BookType
import io.legado.app.data.entities.BookGroup
import io.legado.app.model.WebBook
import kotlinx.coroutines.Dispatchers.IO

class BookshelfViewModel(application: Application) : BaseViewModel(application) {

    fun saveBookGroup(group: String?) {
        if (!group.isNullOrBlank()) {
            execute {
                App.db.bookGroupDao().insert(
                    BookGroup(
                        App.db.bookGroupDao().maxId + 1,
                        group
                    )
                )
            }
        }
    }


    fun upChapterList() {
        execute {
            App.db.bookDao().getRecentRead().map { book ->
                if (book.origin != BookType.local) {
                    val bookSource = App.db.bookSourceDao().getBookSource(book.origin)
                    bookSource?.let {
                        WebBook(bookSource).getChapterList(book).onSuccess(IO) {
                            it?.let {
                                if (it.size > App.db.bookChapterDao().getChapterCount(book.bookUrl)) {

                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
