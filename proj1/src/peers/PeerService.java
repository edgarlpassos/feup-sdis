package peers;

import common.InitiatorInterface;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class PeerService {

    private String serverId;
    private String protocolVersion;
    private String serviceAccessPoint;

    private MulticastSocket mcSocket;
    private InetAddress mcAddr;
    private int mcPort;

    private MulticastSocket mdbSocket;
    private InetAddress mdbAddr;
    private int mdbPort;

    private MulticastSocket mdrSocket;
    private InetAddress mdrAddr;
    private int mdrPort;

    private InitiatorPeer initiatorPeer;

    public PeerService(String serverId,String protocolVersion, String serviceAccessPoint,InetAddress mcAddr,int mcPort,InetAddress mdbAddr,int mdbPort,
                       InetAddress mdrAddr,int mdrPort) throws IOException {

        this.serverId = serverId;
        this.protocolVersion = protocolVersion;
        this.serviceAccessPoint = serviceAccessPoint;

        this.mcAddr = mcAddr;
        this.mcPort = mcPort;

        this.mdbAddr = mdbAddr;
        this.mdbPort = mdbPort;

        this.mdrAddr = mdrAddr;
        this.mdrPort = mdrPort;

        System.out.println("Multicast channel addr: "+ this.mcAddr+" port: "+ this.mcPort);
        System.out.println("Multicast data backup addr: "+ this.mdbAddr+" port: "+ this.mdbPort);
        System.out.println("Multicast data restore addr: "+ this.mdrAddr+" port: "+ this.mdrPort);

        mcSocket = new MulticastSocket(this.mcPort);
        mcSocket.joinGroup(this.mcAddr);

        mdbSocket = new MulticastSocket(this.mdbPort);
        mdbSocket.joinGroup(this.mdbAddr);

        mdrSocket = new MulticastSocket(this.mdrPort);
        mdrSocket.joinGroup(this.mdrAddr);

        initiatorPeer = new InitiatorPeer();

        try{
            Registry registry = LocateRegistry.getRegistry();
            registry.bind(this.serviceAccessPoint,initiatorPeer);
        }catch (Exception e){
            System.out.println("Peer error: "+ e.getMessage());
            e.printStackTrace();
        }

    }
}
