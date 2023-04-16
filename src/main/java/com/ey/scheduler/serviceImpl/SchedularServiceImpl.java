package com.ey.scheduler.serviceImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ey.scheduler.entities.SchedulerStatus;
import com.ey.scheduler.exception.FileDownloadException;
import com.ey.scheduler.repos.SchedulerRepository;
import com.ey.scheduler.service.SchedularService;

@Service
public class SchedularServiceImpl implements SchedularService {

	@Autowired
	public SchedulerRepository schedulerRepository;

	@Autowired
	public RestTemplate restTemplate;

	@Autowired
	JavaMailSender mailSender;

	@Value("${sampleUrl}")
	private String fileUrl;

	@Value("${folderPath}")
	private String downloadToFolder;

	@Value("${fileName}")
	String fileName;

	final Logger LOGGER = LoggerFactory.getLogger(SchedularServiceImpl.class);

	public void downloadFile() throws FileDownloadException {
		try {

			ResponseEntity<byte[]> response = restTemplate.getForEntity(fileUrl, byte[].class);
			if (response.getStatusCode() == HttpStatus.OK) {
				try (FileOutputStream out = new FileOutputStream(new File(downloadToFolder, fileName))) {
					out.write(response.getBody());
					statusUpdate("SUCCESS", "File downloaded");
					LOGGER.info("Job Done Successfully!");
				}
			}
		} catch (Exception e) {

			statusUpdate("FAILED", "File download failed");
			sendMail();
		}

	}

	public String statusUpdate(String status, String jobDescription) {
		SchedulerStatus jobStatus = new SchedulerStatus();
		jobStatus.setStatus(status);
		jobStatus.setLastUpdated(LocalDateTime.now());
		jobStatus.setJobDescription(jobDescription);
		schedulerRepository.save(jobStatus);
		return "Success";
	}

	public void sendMail() {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo("testerm091@gmail.com");
		message.setSubject("File Download Job Failed");
		message.setText("Hi,\n The file you are trying to download has failed.\n");
		mailSender.send(message);
		LOGGER.info("Failure! Mail Sent Successfully.");
	}
}
