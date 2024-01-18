package edu.school21.hibernateconfig;

import edu.school21.domain.Statistic;
import edu.school21.domain.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import java.util.EnumSet;

@Component
public class HibernateConfig {

    @Autowired
    private static SessionFactory sessionFactory;

    public HibernateConfig() {
        sessionFactory = getSessionFactory();
    }

    @Bean
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                // Создание строителя реестра сервисов
                StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder()
                        .applySetting("hibernate.connection.url", "jdbc:postgresql://localhost:5433/postgres")
                        .applySetting("hibernate.connection.username", "postgres")
                        .applySetting("hibernate.connection.password", "postgres");

                // Создание реестра сервисов
                StandardServiceRegistry serviceRegistry = registryBuilder.build();

                // Создание источника метаданных
                MetadataSources metadataSources = new MetadataSources(serviceRegistry);
                metadataSources.addAnnotatedClass(User.class);
                metadataSources.addAnnotatedClass(Statistic.class);
                Metadata metadata = metadataSources.buildMetadata();
                SchemaExport schemaExport = new SchemaExport();
                schemaExport.createOnly(EnumSet.of(TargetType.DATABASE), metadata);

                // Создание фабрики сессий
                sessionFactory = metadata.buildSessionFactory();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
