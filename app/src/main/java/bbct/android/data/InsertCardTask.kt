package bbct.android.data

import android.database.SQLException
import bbct.android.common.fragment.BaseballCardDetails

class InsertCardTask(fragment: BaseballCardDetails, private val dao: BaseballCardDao) :
    AsyncTask<BaseballCard?, Void?, Int?>() {
    private val fragment: BaseballCardDetails = fragment

    protected override fun doInBackground(vararg baseballCards: BaseballCard): Int {
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

    protected override fun onPostExecute(status: Int) {
        when (status) {
            STATUS_OK -> fragment.insertSuccessful()
            STATUS_DUPLICATE -> fragment.insertFailed()
            STATUS_OTHER -> {}
        }
    }

    companion object {
        private const val STATUS_OK = 1
        private const val STATUS_DUPLICATE = 2
        private const val STATUS_OTHER = 3
    }
}
