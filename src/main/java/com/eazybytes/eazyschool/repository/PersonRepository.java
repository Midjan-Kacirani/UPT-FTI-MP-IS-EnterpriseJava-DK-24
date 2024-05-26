package com.eazybytes.eazyschool.repository;

import com.eazybytes.eazyschool.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    Person readByEmail(String email);

    @Query("SELECT p FROM Courses c JOIN c.persons p WHERE c.courseId = :courseId AND p.roles.roleId = :roleId")
    Set<Person> findPersonsByCourseIdAndRoleId(@Param("courseId") int courseId, @Param("roleId") int roleId);

    @Query("SELECT p FROM Person p WHERE p.roles.roleName = :role")
    Set<Person> findByRoleName(@Param("role") String role);

}
