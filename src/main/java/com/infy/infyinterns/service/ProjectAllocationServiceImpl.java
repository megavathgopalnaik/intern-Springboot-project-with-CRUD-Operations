package com.infy.infyinterns.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infy.infyinterns.dto.MentorDTO;
import com.infy.infyinterns.dto.ProjectDTO;
import com.infy.infyinterns.entity.Mentor;
import com.infy.infyinterns.entity.Project;
import com.infy.infyinterns.exception.InfyInternException;
import com.infy.infyinterns.repository.MentorRepository;
import com.infy.infyinterns.repository.ProjectRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProjectAllocationServiceImpl implements ProjectAllocationService {
	
	@Autowired
	private ProjectRepository projectRepo;
	
	@Autowired
	private MentorRepository mentorRepo;
	

	@Override
	public Integer allocateProject(ProjectDTO project) throws InfyInternException {
         Optional<Mentor>opt=mentorRepo.findById(project.getMentorDTO().getMentorId());
         Mentor mentor=opt.orElseThrow(()->new InfyInternException("Service.MENTOR_NOT_FOUND"));
         if(mentor.getNumberOfProjectsMentored()>=3) {
        	 throw new InfyInternException("Service.CANNOT_ALLOCATE_PROJECT");
         }
         Project newProject=new Project();
         newProject.setIdeaOwner(project.getIdeaOwner());
         newProject.setMentor(mentor);
         mentor.setNumberOfProjectsMentored(mentor.getNumberOfProjectsMentored()+1);
         newProject.setProjectId(project.getProjectId());
         newProject.setProjectName(project.getProjectName());
         newProject.setReleaseDate(project.getReleaseDate());
         Project newproj=projectRepo.save(newProject);
		return newproj.getProjectId();
	}

	
	@Override
	public List<MentorDTO> getMentors(Integer numberOfProjectsMentored) throws InfyInternException {
		List<Mentor>opt=mentorRepo.findByNumberOfProjectsMentored(numberOfProjectsMentored);
		if(opt.isEmpty()) {
			throw new InfyInternException("Service.MENTOR_NOT_FOUND");
		}
		List<MentorDTO> mentorDTO=new ArrayList<MentorDTO>();
		for(Mentor mentor:opt ) {
			MentorDTO dto=new MentorDTO();
			dto.setMentorId(mentor.getMentorId());
			dto.setMentorName(mentor.getMentorName());
			dto.setNumberOfProjectsMentored(mentor.getNumberOfProjectsMentored());
			mentorDTO.add(dto);
		}
		return mentorDTO;
	}


	@Override
	public void updateProjectMentor(Integer projectId, Integer mentorId) throws InfyInternException {
		Optional<Mentor> men=mentorRepo.findById(mentorId);
		Mentor mentor=men.orElseThrow(()->new InfyInternException("Service.MENTOR_NOT_FOUND"));
		if(mentor.getNumberOfProjectsMentored()>=3) {
			throw new InfyInternException("Service.CANNOT_ALLOCATE_PROJECT");
		}
		Optional<Project>proj=projectRepo.findById(projectId);
		Project project=proj.orElseThrow(()->new InfyInternException("Service.PROJECT_NOT_FOUND"));
		project.setMentor(mentor);
        mentor.setNumberOfProjectsMentored(mentor.getNumberOfProjectsMentored()+1);
	}

	@Override
	public void deleteProject(Integer projectId) throws InfyInternException {
		Optional<Project>proj=projectRepo.findById(projectId);
		Project project=proj.orElseThrow(()->new InfyInternException("Service.PROJECT_NOT_FOUND"));
		Mentor mentor=project.getMentor();
		mentor.setNumberOfProjectsMentored(mentor.getNumberOfProjectsMentored()-1);
		project.setMentor(null);
		projectRepo.delete(project);
		
	}
}