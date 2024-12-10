package at.samegger.dataaccess;

import at.samegger.domain.Course;
import at.samegger.domain.CourseType;

import java.sql.Date;
import java.util.List;

public interface MyCourseRepository extends BaseRepository<Course, Long> {

    List<Course> findAllCoursesByName(String name);
    List<Course> findAllCoursesByDescription(String description);
    List<Course> findAllCoursesByNameOrDescription(String searchText);
    List<Course> findAllCoursesByStartDate(Date startDate);
    List<Course> findAllCoursesByCourseType(CourseType courseType);
    List<Course> findAllRunningCourses();
}
