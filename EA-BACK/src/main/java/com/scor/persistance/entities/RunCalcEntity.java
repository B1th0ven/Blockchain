package com.scor.persistance.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "RUN_CALC", schema = "dbo", catalog = "EXPAN")
public class RunCalcEntity implements Serializable {

    private int rclcId;
    private int rclcRunId;
    private Timestamp rclcStartDate;
    private Timestamp rclcEndDate;
    private String rclcUserCreation;
    private String rclcStatus;
    private String rclcLogPath;
    private String rclcStep;
    private String rclcDescription;
    private String rclcStatusEa;

    private String rclcVersion;
    
    public void setRclcId(int rclcId) {
        this.rclcId = rclcId;
    }

    public void setRclcRunId(int rclcRunId) {
        this.rclcRunId = rclcRunId;
    }

    public void setRclcStartDate(Timestamp rclcStartDate) {
        this.rclcStartDate = rclcStartDate;
    }

    public void setRclcEndDate(Timestamp rclcEndDate) {
        this.rclcEndDate = rclcEndDate;
    }

    public void setRclcUserCreation(String rclcUserCreation) {
        this.rclcUserCreation = rclcUserCreation;
    }

    public void setRclcStatus(String rclcStatus) {
        this.rclcStatus = rclcStatus;
    }

    public void setRclcLogPath(String rclcLogPath) {
        this.rclcLogPath = rclcLogPath;
    }

    public void setRclcStep(String rclcStep) {
        this.rclcStep = rclcStep;
    }

    public void setRclcDescription(String rclcDescription) {
        this.rclcDescription = rclcDescription;
    }



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RCLC_ID")
    public int getRclcId() {
        return rclcId;
    }

    @Basic
    @Column(name = "RCLC_RUN_ID")
    public int getRclcRunId() {
        return rclcRunId;
    }

    @Basic
    @Column(name = "RCLC_START_DATE")
    public Timestamp getRclcStartDate() {
        return rclcStartDate;
    }

    @Basic
    @Column(name = "RCLC_END_DATE")
    public Timestamp getRclcEndDate() {
        return rclcEndDate;
    }

    @Basic
    @Column(name = "RCLC_USER_CREATION")
    public String getRclcUserCreation() {
        return rclcUserCreation;
    }

    @Basic
    @Column(name = "RCLC_STATUS")
    public String getRclcStatus() {
        return rclcStatus;
    }

    @Basic
    @Column(name = "RCLC_LOG_PATH")
    public String getRclcLogPath() {
        return rclcLogPath;
    }

    @Basic
    @Column(name = "RCLC_STEP")
    public String getRclcStep() {
        return rclcStep;
    }

    @Basic
    @Column(name = "RCLC_DESCRIPTION")
    public String getRclcDescription() {
        return rclcDescription;
    }

    @Basic
    @Column(name = "RCLC_STATUS_EA")
	public String getRclcStatusEa() {
		return rclcStatusEa;
	}

	public void setRclcStatusEa(String rclcStatusEa) {
		this.rclcStatusEa = rclcStatusEa;
	}

	@Basic
    @Column(name = "RCLC_VERSION")
	public String getRclcVersion() {
		return this.rclcVersion;
	}

	public void setRclcVersion(String rclcVersion) {
		this.rclcVersion = rclcVersion;
	}

}
