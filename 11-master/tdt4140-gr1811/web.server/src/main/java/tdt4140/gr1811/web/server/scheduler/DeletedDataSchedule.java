package tdt4140.gr1811.web.server.scheduler;

import tdt4140.gr1811.app.dao.DataProviderDao;
import tdt4140.gr1811.app.db.CredentialsFactory;
import tdt4140.gr1811.app.db.DataSourceBuilder;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;

public class DeletedDataSchedule implements Runnable {


    private DataSource ds = new DataSourceBuilder(CredentialsFactory.get()).build();
    private final int ONE_WEEK =  604800000;

    @Override
    public void run() {
        DataProviderDao dao = new DataProviderDao(ds);
        try {
            Map<Integer, Timestamp> data = dao.getDeletedDataInfo();
            for (Map.Entry<Integer, Timestamp> entry : data.entrySet()) {
                Timestamp ts = entry.getValue();
                int dataproviderID = entry.getKey();
                long calculatedTime = System.currentTimeMillis() - ts.getTime();

                if (calculatedTime  > ONE_WEEK) {
                    // User has been in delete-cache for over one week.
                    // Deleting from database.
                    try {
                        // deleting data
                        dao.deleteCachedData(dataproviderID);

                        // removing from DeletedData table
                        dao.setDeleteState(false, dataproviderID);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

