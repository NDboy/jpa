package activitytracker;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.function.Consumer;

public class ActivityTrackerDao {

    private EntityManagerFactory factory;

    public ActivityTrackerDao(EntityManagerFactory factory) {
        this.factory = factory;
    }

    public void saveActivity(Activity activity) {
        EntityManager entityManager = factory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(activity);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public void saveActivitiesFromList(List<Activity> activities) {
        EntityManager entityManager = factory.createEntityManager();
        entityManager.getTransaction().begin();
        activities.forEach(entityManager::persist);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public List<Activity> selectActivitiesFromDb() {
        EntityManager em = factory.createEntityManager();
        return em.createQuery("select a from Activity a", Activity.class).getResultList();
    }

    public void setDataById(long id, Consumer<Activity> consumer) {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        Activity activity = em.find(Activity.class, id);
        consumer.accept(activity);
        em.getTransaction().commit();
        em.close();
    }

    public void deleteActivityById(long id) {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        Activity activity = em.find(Activity.class, id);
//        Activity activity = em.getReference(Activity.class, id);
        em.remove(activity);
        em.getTransaction().commit();
        em.close();
    }

    public Activity findActivityById(long id) {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        Activity activity = em.find(Activity.class, id);
        em.getTransaction().commit();
        em.close();
        return activity;
    }
}
