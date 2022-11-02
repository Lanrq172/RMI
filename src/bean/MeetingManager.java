package bean;

import interface_.MMInterface;
import serverUtils.ServerUtils;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 将要被客户端调用的RMI对象
 * @author lrq
 */
public class MeetingManager extends UnicastRemoteObject implements MMInterface{

    private List<User> users;
    private List<Meeting> meetings;
    private static int MEETING_NUM =  0;
    //*************************************************************返回提示****************************************
    private static String NULL_PARAMETERS = "参数不能有输入为空的参数";
    private static String REPEATED_USERNAME = "该用户名已存在！";


    //************************************************************************************************************
    public MeetingManager() throws RemoteException {
        users = new ArrayList<>();
        meetings = new ArrayList<>();
    }




    /**
     *  用户注册 成功返回true,失败返回false
     */
    @Override
    public String registerUser (String username, String password)  throws RemoteException{
        if(username == null || password == null){
            return NULL_PARAMETERS;
        }
        //是否有重复的名字
        boolean flag = false;

       if(ServerUtils.isExist((ArrayList<User>) users,username)){
           return REPEATED_USERNAME;
       }


        users.add(new User(username,password));

        return "成功创建用户:>" + username;
    }


    /**
     * 返回创建结果
     * @param username1 用户名1
     * @param password  密码
     * @param meetingTitle  会议标题
     * @param otherUsernames 用户名2
     * @param startTime 会议开始时间
     * @param endTime    会议结束时间
     * @return 1、成功 2、用户名1不存在 3、用户名1密码错误 4、用户名2 不存在 5 Date endTime 比 startTime早 6 两个用户有一个会议重叠
     **/
    @Override
    public String createMeeting(String username1, String password, String meetingTitle, String[] otherUsernames,
                                LocalDateTime startTime, LocalDateTime endTime)
            throws RemoteException{

        if(null == ServerUtils.pwdIsTrue((ArrayList<User>) users,username1,password)){
            return "创建会议的用户不存在或者密码不正确";
        }


        for(String un : otherUsernames){

            System.out.println(un);
            System.out.println(users);

            if(!ServerUtils.isExist((ArrayList<User>) users,un)){
                return "参加会议的用户: " + un + " 不存在于该系统！";
            }
        }

        if(startTime.isAfter(endTime) || startTime.isEqual(endTime)){
            return "结束的时间比开始的时间早或者结束的时间和开始时间相等";
        }


        if(getUserByName(username1).isCross( (ArrayList<Meeting>) meetings,new Meeting(startTime,endTime))){
            return "创建会议的用户时间和其他会议有重合!";
        }

        for(String ou : otherUsernames){

            if(getUserByName(ou).isCross((ArrayList<Meeting>) meetings,new Meeting(startTime,endTime))){
                return  "用户 " + ou + "的其他会议与该会议有冲突" ;
            }

        }

        ArrayList<String> user_s = new ArrayList<>();
        getUserByName(username1).addMeeting(String.valueOf(MEETING_NUM));


        for(String tempUser : otherUsernames){
                user_s.add(tempUser);
                getUserByName(tempUser).addMeeting(String.valueOf(MEETING_NUM));
        }

        meetings.add(new Meeting(username1,user_s,meetingTitle,String.valueOf(MEETING_NUM++),startTime,endTime));

        return "会议创建成功!";
    }


    /**
     * c查询一个人的会议，返回信息
     * @param username  用户名
     * @param password  密码
     * @param startTime 查询开始时间
     * @param endTime   查询结束时间
     * @return  返回会议信息
     */
    @Override
    public String queryMeeting(String username, String password, LocalDateTime startTime, LocalDateTime endTime)
            throws RemoteException{

        User user;
        if( (user = ServerUtils.pwdIsTrue((ArrayList<User>) users,username,password)) == null){
            return "没有该用户或者密码错了啦\n";

        }

        ArrayList<Meeting> meetingOfSomeUser = new ArrayList<>();


        for(String id : user.getMeetingsId()){
            if(ServerUtils.isCrossTime(getMeetingById(id),startTime,endTime,true)){
                meetingOfSomeUser.add(getMeetingById(id));
            }
        }


        Collections.sort(meetingOfSomeUser, (o1, o2) -> {
            if(o1.getStartTime().isBefore(o2.getStartTime())){
                return -1;
            }else {
                return 1;
            }
        });

        //排序好了，然后将其信息打印

        String info = "SUCCESS\n";


        for(Meeting mt : meetingOfSomeUser){
            info += mt.getStartTime() + "___" + mt.getEndTime() + "___" + mt.getMeetingTitle()  + "___" +
                     mt.getCreator()  +  mt.getUsers_names() + "___" + mt.getMeetingId()
                    + "\n";
            }

        if(info.split("\n").length == 1){
            return "没有会议了，这个兄弟最近放假了啦~\n";
        }

        return info;
    }

