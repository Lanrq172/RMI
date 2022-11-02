package client;

import interface_.MMInterface;

import javax.print.attribute.standard.MediaSize;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @author lan
 * RMI服务器的类，
 */
public class RmiClient {

    private static Scanner IN;
    private static  MMInterface meetingManger;

    public RmiClient(){
        IN = new Scanner(System.in);
    }


    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException {


        RmiClient rmiClient = new RmiClient();
        if(args.length < 4){
           System.out.println("传入参数失败，启动客户的失败");
            return;
       }


        meetingManger = (MMInterface) Naming.lookup("rmi://" + args[0] + ":" + args[1] + "/meetingManager");


        rmiClient.run(Arrays.copyOfRange(args,2,args.length));


        String cmd;
        String[]  args_;

        while (true){

            showMenu();


            cmd = IN.nextLine();

            //如果用户输入纯空白处理
            if(""==cmd.trim()){
                continue;
            }
            //输入quit则返回
            if("quit".equals(cmd)){
                break;
            }

            args_ = cmd.split(" ");

            if(args_.length < 2){
                System.out.println("请按格式输入命令!");
                continue;
            }


            rmiClient.run(args_);
        }
    }

    /**
     * 显示菜单
     */
    private static void showMenu(){
        String menu = "请按照以下格式输入命令\n "+
                "注意有关时间的格式都是yyyy-mm-dd-hh-mm以横杠链接哦\n" +
                "用户注册:\n" +
                "register [username] [password]\n" +
                "添加会议\n" +
                "add [username] [password] [otherusername] [start] [end] [title]\n" +
                "查询会议\n" +
                "query [username] [password] [start] [end]\n" +
                "删除会议\n" +
                "delete [username] [password] [meetingid]\n" +
                "清除会议\n" +
                "clear [username] [password]\n" +
                "退出系统\n" +
                "quit \n";

        System.out.println(menu);

    }

    /**
     * 传入参数，然后运行不同方法
     * @param args
     * @throws MalformedURLException
     * @throws NotBoundException
     * @throws RemoteException
     */
    public void  run(String[] args) throws MalformedURLException, NotBoundException, RemoteException {

        if("register".equals(args[0])){
            userRegister(args);
            return;
        }

        if("add".equals(args[0])){
           addMeeting(args);
           return;
        }

        if("query".equals(args[0])){
           queryMeeting(args);
           return;
        }

        if("delete".equals((args[0]))){
          deleteMeeting(args);
          return;
        }

        if("clear".equals((args[0]))){
           clearMeeting(args);
           return;
        }

        System.out.println("没有该命令!");
    }

    /**
     * 用户注册
     * @param args
     * @throws MalformedURLException
     * @throws NotBoundException
     * @throws RemoteException
     */
    public void userRegister(String[] args) throws MalformedURLException, NotBoundException, RemoteException {

        if(args.length!=3){
            System.out.println("参数有问题，请检查后重新输入");
            return;
        }



        System.out.println(meetingManger.registerUser(args[1], args[2]));

    }

    /**
     * 添加会议
     * @param args
     * @throws MalformedURLException
     * @throws NotBoundException
     * @throws RemoteException
     */
    public void addMeeting(String[] args) throws MalformedURLException, NotBoundException, RemoteException {
        if(args.length < 7){
            System.out.println("参数有问题，请检查后重新输入");
            return;
        }


        String[] otherUser = Arrays.copyOfRange(args,3,args.length-3);

        String startStr =  args[args.length-3];
        String endStr =  args[args.length-2];
        String title =  args[args.length-1];

        //LocalDateTime的日期格式: yyyy-mm-dd-hh-mm 每个单位用 '-' 分隔
        if(startStr.split("-").length != 5){
            System.out.println("日期输入格式有问题，检查后重新输入");
            return;
        }


        LocalDateTime start = LocalDateTime.of(Integer.valueOf(startStr.split("-")[0]),
                Integer.valueOf(startStr.split("-")[1]),Integer.valueOf(startStr.split("-")[2]),
                Integer.valueOf(startStr.split("-")[3]),Integer.valueOf(startStr.split("-")[4]));

        LocalDateTime end = LocalDateTime.of(Integer.valueOf(endStr.split("-")[0]),
                Integer.valueOf(endStr.split("-")[1]),Integer.valueOf(endStr.split("-")[2]),
                Integer.valueOf(endStr.split("-")[3]),Integer.valueOf(endStr.split("-")[4]));

        System.out.println(meetingManger.createMeeting(args[1], args[2], title, otherUser, start, end));

    }

