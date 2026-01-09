package com.workilnk.application;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByTaskId(Long taskId);
    List<Application> findByWorkerId(Long workerId);
}

