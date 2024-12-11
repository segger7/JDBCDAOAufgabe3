package at.samegger.dataaccess;

import at.samegger.MysqlDatabaseConnection;
import at.samegger.domain.Course;
import at.samegger.domain.CourseType;
import at.samegger.domain.Student;
import at.samegger.util.Assert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySqlStudentRepository implements MyStudentRepository{

    private Connection con;

    public MySqlStudentRepository() throws SQLException, ClassNotFoundException {
        this.con = MysqlDatabaseConnection.getConnection("jdbc:mysql://localhost:3306/imstkurssystem","root","");
    }

    @Override
    public List<Student> findAllStudentsByNameLike(String searchText) {
        String sql = "SELECT * FROM students WHERE firstname LIKE %?% OR lastname LIKE %?%";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, searchText);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Student> studentList = new ArrayList<>();
            while(resultSet.next()) {
                Student student = new Student(
                        resultSet.getLong("id"),
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname"),
                        resultSet.getDate("dateOfBirth"));
                studentList.add(student);
            }
            return studentList;

        } catch (SQLException sqlException) {
            System.out.println("Datenbankfehler: " + sqlException);
        }
        return List.of();
    }

    @Override
    public List<Student> findAllStudentsByBirthDate(Date birthdate) {
        String sql = "SELECT * FROM students WHERE birthdate = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setDate(1, birthdate);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Student> studentList = new ArrayList<>();
            while(resultSet.next()) {
                Student student = new Student(
                        resultSet.getLong("id"),
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname"),
                        resultSet.getDate("dateOfBirth"));
                studentList.add(student);
            }
            return studentList;

        } catch (SQLException sqlException) {
            System.out.println("Datenbankfehler: " + sqlException);
        }
        return List.of();
    }

    @Override
    public Optional<Student> insert(Student entity) {
        Assert.notNull(entity);
        try{
            String sql = "INSERT INTO students (firstname, lastname, dateOfBirth) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, entity.getFirstname());
            preparedStatement.setString(2, entity.getLastname());
            preparedStatement.setDate(3, entity.getDateOfBirth());

            int affectedRows = preparedStatement.executeUpdate();

            if(affectedRows == 0) {
                return Optional.empty();
            }

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.next()) {
                return this.getById(generatedKeys.getLong(1));
            } else {
                return Optional.empty();
            }
        } catch(SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public Optional<Student> getById(Long id) {
        Assert.notNull(id);
        if(countStudentsInDBWithId(id)==0) {
            return Optional.empty();
        } else {
            try {
                String sql = "SELECT * FROM students WHERE id = ?";
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();

                resultSet.next();
                Student student = new Student(
                        resultSet.getLong("id"),
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname"),
                        resultSet.getDate("dateOfBirth"));
                return Optional.of(student);

            } catch(SQLException sqlException) {
                throw new DatabaseException(sqlException.getMessage());
            }
        }
    }

    @Override
    public List<Student> getAll() {
        String sql = "SELECT * FROM students";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Student> studentList = new ArrayList<>();
            while(resultSet.next()) {
                studentList.add( new Student(
                                resultSet.getLong("id"),
                                resultSet.getString("firstname"),
                                resultSet.getString("lastname"),
                                resultSet.getDate("dateOfBirth")
                        )
                );
            }
            return studentList;
        } catch (SQLException e) {
            throw new DatabaseException("Database Error");
        }
    }

    private int countStudentsInDBWithId(Long id) {
        try {
            String countSql = "SELECT COUNT(*) FROM `students` WHERE `id`=?";
            PreparedStatement preparedStatementCount = con.prepareStatement(countSql);
            preparedStatementCount.setLong(1, id);
            //System.out.println(preparedStatementCount.toString());
            ResultSet resultSetCount = preparedStatementCount.executeQuery();
            resultSetCount.next();
            int courseCount = resultSetCount.getInt(1);
            return courseCount;
        } catch(SQLException sqlExcepton) {
            throw new DatabaseException(sqlExcepton.getMessage());
        }
    }

    @Override
    public Optional<Student> update(Student entity) {
        Assert.notNull(entity);

        String sql = "UPDATE students SET firstname = ?, lastname = ?, dateOfBirth = ? WHERE students.id = ? ";

        if(countStudentsInDBWithId(entity.getId())==0) {
            return Optional.empty();
        } else {
            try {
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, entity.getFirstname());
                preparedStatement.setString(2, entity.getLastname());
                preparedStatement.setDate(3, entity.getDateOfBirth());
                preparedStatement.setLong(4, entity.getId());

                int affectedRows = preparedStatement.executeUpdate();

                if(affectedRows==0) {
                    return Optional.empty();
                } else {
                    return this.getById(entity.getId());
                }

            } catch(SQLException sqlException) {
                throw new DatabaseException(sqlException.getMessage());

            }
        }
    }

    @Override
    public void deleteByid(Long id) {
        Assert.notNull(id);
        String sql = "DELETE FROM students WHERE id = ?";
        try {
            if (countStudentsInDBWithId(id) == 1) {
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();
            }
        } catch(SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }
}
