package at.samegger.dataaccess;

import at.samegger.domain.Student;

import java.sql.Date;
import java.util.List;

public interface MyStudentRepository extends BaseRepository<Student, Long>{

    List<Student> findAllStudentsByNameLike(String searchText);
    List<Student> findAllStudentsByBirthDate(Date birthdate);

}
