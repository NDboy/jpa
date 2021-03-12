package activitytracker;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.MariaDbDataSource;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class ActivityTrackerDaoTest {

    ActivityTrackerDao activityTrackerDao;
    List<Activity> activities = new ArrayList<>();
    Activity activitySingle;

    @BeforeEach
    void init() {
        MariaDbDataSource dataSource;
        try {
            dataSource = new MariaDbDataSource();
            dataSource.setUrl("jdbc:mariadb://localhost:3306/activitytracker?useUnicode=true");
            dataSource.setUser("activitytracker");
            dataSource.setPassword("activitytracker");

        } catch (SQLException se) {
            throw new IllegalStateException("Can not create data source", se);
        }

        Flyway flyway = Flyway.configure().locations("/db/migration/activitytracker").dataSource(dataSource).load();
        flyway.clean();
        flyway.migrate();

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("pu");
        activityTrackerDao = new ActivityTrackerDao(factory);

        IntStream.range(0,10)
                .mapToObj(Activity::creator)
                .forEach(activities::add);

        activitySingle = new Activity(LocalDateTime.now(), "single activity", ActivityType.HIKING);

    }

    @Test
    void testSaveActivity() {
        activityTrackerDao.saveActivity(activitySingle);
        long id = activitySingle.getId();
        assertEquals("single activity", activityTrackerDao.findActivityById(id).getDesc());
    }

    @Test
    void testSaveActivitiesFromList() {
        activityTrackerDao.saveActivitiesFromList(activities);
        assertEquals(10, activityTrackerDao.selectActivitiesFromDb().size());
    }

    @Test
    void testSetDataById() {
        activityTrackerDao.saveActivity(activitySingle);
        long id = activitySingle.getId();
        activityTrackerDao.setDataById(id, a -> a.setDesc("!!!fake activity!!!"));
        assertEquals("!!!fake activity!!!", activityTrackerDao.findActivityById(id).getDesc());
    }

    @Test
    void testDeleteActivityById() {
        activityTrackerDao.saveActivitiesFromList(activities);
        activityTrackerDao.deleteActivityById(1);
        assertEquals(9, activityTrackerDao.selectActivitiesFromDb().size());
    }

}