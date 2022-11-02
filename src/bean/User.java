package bean;


import serverUtils.ServerUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 参加会议的用户对象 保存了一个会议号列表
 */
public class User implements Serializable {
    private String username;
    private String password;

    private ArrayList<String> meetingsId;

    /**
     * 构造函数，如果有一个User叫作username,password
     * @param username
     * @param password
     */
    public User(String username,String password){

        this.username = username;
        this.password = password;

        meetingsId = new ArrayList<String>();
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }

    /**
     * 如果时间有冲突，返回true 否则返回false
     * @param meeting
     * @return
     */
    /**
     * 是否传入的会议和 用户所拥有的会议号重叠
     * @param meetings
     * @param meeting
     * @return
     */
    public boolean isCross(ArrayList<Meeting> meetings,Meeting meeting){

        //传入的meetings查找哪一个meeting里面的 creator 或者 参会者是这个人，然后判断和传入的meeting是否重合

        for(Meeting temp : meetings){

            if(temp.inMeeting(this.getUsername())){
                if(ServerUtils.isCrossTime(meeting,temp.getStartTime(),temp.getEndTime())){
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 给会议列表加一个会议号
     * @param MeetingId
     */
    public void addMeeting(String MeetingId){
        meetingsId.add(MeetingId);
    }

    /**
     * 得到所有会议号id
     * @return
     */
    public ArrayList<String> getMeetingsId() {
        return meetingsId;
    }


    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", meetingsId=" + meetingsId +
                '}';
    }
}
