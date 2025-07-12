package com.playschool.management.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
@Profile({"postgresql", "cloud", "dev"})
public class DatabaseConnectionTest implements CommandLineRunner {
    
    @Autowired
    private DataSource dataSource;
    
    @Override
    public void run(String... args) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("✅ Database connection successful!");
            System.out.println("📊 Database URL: " + connection.getMetaData().getURL());
            System.out.println("🔧 Database Product: " + connection.getMetaData().getDatabaseProductName());
            System.out.println("📝 Database Version: " + connection.getMetaData().getDatabaseProductVersion());
            System.out.println("👤 Connected as: " + connection.getMetaData().getUserName());
        } catch (Exception e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
