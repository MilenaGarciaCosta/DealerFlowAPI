package com.example.DealerFlow;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestDBConnection {
    public static void main(String[] args){
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/dealer_flow",
                    "root",
                    "root"
            );

            System.out.println("Conectado com sucesso!");
            conn.close();

        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