    /***
     * 返回删除结果
     * @param username  用户名
     * @param password  密码
     * @param meetingId 会议id
     * @return  删除情况，
     */
    @Override
    public String deleteMeeting(String username, String password, String meetingId) throws RemoteException {

        User user_ = ServerUtils.pwdIsTrue((ArrayList<User>) users,username, password);

        if(user_ == null){
            return "没有该用户或者密码错了啦\n";
        }


        Meeting meeting = getMeetingById(meetingId);
        if(meeting == null){
            return "该会议不知道去哪流浪了(会议不存在)~\n";
        }

        if(meeting.getCreator().equals(username)){

        removeAllNumber(meeting);
        meetings.remove(meeting);

        return "SUCCESS";
        }else {
            return "这个人不是会议的创建人呀?";
        }
    }

//    @Override  /**
//     * 清除一个用户的所有会议
//     * @param username  用户名
//     * @param password  密码
//     * @return  返回清除结果
//     */
//    public String clearMeeting(String username, String password)  throws RemoteException{
//
//        User user = ServerUtils.pwdIsTrue((ArrayList<User>) users,username, password);
//
//        if(user == null){
//            return "没有该用户或者密码错了啦\n";
//        }
//
//        //需要把这个会议直接删掉
//        ArrayList<Meeting> meetingToDelete = new ArrayList<>();
//
//        //仅仅需要删除这个用户的
//        ArrayList<Meeting> userToDelete = new ArrayList<>();
//
//        //遍历这个人的所有的会议id 然后删除会议里面这个人的信息
//        for(String mid : user.getMeetingsId()){
//            //如果是创建者，则会议直接没掉
//            if(getMeetingById(mid).getCreator().equals(username)){
//
//                meetingToDelete.add(getMeetingById(mid));
//                continue;
//            }
//
//            //如果是成员 ，则要删会议里的这个人
//
//
//            if(getMeetingById(mid).getUsers().size() > 1){
//                //如果这个成员没了，还有别的 成员，则光删这个人
//
//                userToDelete.add(getMeetingById(mid));
//            }else{
//                //这个成员没了，只有创建人，则删会议
//                meetingToDelete.add(getMeetingById(mid));
//            }
//
//        }
//
//        //要把会议删除
//        for(Meeting meeting : meetingToDelete){
//            removeAllNumber(meeting);
//        }
//
//        for(Meeting meeting : userToDelete){
//            //把人删除
//            meeting.getUsers().remove(username);
//            getUserByName(username).getMeetingsId().remove(meeting.getMeetingId());
//        }
//
//
//
//        return "SUCCESS";
//    }


/**
 * 清除由这个用户创建的所有会议
 */
    public String clearMeeting(String username, String password)  throws RemoteException{

        User user_ = ServerUtils.pwdIsTrue((ArrayList<User>) users,username, password);

        if(user_ == null){
            return "没有该用户或者密码错了啦\n";
        }

        ArrayList<String> toDelete  = new ArrayList<>();

        for(Meeting meeting : meetings){
            if(meeting.getCreator().equals(username)){
                    toDelete.add(meeting.getMeetingId());
            }
        }

        for(String mID : toDelete) {
            deleteMeeting(username, password,mID);
        }
        return "SUCCESS";
    }

    /**
     * 用名字得到 用户的对象
     * @param username
     * @return
     * @throws RemoteException
     */
    @Override
public User getUserByName(String username) throws RemoteException{
        for(User user : users){
            if(username.equals(user.getUsername())){
                return user;
            }
        }
        return null;
    }

    /**
     * 通过id得到会议的丢对象
     * @param meetingId
     * @return
     * @throws RemoteException
     */
    @Override
public Meeting getMeetingById(String meetingId) throws RemoteException{
        for(Meeting meeting : meetings){
            if(meeting.getMeetingId().equals(meetingId)){
                return meeting;
            }
        }
        return null;
    }


    /**
     * 移除会议的所有成员中拥有的该会议号，然后最后把会议删掉
     * @param meeting
     * @throws RemoteException
     */
    public void removeAllNumber(Meeting meeting) throws RemoteException {

        List<String> users = meeting.getUsers();
        //将这些用户中的该会议号都 删掉！
        //先删创建者的对象中的该会议号
        getUserByName(meeting.getCreator()).getMeetingsId().remove(meeting.getMeetingId());

        //再删成员的会议号
        for(String user : users){
            getUserByName(user).getMeetingsId().remove(meeting.getMeetingId());
        }

        meetings.remove(meeting);
    }
}
