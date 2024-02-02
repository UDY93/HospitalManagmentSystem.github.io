package HospitalManagmentSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Doctors {
    private Connection connection;


    public Doctors(Connection connection)
    {
        this.connection=connection;

    }


    public void viewDoctors() {
        try {
            String query = "SELECT id,name,specilization FROM doctors";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int doctorId = resultSet.getInt("id");
                String doctorName = resultSet.getString("name");

                String doctorspe = resultSet.getString("specilization");

                // Process or print the retrieved data
                System.out.println("Doctor ID: " + doctorId + ", Doctor Name: " + doctorName + ", Doctor specilization: " + doctorspe);
            }

            // Close the resources
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean getDoctorById(int id)
    {
        String query = "select * from doctors where id = ? ";

        try {
            PreparedStatement  preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
            {
                return true;

            }else {
                return false;
            }

        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }
}
