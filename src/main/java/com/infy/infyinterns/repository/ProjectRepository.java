package com.infy.infyinterns.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infy.infyinterns.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Integer>
{

    // add methods if required

}
