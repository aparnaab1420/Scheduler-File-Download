package com.ey.scheduler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.ey.scheduler.controller.SchedulerController;
import com.ey.scheduler.exception.FileDownloadException;
import com.ey.scheduler.service.SchedularService;

@SpringBootTest
public class SchedulerControllerTest {

	@Mock
	private SchedularService schedularService;

	@InjectMocks
	private SchedulerController schedulerController;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testScheduleJob() throws FileDownloadException {

		Mockito.doNothing().when(schedularService).downloadFile();
		schedulerController.scheduleJob();
		Mockito.verify(schedularService, Mockito.times(1)).downloadFile();
	}

	@BeforeEach
	public void setUpException() throws Exception {
		Mockito.doThrow(new FileDownloadException()).when(schedularService).downloadFile();
	}

	@Test
	public void testScheduleJob_FileDownloadException() throws FileDownloadException {
		schedulerController.scheduleJob();
	}
}
