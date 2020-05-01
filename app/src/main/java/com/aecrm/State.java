package com.aecrm;

public class State {

    String StateId;
    String State;
    String StateCode;
    String Country;
    String Status ;


    public State(String StateId,String State, String StateCode, String Country,String Status )
    {
       this.StateId = StateId;
       this.State = State;
       this.StateCode = StateCode;
       this.Country  =  Country;
       this.Status  = Status;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public void setState(String state) {
        State = state;
    }

    public void setStateCode(String stateCode) {
        StateCode = stateCode;
    }

    public void setStateId(String stateId) {
        StateId = stateId;
    }

    public String getStatus() {
        return Status;
    }

    public String getCountry() {
        return Country;
    }

    public String getState() {
        return State;
    }

    public String getStateCode() {
        return StateCode;
    }

    public String getStateId() {
        return StateId;
    }
}
