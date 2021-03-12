package activitytracker;

import org.flywaydb.core.Flyway;
import org.mariadb.jdbc.MariaDbDataSource;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ActivityTrackerMain {

    public static void main(String[] args) {
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

        List<Activity> activities = new ArrayList<>();
        IntStream.range(0,10)
                .mapToObj(Activity::creator)
                .forEach(activities::add);

        ActivityTrackerDao atd = new ActivityTrackerDao(factory);

        atd.saveActivity(new Activity(LocalDateTime.now(), "single activity", ActivityType.HIKING));
        atd.saveActivitiesFromList(activities);
        List<Activity> loadedActivities = atd.selectActivitiesFromDb();
        loadedActivities.forEach(System.out::println);
        atd.setDataById(8, a->a.setDesc("This is a fake description"));
        atd.deleteActivityById(9);
        List<Activity> loadedActivities2 = atd.selectActivitiesFromDb();
        loadedActivities2.forEach(System.out::println);

    }
}






























