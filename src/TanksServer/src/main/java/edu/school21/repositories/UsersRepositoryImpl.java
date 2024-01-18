package edu.school21.repositories;

import edu.school21.domain.Statistic;
import edu.school21.domain.Tank;
import edu.school21.domain.User;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import javax.persistence.TypedQuery;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class UsersRepositoryImpl implements UsersRepository{

    @Autowired
    private  final SessionFactory sessionFactory;

    @Override
    public Optional<User> findByName(String name) {
        Session session = sessionFactory.openSession();
        User user = session.get(User.class, name);
        session.close();
        return Optional.ofNullable(user);
    }


    @Override
    public List<User> findAll() {
        Session session = sessionFactory.openSession();
        CriteriaQuery<User> criteriaQuery = session.getCriteriaBuilder().createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root);
        TypedQuery<User> query = session.createQuery(criteriaQuery);
        List<User> users = query.getResultList();
        session.close();
        return users;
    }

    @Override
    public boolean save(String name, String password) {
        Statistic statistic = new Statistic(name);
        User user = new User(name, password, System.currentTimeMillis(), new Tank(), statistic);
        user.getStatistic().setUser(user);
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        }
    }

    @Override
    public boolean update(User user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        }
    }

    @Override
    public boolean delete(User user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.delete(user);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        }
    }
}
