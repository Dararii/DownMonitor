package main;

public class DownTime {
    private int id;
    private long timestamp;
    private long durationInSecond;
    private String defect, reason, op1, op2;

    public DownTime(int id, long timestamp, long durationInSecond, String defect, String reason) {
        this.id = id;
        this.timestamp = timestamp;
        this.durationInSecond = durationInSecond;
        this.defect = defect;
        this.reason = reason;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getDurationInSecond() {
        return durationInSecond;
    }

    public void setDurationInSecond(long durationInSecond) {
        this.durationInSecond = durationInSecond;
    }

    public String getDefect() {
        return defect;
    }

    public void setDefect(String defect) {
        this.defect = defect;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getOp1() {
        return op1;
    }

    public void setOp1(String op1) {
        this.op1 = op1;
    }

    public String getOp2() {
        return op2;
    }

    public void setOp2(String op2) {
        this.op2 = op2;
    }

    public enum Defect {
        A, B, C, D, E;
    }
}
