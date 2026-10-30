package com.ems;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/updateEmployee")
public class UpdateEmployeeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String sql = "UPDATE employees SET name=?, email=?, "
                + "department=?, salary=? WHERE id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, request.getParameter("name"));
            ps.setString(2, request.getParameter("email"));
            ps.setString(3, request.getParameter("department"));
            ps.setBigDecimal(4, new java.math.BigDecimal(
                    request.getParameter("salary")));
            ps.setInt(5, Integer.parseInt(request.getParameter("id")));

            int rows = ps.executeUpdate();

            if (rows > 0) {
                response.sendRedirect("viewEmployees");
            } else {
                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "Employee not found.");
            }

        } catch (SQLException | NumberFormatException e) {
            throw new ServletException("Unable to update employee.", e);
        }
    }
}
