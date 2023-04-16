package com.ey.scheduler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.client.RestTemplate;

import com.ey.scheduler.entities.SchedulerStatus;
import com.ey.scheduler.exception.FileDownloadException;
import com.ey.scheduler.repos.SchedulerRepository;
import com.ey.scheduler.serviceImpl.SchedularServiceImpl;

public class SchedulerServiceImplTest {

	@Mock
	public SchedulerRepository schedulerRepository;

	@Mock
	public RestTemplate restTemplate;

	@Mock
	public JavaMailSender mailSender;

	@InjectMocks
	public SchedularServiceImpl schedularServiceImpl;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testDownloadFileSuccess() throws IOException, FileDownloadException {
		ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>("Test".getBytes(), HttpStatus.OK);
		when(restTemplate.getForEntity("http://test.com", byte[].class)).thenReturn(responseEntity);
		File file = new File("testFile.txt");
		Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.eq(byte[].class)))
				.thenReturn(responseEntity);

		schedularServiceImpl.downloadFile();
		Assertions.assertEquals(file.exists(), true);

	}

	@Test
	public void testDownloadFileFailed() {
		when(restTemplate.getForEntity("http://test.com", byte[].class))
				.thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
		try {
			schedularServiceImpl.downloadFile();
		} catch (FileDownloadException e) {
			Assertions.assertEquals(e.getMessage(), "Error downloading file");
		}

	}

	@Test
	public void testStatusUpdate() {
		SchedulerStatus jobStatus = new SchedulerStatus();
		jobStatus.setStatus("SUCCESS");
		jobStatus.setLastUpdated(LocalDateTime.now());
		jobStatus.setJobDescription("File downloaded");
		when(schedulerRepository.save(jobStatus)).thenReturn(jobStatus);
		String savedJobStatus = schedularServiceImpl.statusUpdate("SUCCESS", "File downloaded");

		Assertions.assertEquals("Success", savedJobStatus);
		Assertions.assertEquals("SUCCESS", jobStatus.getStatus());
		Assertions.assertNotNull(jobStatus.getLastUpdated());
		Assertions.assertEquals("File downloaded", jobStatus.getJobDescription());
	}

	@Test
	public void testSendMail() {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo("test@test.com");
		message.setSubject("File Download Job Failed");
		message.setText("Hi,\n The file you are trying to download has failed.\n");
		schedularServiceImpl.sendMail();
	}

	@Test
	public void testSchedulerStatusConstructor() {
		String status = "SUCCESS";
		String jobDescription = "Job completed successfully";
		LocalDateTime lastUpdated = LocalDateTime.now();

		SchedulerStatus schedulerStatus = new SchedulerStatus(status, jobDescription, lastUpdated);

		assertNotNull(schedulerStatus);
		assertEquals(lastUpdated, schedulerStatus.getLastUpdated());
	}
}
