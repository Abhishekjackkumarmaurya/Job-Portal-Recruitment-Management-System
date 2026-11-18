package com.abhishek.jobportal.repository;

import com.abhishek.jobportal.entity.Job;
import com.abhishek.jobportal.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JobRepository extends JpaRepository<Job, Long> {

    Page<Job> findByRecruiter(User recruiter, Pageable pageable);

    Page<Job> findByActiveTrue(Pageable pageable);

    @Query("SELECT j FROM Job j WHERE j.active = true " +
            "AND (:keyword IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "     OR LOWER(j.skillsRequired) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) " +
            "AND (:employmentType IS NULL OR j.employmentType = :employmentType)")
    Page<Job> search(@Param("keyword") String keyword,
                      @Param("location") String location,
                      @Param("employmentType") String employmentType,
                      Pageable pageable);
}
