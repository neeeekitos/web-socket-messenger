//package common;
//
//import common.domain.Chat;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.context.annotation.*;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
//import server.ConnectionThread;
//import server.service.MessageService;
//
//import java.util.Map;
//
//@Configuration
//@EnableJpaRepositories(basePackages = {"server.dao"})
//@EntityScan(basePackages = {"domain"} )
//@Import({MessageService.class})
//public class ApplicationConfiguration {
//
//    @Autowired
//    MessageService messageService;
//
////    @Bean(name = "connectionThread")
////    @Scope("prototype")  // As we want to create several beans with different args, right?
////    public ConnectionThread connectionThread(Connection connection, Map<String, Connection> activeConnections, Map<Integer, Chat> activeChats) {
////        return new ConnectionThread(connection, activeConnections, activeChats, messageService);
////    }
//
//    @Bean(destroyMethod = "shutdown")
//    public EmbeddedDatabase dataSource() {
//        return new EmbeddedDatabaseBuilder().
//                setType(EmbeddedDatabaseType.H2).
//                addScript("db-schema.sql").
//                addScript("db-test-data.sql").
//                build();
//    }
//}