package jpa;

import org.flywaydb.core.Flyway;
import org.mariadb.jdbc.MariaDbDataSource;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.IntStream;

public class EmployeeMain {

    public static void main(String[] args) {
        MariaDbDataSource dataSource;
        try {
            dataSource = new MariaDbDataSource();
            dataSource.setUrl("jdbc:mariadb://localhost:3306/employees?useUnicode=true");
            dataSource.setUser("employees");
            dataSource.setPassword("employees");
        } catch (SQLException se) {
            throw new IllegalStateException("Can not create data source", se);
        }

        Flyway flyway = Flyway.configure()./*locations("/db/migration/covid").*/dataSource(dataSource).load();
        flyway.clean();
        flyway.migrate();


        EntityManagerFactory factory = Persistence.createEntityManagerFactory("pu");
        EntityManager entityManager = factory.createEntityManager();

        entityManager.getTransaction().begin();
        IntStream.range(0,10)
//                .mapToObj(i -> new Employee("John Doe " + i))
//                .forEach(entityManager::persist);
                .forEach(i -> { Employee emp = new Employee();
                                emp.setName("John Doe " + i);
                                entityManager.persist(emp);
                });
        entityManager.getTransaction().commit();

        List<Employee> employees = entityManager.createQuery("select e from Employee e order by e.name",
                Employee.class).getResultList();

        System.out.println(employees);

        entityManager.getTransaction().begin();
        Employee employeeToModify = entityManager.find(Employee.class, employees.get(4).getId());
        System.out.println(employeeToModify.getName());

        employeeToModify.setName("Jack Doe");
        entityManager.getTransaction().commit();

        entityManager.getTransaction().begin();
        Employee employeeToDelete = entityManager.find(Employee.class, employees.get(3).getId());
        entityManager.remove(employeeToDelete);
        entityManager.getTransaction().commit();

        List<Employee> newEmployees = entityManager.createQuery("select e from Employee e", Employee.class)
                .getResultList();
        System.out.println(newEmployees);

        entityManager.close();
        factory.close();
    }
}
