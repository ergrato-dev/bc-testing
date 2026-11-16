package com.bootcamp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/** Guarda entradas en PostgreSQL con JDBC. Las reglas (precio positivo, butaca única) las pone la tabla. */
public class TicketRepository {

    private final String url;
    private final String user;
    private final String password;

    public TicketRepository(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public Ticket save(String seat, int priceCents) throws SQLException {
        String sql = "INSERT INTO tickets (seat, price_cents) VALUES (?, ?) RETURNING id";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement insert = connection.prepareStatement(sql)) {
            insert.setString(1, seat);
            insert.setInt(2, priceCents);
            try (ResultSet result = insert.executeQuery()) {
                result.next();
                return new Ticket(result.getLong("id"), seat, priceCents);
            }
        }
    }

    public Optional<Ticket> findBySeat(String seat) throws SQLException {
        String sql = "SELECT id, seat, price_cents FROM tickets WHERE seat = ?";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement select = connection.prepareStatement(sql)) {
            select.setString(1, seat);
            try (ResultSet result = select.executeQuery()) {
                if (!result.next()) {
                    return Optional.empty();
                }
                return Optional.of(new Ticket(result.getLong("id"), result.getString("seat"), result.getInt("price_cents")));
            }
        }
    }
}
