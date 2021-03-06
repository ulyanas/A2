

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import javax.swing.JOptionPane;

/**
 *
 * @author zmolo_000
 */
public class Server {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        try {
            if(args.length != 2){
                JOptionPane.showMessageDialog(null, "Please fill the EEF database server IP address and Leaftech database server IP address");
                return;
            }
            else{
                System.setProperty("java.security.policy","policy.txt");
                System.setSecurityManager(new java.rmi.RMISecurityManager());
                RemoteImplementation remote = new RemoteImplementation();
                DBHelper.EEFServerIPAddress = args[0];
                DBHelper.LeaftechServerIPAdress = args[1];
                LocateRegistry.createRegistry(1234);
                Naming.bind("//localhost:1234/Remote", remote);
                JOptionPane.showMessageDialog(null, "Server Start");
            }
        } catch (RemoteException e) {
            System.out.println("Errors on creating remote object");
            JOptionPane.showMessageDialog(null, e.toString());
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            System.out.println("Already Bound");
            JOptionPane.showMessageDialog(null, e.toString());
            e.printStackTrace();
        } catch (MalformedURLException e) {
            System.out.println("URL Error");
            JOptionPane.showMessageDialog(null, e.toString());
            e.printStackTrace();
        }
    }
}
