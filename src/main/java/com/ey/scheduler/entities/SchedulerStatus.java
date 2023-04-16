package com.ey.scheduler.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table
@NoArgsConstructor
public class SchedulerStatus {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	String status;
	String jobDescription;
	LocalDateTime lastUpdated;

	public SchedulerStatus(String status, String jobDescription, LocalDateTime lastUpdated) {
		super();
		this.status = status;
		this.jobDescription = jobDescription;
		this.lastUpdated = lastUpdated;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getJobDescription() {
		return jobDescription;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(LocalDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

}
