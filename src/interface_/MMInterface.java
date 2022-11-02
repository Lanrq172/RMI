package interface_;

import bean.Meeting;
import bean.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDateTime;


/**
 * 会议管理接口
 */
public interface MMInterface extends Remote{

    /**
     *  用户注册 成功返回true,失败返回false
     */
    public String registerUser(String username,String password) throws RemoteException;

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

    public String createMeeting(String username1, String password, String meetingTitle, String[] otherUsernames,
                                LocalDateTime startTime, LocalDateTime endTime) throws RemoteException;

    /**
     * c查询一个人的会议，返回信息
     * @param username  用户名
     * @param password  密码
     * @param startTime 查询开始时间
     * @param endTime   查询结束时间
     * @return  返回会议信息
     */

    public String queryMeeting(String username,String password,LocalDateTime startTime,LocalDateTime endTime) throws RemoteException;

    /***
     * 返回删除结果
     * @param username  用户名
     * @param password  密码
     * @param meetingId 会议id
     * @return  删除情况，
     */
    public String deleteMeeting(String username,String password,String meetingId) throws RemoteException;

    /**
     * 清除一个用户的所有会议
     * @param username  用户名
     * @param password  密码
     * @return  返回清除结果
     */
    public String clearMeeting(String username,String password) throws RemoteException;

    User getUserByName(String username) throws RemoteException;

    Meeting getMeetingById(String meetingId) throws RemoteException;
}
