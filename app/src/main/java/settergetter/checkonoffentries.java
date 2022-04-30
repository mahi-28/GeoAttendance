package settergetter;

public class checkonoffentries {

  //  private String date,intime,standdardintime,outtime,standardouttime;
    private int exceptionInStatus,exceptionOutStatus,clockOnOffStatus;
    private String EmpInApproveEmp,EmpOutApproveEmp,dayStatus,leaveId;

    public String getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(String leaveId) {
        this.leaveId = leaveId;
    }

    public String getDayStatus() {
        return dayStatus;
    }

    public void setDayStatus(String dayStatus) {
        this.dayStatus = dayStatus;
    }

    public String getEmpInApproveEmp() {
        return EmpInApproveEmp;
    }

    public void setEmpInApproveEmp(String empInApproveEmp) {
        EmpInApproveEmp = empInApproveEmp;
    }

    public String getEmpOutApproveEmp() {
        return EmpOutApproveEmp;
    }

    public void setEmpOutApproveEmp(String empOutApproveEmp) {
        EmpOutApproveEmp = empOutApproveEmp;
    }

    public int getClockOnOffStatus() {
        return clockOnOffStatus;
    }

    public void setClockOnOffStatus(int clockOnOffStatus) {
        this.clockOnOffStatus = clockOnOffStatus;
    }

    private String clockDate,rosterStartTime,approvedStartTime,actualStartTime,rosterEndTime,approvedActualEndTime,actualEndTime,strBgColor,exceptionEmpInReason,exceptionEmpOutReason;

    public String getClockDateinFormat() {
        return clockDateinFormat;
    }

    public void setClockDateinFormat(String clockDateinFormat) {
        this.clockDateinFormat = clockDateinFormat;
    }

    private String clockDateinFormat;
    private String rosterSummary,daySummary,variance,othour;

    public String getOthour() {
        return othour;
    }

    public void setOthour(String othour) {
        this.othour = othour;
    }

    public String getVariance() {

        return variance;
    }

    public void setVariance(String variance) {
        this.variance = variance;
    }

    public String getDaySummary() {
        return daySummary;

    }

    public void setDaySummary(String daySummary) {
        this.daySummary = daySummary;
    }

    public String getRosterSummary() {
        return rosterSummary;
    }

    public void setRosterSummary(String rosterSummary) {
        this.rosterSummary = rosterSummary;
    }



    public int getExceptionInStatus() {
        return exceptionInStatus;
    }

    public String getExceptionEmpInReason() {
        return exceptionEmpInReason;
    }

    public void setExceptionEmpInReason(String exceptionEmpInReason) {
        this.exceptionEmpInReason = exceptionEmpInReason;
    }

    public String getExceptionEmpOutReason() {
        return exceptionEmpOutReason;
    }

    public void setExceptionEmpOutReason(String exceptionEmpOutReason) {
        this.exceptionEmpOutReason = exceptionEmpOutReason;
    }

    public void setExceptionInStatus(int exceptionInStatus) {
        this.exceptionInStatus = exceptionInStatus;
    }

    public int getExceptionOutStatus() {
        return exceptionOutStatus;
    }

    public void setExceptionOutStatus(int exceptionOutStatus) {
        this.exceptionOutStatus = exceptionOutStatus;
    }

    public String getClockDate() {
        return clockDate;
    }

    public void setClockDate(String clockDate) {
        this.clockDate = clockDate;
    }

    public String getRosterStartTime() {
        return rosterStartTime;
    }

    public void setRosterStartTime(String rosterStartTime) {
        this.rosterStartTime = rosterStartTime;
    }

    public String getApprovedStartTime() {
        return approvedStartTime;
    }

    public void setApprovedStartTime(String approvedStartTime) {
        this.approvedStartTime = approvedStartTime;
    }

    public String getActualStartTime() {
        return actualStartTime;
    }

    public void setActualStartTime(String actualStartTime) {
        this.actualStartTime = actualStartTime;
    }

    public String getRosterEndTime() {
        return rosterEndTime;
    }

    public void setRosterEndTime(String rosterEndTime) {
        this.rosterEndTime = rosterEndTime;
    }

    public String getApprovedActualEndTime() {
        return approvedActualEndTime;
    }

    public void setApprovedActualEndTime(String approvedActualEndTime) {
        this.approvedActualEndTime = approvedActualEndTime;
    }

    public String getActualEndTime() {
        return actualEndTime;
    }

    public void setActualEndTime(String actualEndTime) {
        this.actualEndTime = actualEndTime;
    }

    public String getStrBgColor() {
        return strBgColor;
    }

    public void setStrBgColor(String strBgColor) {
        this.strBgColor = strBgColor;
    }
}
