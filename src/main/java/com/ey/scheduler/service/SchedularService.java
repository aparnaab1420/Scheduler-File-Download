package com.ey.scheduler.service;

import com.ey.scheduler.exception.FileDownloadException;

public interface SchedularService {

	public void downloadFile() throws FileDownloadException;

}
