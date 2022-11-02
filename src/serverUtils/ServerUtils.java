package serverUtils;

import bean.Meeting;
import bean.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * @author
 * 服务器会用到的一些工具
 */
public class ServerUtils {
    /**
     * 查看users 是否存在一个叫username的人
     * @param users     用户列表
     * @param username  用户名
     * @return  存在返回true
     */
    public static boolean isExist(ArrayList<User> users,String username){

        for(User user : users){
            if(username.equals(user.getUsername())){
                return true;
            }
        }

        return false;
    }

    /**
     * 查看username的人的密码是否正确
     * @param users 用户列表
     * @param username  用户名
     * @param password  密码
     * @return 用户名存在且密码正确返回true，否则返回false
     */
    public static User pwdIsTrue(ArrayList<User> users,String username,String password){

        for(User user : users){
            if(username.equals(user.getUsername())){
                if(user.getPassword().equals(password)){
                    return user;
                }
            }
        }


        return null;
    }

    /**
     * 传入一个会议和 开始，结束时间，
     * @param meeting   会议
     * @param start 开始时间
     * @param end   结束时间
     * @return  如果时间有重合，就返回true,不然返回false
     */
    public static boolean isCrossTime(Meeting meeting, LocalDateTime start,LocalDateTime end){

        if(meeting.getStartTime().isAfter(start) && meeting.getStartTime().isBefore(end) ||
        meeting.getStartTime().isEqual(end) || meeting.getStartTime().isEqual(start)){
            return true;
        }


        if(meeting.getEndTime().isAfter(start) &&  meeting.getEndTime().isBefore(end)  ||
                meeting.getEndTime().isEqual(end) || meeting.getEndTime().isEqual(start)){
            return true;
        }

        return false;
    }

    public static boolean isCrossTime(Meeting meeting, LocalDateTime start,LocalDateTime end,boolean flag){

        if(meeting ==null){
            return false;
        }

        if(meeting.getStartTime().isAfter(start) && meeting.getEndTime().isBefore(end)
                || meeting.getStartTime().isEqual(start) && (meeting.getEndTime().isBefore(end))
                || (meeting.getEndTime().isEqual(end) && (meeting.getStartTime().isAfter(start)))
                || meeting.getStartTime().isEqual(start) && meeting.getEndTime().isEqual(end)
        ){
            return true;
        }

        return false;
    }


}
