package bbct.android.data

import android.database.SQLException
import android.os.AsyncTask

class InsertCardTask(
    private val dao: BaseballCardDao,
    private val onSuccess: () -> Unit,
    private val onFail: () -> Unit
) :
    AsyncTask<BaseballCard, Void, Int>() {

    override fun doInBackground(vararg baseballCards: BaseballCard): Int {
        try {
            dao.insertBaseballCard(baseballCards[0])
            return STATUS_OK
        } catch (e: SQLException) {
            val message = e.message
            if (message!!.startsWith("UNIQUE constraint failed")) {
                return STATUS_DUPLICATE
            }

            return STATUS_OTHER
        }
    }

    override fun onPostExecute(status: Int) {
        when (status) {
            STATUS_OK -> onSuccess()
            STATUS_DUPLICATE -> onFail()
            STATUS_OTHER -> {}
        }
    }

    companion object {
        private const val STATUS_OK = 1
        private const val STATUS_DUPLICATE = 2
        private const val STATUS_OTHER = 3
    }
}
