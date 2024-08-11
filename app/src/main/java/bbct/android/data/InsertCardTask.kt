package bbct.android.data;

import android.database.SQLException;
import android.os.AsyncTask;

import bbct.android.common.fragment.BaseballCardDetails;

public class InsertCardTask extends AsyncTask<BaseballCard, Void, Integer> {
    private static final int STATUS_OK = 1;
    private static final int STATUS_DUPLICATE = 2;
    private static final int STATUS_OTHER = 3;

    private final BaseballCardDetails fragment;
    private final BaseballCardDao dao;

    public InsertCardTask(BaseballCardDetails fragment, BaseballCardDao dao) {
        this.fragment = fragment;
        this.dao = dao;
    }

    @Override
    protected Integer doInBackground(BaseballCard... baseballCards) {
        try {
            dao.insertBaseballCard(baseballCards[0]);
            return STATUS_OK;
        } catch (SQLException e) {
            String message = e.getMessage();
            if (message.startsWith("UNIQUE constraint failed")) {
                return STATUS_DUPLICATE;
            }

            return STATUS_OTHER;
        }
    }

    @Override
    protected void onPostExecute(Integer status) {
        switch (status) {
            case STATUS_OK:
                fragment.insertSuccessful();
                break;
            case STATUS_DUPLICATE:
                fragment.insertFailed();
                break;
            case STATUS_OTHER:
                break;
        }
    }
}
