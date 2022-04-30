package settergetter;

public class ClockOnOffGtSt {

    public String date;
    public String intime;
    public String outtime;
    public boolean isweeklyoff;
    public boolean isonleave;
    public boolean isclockonforday;
    public boolean isclockoffforday;



    public boolean isIsclockonforday() {
        return isclockonforday;
    }

    public void setIsclockonforday(boolean isclockonforday) {
        this.isclockonforday = isclockonforday;
    }

    public boolean isIsclockoffforday() {
        return isclockoffforday;
    }

    public void setIsclockoffforday(boolean isclockoffforday) {
        this.isclockoffforday = isclockoffforday;
    }




    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIntime() {
        return intime;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public String getOuttime() {
        return outtime;
    }

    public void setOuttime(String outtime) {
        this.outtime = outtime;
    }

    public boolean isIsweeklyoff() {
        return isweeklyoff;
    }

    public void setIsweeklyoff(boolean isweeklyoff) {
        this.isweeklyoff = isweeklyoff;
    }

    public boolean isIsonleave() {
        return isonleave;
    }

    public void setIsonleave(boolean isonleave) {
        this.isonleave = isonleave;
    }


}
