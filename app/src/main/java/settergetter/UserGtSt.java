package settergetter;

public class UserGtSt {


    public String useruid;
    public String code;
    public String name;
    public String pin;
    public String username;
    public String starttime;
    public String endtime;

    public boolean isIsgeoEnabled() {
        return isgeoEnabled;
    }

    public void setIsgeoEnabled(boolean isgeoEnabled) {
        this.isgeoEnabled = isgeoEnabled;
    }

    public boolean isgeoEnabled;

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }



    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String designation;
    public String getUseruid() {
        return useruid;
    }

    public void setUseruid(String useruid) {
        this.useruid = useruid;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