    /***
     * 查询用户会议列表
     * @param args
     * @throws MalformedURLException
     * @throws NotBoundException
     * @throws RemoteException
     */
    public void queryMeeting(String[] args) throws MalformedURLException, NotBoundException, RemoteException {

        if(args.length != 5){
            System.out.println("参数有问题，请检查后重新输入");
            return;
        }



        String username = args[1];
        String pwd = args[2];
        String startStr = args[3];
        String endStr = args[4];

        if(startStr.split("-").length != 5){
            System.out.println("日期输入格式有问题，检查后重新输入");
            return;
        }

        LocalDateTime start = LocalDateTime.of(Integer.valueOf(startStr.split("-")[0]),
                Integer.valueOf(startStr.split("-")[1]),Integer.valueOf(startStr.split("-")[2]),
                Integer.valueOf(startStr.split("-")[3]),Integer.valueOf(startStr.split("-")[4]));

        LocalDateTime end = LocalDateTime.of(Integer.valueOf(endStr.split("-")[0]),
                Integer.valueOf(endStr.split("-")[1]),Integer.valueOf(endStr.split("-")[2]),
                Integer.valueOf(endStr.split("-")[3]),Integer.valueOf(endStr.split("-")[4]));

        String meetingList = meetingManger.queryMeeting(username,pwd,start,end);


        //每一行处理好之后直接按printf打印

        if(meetingList.startsWith("SUCCESS")){

        //多个会议，每个会议分成一行
        String[] listLine= meetingList.split("\n");

        System.out.printf("%-5s %-30s %-20s %-10s %-40s\n","会议编号","开始时间","结束时间","会议名称","参会人员");


        for(String l : listLine){
            if("SUCCESS".equals(l)){
                continue;
            }
            //每一行，先得到开始时间，结束时间
            String[] element= l.split("___");
            //存储了名字
            String[] names= element[3].split("\\.\\.");
            String nameList = "";

            for(String s : names){
                nameList += s + " ";
            }
            //开始时间
            System.out.printf("%-5s %-30s %-25s %-10s %-40s\n",element[4],element[0],element[1],element[2],nameList);
        }

            System.out.println("\n\n");
        }else {
            System.out.println(meetingList);
        }
    }

    /**
     * 将某个人创建的会议删掉
     * @param args
     * @throws MalformedURLException
     * @throws NotBoundException
     * @throws RemoteException
     */
    public void deleteMeeting(String[] args) throws MalformedURLException, NotBoundException, RemoteException {

        if(args.length != 4){
            System.out.println("参数有问题，请检查后重新输入");
        }


        String username = args[1];
        String pwd = args[2];
        String meetingId = args[3];

        System.out.println(meetingManger.deleteMeeting(username,pwd,meetingId));

    }

    /**
     * 清空一个人的全部会议
     * @param args
     * @throws MalformedURLException
     * @throws NotBoundException
     * @throws RemoteException
     */
    public void clearMeeting(String[] args) throws MalformedURLException, NotBoundException, RemoteException {

        if(args.length != 3){
            System.out.println("参数有问题，请检查后重新输入");
        }

        String username = args[1];
        String pwd = args[2];

        System.out.println(meetingManger.clearMeeting(username,pwd));

    }


}
