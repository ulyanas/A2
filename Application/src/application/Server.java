package application;

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

/**
 *
 * @author zmolo_000
 */
public class Server {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        try {
            RemoteImplementation remote = new RemoteImplementation();
            LocateRegistry.createRegistry(1234);
            Naming.bind("//localhost:1234/Inventory", remote);
            System.out.println("Remote Class Bind Success");
        } catch (RemoteException e) {
            System.out.println("Errors on creating remote object");
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            System.out.println("Already Bound");
            e.printStackTrace();
        } catch (MalformedURLException e) {
            System.out.println("URL Error");
            e.printStackTrace();
        }
    }
}
