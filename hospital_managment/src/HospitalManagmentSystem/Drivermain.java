package HospitalManagmentSystem;

import java.sql.*;
import java.util.Scanner;
import java.sql.DriverManager;


import static java.lang.Class.forName;

public class Drivermain {
   private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String userName = "root";
    private static final String password = "Uday@3421";


    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e)
        {
            e.printStackTrace();

        }
        Scanner scanner = new Scanner(System.in);

        try {
            Connection connection = DriverManager.getConnection(url, userName, password);

            Patient patient = new Patient( connection,scanner);
            Doctors doctors = new Doctors(connection);

            while (true)
            {
                System.out.println("hospital management system");
                System.out.println("1. Add Patient");
                System.out.println("2.  view Patients");
                System.out.println("3. view Doctors");
                System.out.println("4. book appointment");
                System.out.println("5. excites");

                System.out.println("Enter your Choice");
                int choice = scanner.nextInt();

                switch (choice)
                {
                    case 1:
                        // add patient
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        //view patient
                        patient.viewPatient();
                        System.out.println();
                        break;
                    case 3:
                        //view doctors
                        doctors.viewDoctors();
                        System.out.println();
                        break;
                    case 4:
                        //book appointment
                        bokAppoinent(patient,doctors,connection,scanner);
                        System.out.println();
                        break;
                    case 5:
                        return;


                    default:
                        System.out.println("Enter a valid choice");
                        break;
                }

            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void bokAppoinent(Patient patient, Doctors doctors,Connection connection,Scanner scanner)
    {
        System.out.println("enter a patient id");
        int patientid= scanner.nextInt();
        System.out.println("enter a doctor  id");
        int doctorid= scanner.nextInt();

        System.out.println("enter date (YYYY-MM-DD): ");
        String appoinemtdate = scanner.next();

        if (patient.getPatientById(patientid) && doctors.getDoctorById(doctorid))
        {
            if (checkdoctoravailability(doctorid ,appoinemtdate, connection))
            {
                String appoinquery = " insert into  appointments (patient_id,doctor_id,appointment_date) values( ? , ? , ? )" ;

                try {

                    PreparedStatement preparedStatement = connection.prepareStatement(appoinquery);
                    preparedStatement.setInt(1,patientid);
                    preparedStatement.setInt(2,doctorid);
                    preparedStatement.setString(3,appoinemtdate);


                    int rowAffected = preparedStatement.executeUpdate();
                    if (rowAffected > 0)
                    {
                        System.out.println("appointment booked");
                    }
                    else
                    {
                        System.out.println("appointment not booked");
                    }

                }catch (SQLException e)
                {
                    e.printStackTrace();
                }

            }else
            {
                System.out.println("already book appointments");
            }

        }else {
            System.out.println(" exist doctor or patient");
        }

    }

    public static boolean checkdoctoravailability(int doctorid,String appoinemtdate, Connection connection)
    {
        String query = " select count(*) from  appointments where doctor_id= ? and appointment_date=? ";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1,doctorid);
            preparedStatement.setString(2,appoinemtdate);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
            {
                int count = resultSet.getInt(1);

                if (count==0)
                {
                    return true;
                }else {
                    return false;
                }
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }

}
