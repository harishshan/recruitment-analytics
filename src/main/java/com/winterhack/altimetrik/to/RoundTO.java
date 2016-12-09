package com.winterhack.altimetrik.to;

import java.io.Serializable;

public class RoundTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String analyticalSkill;

    private String codingSkill;

    private String attitude;

    private String problemSolvingSkill;

    private String interviewerComment;

    private String recruterComment;

    public String getAnalyticalSkill() {
        return analyticalSkill;
    }

    public void setAnalyticalSkill(String analyticalSkill) {
        this.analyticalSkill = analyticalSkill;
    }

    public String getCodingSkill() {
        return codingSkill;
    }

    public void setCodingSkill(String codingSkill) {
        this.codingSkill = codingSkill;
    }

    public String getAttitude() {
        return attitude;
    }

    public void setAttitude(String attitude) {
        this.attitude = attitude;
    }

    public String getProblemSolvingSkill() {
        return problemSolvingSkill;
    }

    public void setProblemSolvingSkill(String problemSolvingSkill) {
        this.problemSolvingSkill = problemSolvingSkill;
    }

    public String getInterviewerComment() {
        return interviewerComment;
    }

    public void setInterviewerComment(String interviewerComment) {
        this.interviewerComment = interviewerComment;
    }

    public String getRecruterComment() {
        return recruterComment;
    }

    public void setRecruterComment(String recruterComment) {
        this.recruterComment = recruterComment;
    }

}
