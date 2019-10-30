package org.superbiz.moviefun;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ServletRegistrationBean actionServletRegistration(ActionServlet actionServlet) {
        return new ServletRegistrationBean(actionServlet, "/moviefun/*");
    }

    @Bean
    public DatabaseServiceCredentials getDatabaseServicesCredentials(@Value("${vcap.services}") String vcapServices) {
        return new DatabaseServiceCredentials(vcapServices);
    }

    @Bean
    public DataSource albumsDataSource(DatabaseServiceCredentials serviceCredentials) {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(serviceCredentials.jdbcUrl("albums-mysql"));
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setDataSource(dataSource);
        return hikariDataSource;
    }

    @Bean
    public DataSource moviesDataSource(DatabaseServiceCredentials serviceCredentials) {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(serviceCredentials.jdbcUrl("movies-mysql"));
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setDataSource(dataSource);
        return hikariDataSource;
    }

    @Bean
    public HibernateJpaVendorAdapter getHibernateJpaVendorAdapter() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setDatabase(Database.MYSQL);
        hibernateJpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQL5Dialect");
        hibernateJpaVendorAdapter.setGenerateDdl(true);
        return hibernateJpaVendorAdapter;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean albumsLocalContainerEntityManagerFactoryBean(HibernateJpaVendorAdapter hibernateJpaVendorAdapter, DataSource albumsDataSource ) {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(albumsDataSource);
        em.setPackagesToScan("org.superbiz.moviefun.albums");
        em.setJpaVendorAdapter(hibernateJpaVendorAdapter);
        em.setPersistenceUnitName("albumsPersistence");
        return em;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean moviesLocalContainerEntityManagerFactoryBean(HibernateJpaVendorAdapter hibernateJpaVendorAdapter, DataSource moviesDataSource ) {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(moviesDataSource);
        em.setPackagesToScan("org.superbiz.moviefun.movies");
        em.setJpaVendorAdapter(hibernateJpaVendorAdapter);
        em.setPersistenceUnitName("moviesPersistence");
        return em;
    }


    @Bean
    public PlatformTransactionManager moviesPlatformTransactionManager(EntityManagerFactory moviesLocalContainerEntityManagerFactoryBean) {
        JpaTransactionManager platformTransactionManager = new JpaTransactionManager();
        platformTransactionManager.setEntityManagerFactory(moviesLocalContainerEntityManagerFactoryBean);
        return platformTransactionManager;
    }

    @Bean
    public PlatformTransactionManager albumsPlatformTransactionManager(EntityManagerFactory albumsLocalContainerEntityManagerFactoryBean) {
        JpaTransactionManager platformTransactionManager = new JpaTransactionManager();
        platformTransactionManager.setEntityManagerFactory(albumsLocalContainerEntityManagerFactoryBean);
        return platformTransactionManager;
    }

    @Bean(name = "chainedTransactionManager")
    public ChainedTransactionManager transactionManager(PlatformTransactionManager moviesPlatformTransactionManager,
                                                        PlatformTransactionManager albumsPlatformTransactionManager) {
        return new ChainedTransactionManager(moviesPlatformTransactionManager, albumsPlatformTransactionManager);
    }

}
