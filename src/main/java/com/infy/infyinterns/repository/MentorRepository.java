package com.infy.infyinterns.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infy.infyinterns.entity.Mentor;

public interface MentorRepository extends JpaRepository<Mentor, Integer>
{
  List<Mentor> findByNumberOfProjectsMentored(Integer numberOfProjectsMentored);
}
