package bean;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author lrq
 * 会议
 */
public class Meeting implements Serializable {

    public List<String> getUsers() {
        return users;
    }

    /**
     * 得到的名单是以..为分隔符的
     * @return
     */
    public String getUsers_names(){

        String info = "";

        for(String user : users){
            info += ".." + user;
        }

        return info;
    }

    private List<String> users;
    private String meetingId;
    private String creator;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String meetingTitle;


    public Meeting(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getCreator() {
        return creator;
    }

    public boolean inMeeting(String username){
        if(creator.equals(username)){
            return true;
        }

        for(String name : users){
            if(username.equals(name)){
                return true;
            }
        }

        return false;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getMeetingTitle() {
        return meetingTitle;
    }

    /***
     * 构造方法
     * @param creator
     * @param users
     * @param meetingTitle
     * @param meetingId
     * @param startTime
     * @param endTime
     */

    public Meeting(String creator, List<String> users, String meetingTitle, String meetingId, LocalDateTime startTime,
                   LocalDateTime endTime) {
        this.users = users;
        this.meetingId = meetingId;
        this.creator = creator;
        this.startTime = startTime;
        this.endTime = endTime;
        this.meetingTitle = meetingTitle;
    }


    @Override
    public String toString() {
        return "Meeting{" +
                "users=" + users +
                ", meetingId='" + meetingId + '\'' +
                ", creator='" + creator + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
