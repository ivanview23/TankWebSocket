//package edu.school21.repositories;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import edu.school21.domain.User;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.Transaction;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import javax.persistence.TypedQuery;
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.Root;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.Mockito.*;
//
//class UsersRepositoryImplTest {
//
//    @Mock
//    private SessionFactory sessionFactory;
//
//    @Mock
//    private Session session;
//
//    @Mock
//    private Transaction transaction;
//
//    @Mock
//    private TypedQuery<User> query;
//
//    private UsersRepositoryImpl usersRepository;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.initMocks(this);
//        usersRepository = new UsersRepositoryImpl(sessionFactory);
//    }
//
//    @Test
//    void findById() {
//        // Test the findById method
//        String userId = "1";
//        User user = new User(userId, "John Doe");
//
//        when(sessionFactory.openSession()).thenReturn(session);
//        when(session.get(User.class, userId)).thenReturn(user);
//        when(session.close()).thenReturn(null);
//
//        Optional<User> result = usersRepository.findById(userId);
//
//        assertTrue(result.isPresent());
//        assertEquals(user, result.get());
//
//        verify(sessionFactory).openSession();
//        verify(session).get(User.class, userId);
//        verify(session).close();
//    }
//
//    @Test
//    void findAll() {
//        // Test the findAll method
//        User user1 = new User("1", "John Doe");
//        User user2 = new User("2", "Jane Smith");
//        List<User> userList = new ArrayList<>();
//        userList.add(user1);
//        userList.add(user2);
//
//        when(sessionFactory.openSession()).thenReturn(session);
//        when(session.getCriteriaBuilder()).thenReturn(mock(CriteriaBuilder.class));
//        when(session.getCriteriaBuilder().createQuery(User.class)).thenReturn(mock(CriteriaQuery.class));
//        when(session.getCriteriaBuilder().createQuery(User.class).from(User.class)).thenReturn(mock(Root.class));
//        when(session.createQuery(any(CriteriaQuery.class))).thenReturn(query);
//        when(query.getResultList()).thenReturn(userList);
//        when(session.close()).thenReturn(null);
//
//        List<User> result = usersRepository.findAll();
//
//        assertEquals(userList, result);
//
//        verify(sessionFactory).openSession();
//        verify(session).getCriteriaBuilder();
//        verify(session.getCriteriaBuilder()).createQuery(User.class);
//        verify(session.getCriteriaBuilder().createQuery(User.class)).from(User.class);
//        verify(session).createQuery(any(CriteriaQuery.class));
//        verify(query).getResultList();
//        verify(session).close();
//    }
//
//    @Test
//    void save() {
//        // Test the save method
//        User user = new User("1", "John Doe");
//
//        when(sessionFactory.openSession()).thenReturn(session);
//        when(session.beginTransaction()).thenReturn(transaction);
//        when(session.save(user)).thenReturn(null);
//        when(transaction.commit()).thenReturn(null);
//        when(session.close()).thenReturn(null);
//
//        boolean result = usersRepository.save(user);
//
//        assertTrue(result);
//
//        verify(sessionFactory).openSession();
//        verify(session).beginTransaction();
//        verify(session).save(user);
//        verify(transaction).commit();
//        verify(session).close();
//    }
//
//    @Test
//    void update() {
//        // Test the update method
//        User user = new User("1", "John Doe");
//
//        when(sessionFactory.openSession()).thenReturn(session);
//        when(session.beginTransaction()).thenReturn(transaction);
//        when(session.update(user)).thenReturn(null);
//        when(transaction.commit()).thenReturn(null);
//        when(session.close()).thenReturn(null);
//
//        boolean result = usersRepository.update(user);
//
//        assertTrue(result);
//
//        verify(sessionFactory).openSession();
//        verify(session).beginTransaction();
//        verify(session).update(user);
//        verify(transaction).commit();
//        verify(session).close