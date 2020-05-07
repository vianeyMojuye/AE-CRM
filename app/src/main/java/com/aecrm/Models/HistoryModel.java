package com.aecrm.Models;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;

public class HistoryModel {

  String  visitId;
  String Party;
  String PartyCoordinates;
  String EmployeeCoordinates;
  String Order;
  String Payment;
  String Issue;
  String DateVisited;
  String CheckInTime;
  String CheckOutTime;
  String Comments;
  String modelType;



  public HistoryModel(String  visitId,String Party,String PartyCoordinates,String EmployeeCoordinates,
          String Order,String Payment, String Issue, String DateVisited,String CheckInTime,String CheckOutTime,String Comments,String modelType)
  {
      this.visitId = visitId;
      this.Party = Party;
      this.PartyCoordinates = PartyCoordinates;
      this.EmployeeCoordinates = EmployeeCoordinates;
      this.Order = Order;
      this.Payment = Payment;
      this.Issue = Issue;
      this.DateVisited = DateVisited;
      this.CheckInTime = CheckInTime;
      this.CheckOutTime = CheckOutTime;
      this.Comments = Comments;
      this.modelType = modelType;
   }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }

    public String getModelType() {
        return modelType;
    }

    public void setPayment(String payment) {
        Payment = payment;
    }

    public void setPartyCoordinates(String partyCoordinates) {
        PartyCoordinates = partyCoordinates;
    }

    public void setOrder(String order) {
        Order = order;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public void setEmployeeCoordinates(String employeeCoordinates) {
        EmployeeCoordinates = employeeCoordinates;
    }

    public void setDateVisited(String DateVisited) {
        DateVisited = DateVisited;
    }

    public void setComments(String comments) {
        Comments = comments;
    }

    public void setCheckOutTime(String checkOutTime) {
        CheckOutTime = checkOutTime;
    }

    public void setCheckInTime(String checkInTime) {
        CheckInTime = checkInTime;
    }

    public String getVisitId() {
        return visitId;
    }

    public String getPayment() {
        return Payment;
    }

    public String getOrder() {
        return Order;
    }

    public String getDateVisited() {
        return DateVisited;
    }

    public String getComments() {
        return Comments;
    }

    public String getCheckOutTime() {
        return CheckOutTime;
    }

    public String getCheckInTime() {
        return CheckInTime;
    }

    public String getPartyCoordinates() {
        return PartyCoordinates;
    }

    public String getEmployeeCoordinates() {
        return EmployeeCoordinates;
    }

    public String getIssue() {
        return Issue;
    }

    public void setIssue(String issue) {
        Issue = issue;
    }

    public String getParty() {
        return Party;
    }

    public void setParty(String party) {
        Party = party;
    }
}
