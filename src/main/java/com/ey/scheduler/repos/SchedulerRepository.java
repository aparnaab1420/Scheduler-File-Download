package com.ey.scheduler.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ey.scheduler.entities.SchedulerStatus;

public interface SchedulerRepository extends JpaRepository<SchedulerStatus, Long> {

}
