package com.ey.scheduler.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import com.ey.scheduler.exception.FileDownloadException;
import com.ey.scheduler.service.SchedularService;

@Controller
public class SchedulerController {

	@Autowired
	private SchedularService schedularService;

	final Logger LOGGER = LoggerFactory.getLogger(SchedulerController.class);

	@Scheduled(fixedRate = 20000)
	public void scheduleJob() {
		LOGGER.info("Application started");
		try {
			schedularService.downloadFile();
		} catch (FileDownloadException e) {
			e.printStackTrace();
			LOGGER.error("Error downloading file!");
		}
	}
}
