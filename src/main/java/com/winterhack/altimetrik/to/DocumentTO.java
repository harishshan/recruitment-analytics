package com.winterhack.altimetrik.to;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DocumentTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String analyticsKey;

    private String emailId;

    private String name;

    private String fileName;

    private String status;

    private List<RoundTO> roundTO;

    public String getAnalyticsKey() {
        return analyticsKey;
    }

    public void setAnalyticsKey(String analyticsKey) {
        this.analyticsKey = analyticsKey;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<RoundTO> getRoundTO() {
        return roundTO;
    }

    public void setRoundTO(List<RoundTO> roundTO) {
        if (this.roundTO == null) {
            this.roundTO = new ArrayList<RoundTO>();
        }
        this.roundTO.addAll(roundTO);
    }

    public void setRoundTO(RoundTO roundTO) {
        if (this.roundTO == null) {
            this.roundTO = new ArrayList<RoundTO>();
        }
        this.roundTO.add(roundTO);
    }

}
