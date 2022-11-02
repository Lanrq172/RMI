package server;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import bean.MeetingManager;
import bean.User;
import interface_.MMInterface;

/**
 * RMI服务器
 * @author  lrq
 */
public class rmiServer {

    public static void main(String[] args) throws RemoteException, MalformedURLException, AlreadyBoundException {

        if(args.length!=2){
            System.out.println("请检查输入参数的格式");
        }

        LocateRegistry.createRegistry(Integer.decode(args[1]));

        MMInterface meetingManger = new MeetingManager();

        Naming.bind("rmi://" + args[0] + ":" + args[1]  + "/meetingManager",meetingManger);

    }

}
