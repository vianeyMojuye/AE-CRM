package com.aecrm;

public class HistoryModel {

  private   String date;
  private   String issue;
  private   String name;
  private   String purpose;

    public HistoryModel(String date , String issue, String name, String purpose)
    {
        this.date = date;
        this.issue = issue;
        this.name = name;
        this.purpose = purpose;
    }

    public HistoryModel(String date , String name, String purpose)
    {
        this.date = date;
        this.name = name;
        this.purpose = purpose;
    }

    public String getDate() {
        return date;
    }

    public String getIssue() {
        return issue;
    }

    public String getName() {
        return name;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}
