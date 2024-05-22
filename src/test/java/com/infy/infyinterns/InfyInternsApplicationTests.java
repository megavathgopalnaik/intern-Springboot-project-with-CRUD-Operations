package com.infy.infyinterns;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.infy.infyinterns.dto.MentorDTO;
import com.infy.infyinterns.dto.ProjectDTO;
import com.infy.infyinterns.entity.Mentor;
import com.infy.infyinterns.exception.InfyInternException;
import com.infy.infyinterns.repository.MentorRepository;
import com.infy.infyinterns.service.ProjectAllocationService;
import com.infy.infyinterns.service.ProjectAllocationServiceImpl;

@SpringBootTest
public class InfyInternsApplicationTests {

	@Mock
	private MentorRepository mentorRepository;

	@InjectMocks
	private ProjectAllocationService projectAllocationService = new ProjectAllocationServiceImpl();

	@Test
	public void allocateProjectCannotAllocateTest() throws Exception {
     ProjectDTO projectDTO=new ProjectDTO();
     projectDTO.setIdeaOwner(10009);
     projectDTO.setProjectName("IOS Shopping App");
     projectDTO.setReleaseDate(LocalDate.of(2020, 10,22));
     MentorDTO mentorDTO= new MentorDTO();
     mentorDTO.setMentorId(1009);
     projectDTO.setMentorDTO(mentorDTO);
     Mentor mentor= new  Mentor();
     mentor.setMentorId(1009);
     mentor.setMentorName("David");
     mentor.setNumberOfProjectsMentored(4);
     Mockito.<Optional<Mentor>>when(mentorRepository.findById(mentorDTO.getMentorId())).thenReturn(Optional.of(mentor));
     InfyInternException ie=Assertions.assertThrows(InfyInternException.class,
       ()->projectAllocationService.allocateProject(projectDTO));
     Assertions.assertEquals("Service.CANNOT_ALLOCATE_PROJECT", ie.getMessage());
     
     
		

	}

	@Test
	public void allocateProjectMentorNotFoundTest() throws Exception {
	
		 ProjectDTO projectDTO=new ProjectDTO();
	     projectDTO.setIdeaOwner(10009);
	     projectDTO.setProjectName("IOS Shopping App");
	     projectDTO.setReleaseDate(LocalDate.of(2020, 10,22));
	     MentorDTO mentorDTO= new MentorDTO();
	     mentorDTO.setMentorId(1009);
	     projectDTO.setMentorDTO(mentorDTO);
	    
	     Mockito.<Optional<Mentor>>when(mentorRepository.findById(mentorDTO.getMentorId())).thenReturn(Optional.empty());
	     InfyInternException ie=Assertions.assertThrows(InfyInternException.class,
	       ()->projectAllocationService.allocateProject(projectDTO));
	     Assertions.assertEquals("Service.MENTOR_NOT_FOUND", ie.getMessage());
	     
	}
}