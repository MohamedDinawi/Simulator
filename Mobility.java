package Sim;

import java.util.ArrayList;

public class Mobility {
    public static void main (String [] args)
    {
        // Links
        Link a = new Link();
        Link b = new Link();
        Link r = new Link();
        Link c = new Link();

        // Hosts
        Node A = new Node(1, 1);
        Node B = new Node(1, 2);
        Node C = new Node(2, 3);

        //Connect hosts to links
        A.setPeer(a);
        B.setPeer(b);
        C.setPeer(c);

        // Routers
        Router R1 = new Router(1, 4);
        Router R2 = new Router(2, 4);

        BufferPackets homeagent = new BufferPackets(R1,R1.getSentPackets(),1, 1);


        // Wire up routers
        R1.connectInterface(0, a, A);
        R1.connectInterface(1, b, B);
        R1.connectInterface(2, r, R2);

        R2.connectInterface(0, c, C);
        R2.connectInterface(0, r, R1);


        // migrate B to network 2
        B.send(R2, new RegistrationRequest(R1), 0);

        // A and C send two packets to B;
        // B send two packets to A
        // The first packets will be sent *before* B migrates
        // The seconds packets will be sent *after* B migrates
        A.StartSending(B.getAddr().networkId(), B.getAddr().nodeId(), 3, 10, 0, 10);

        ArrayList list = R1.getSentPackets();
        list.add(2);
        list.add(1);
        list.add(3);

        System.out.println(list);
        for (int i = 0; i < list.size(); i++) {

            int seq = (int) list.get(i);
            homeagent.StartSending(B.getAddr().networkId(), B.getAddr().nodeId(),1,seq, 10, 100);
            System.out.println(list);

        }

//        C.StartSending(B.getAddr().networkId(), B.getAddr().nodeId(), 2, 40, 2, 10);
//        B.StartSending(A.getAddr().networkId(), A.getAddr().nodeId(), 2, 40, 4, 20);





        // Start the simulation engine and of we go!
        Thread t=new Thread(SimEngine.instance());

        t.start();
        try
        {
            t.join();
        }
        catch (Exception e)
        {
            System.out.println("The motor seems to have a problem, time for service?");
        }
//        R1.printAllInterfaces(R1.get_routingTable());
//        R2.printAllInterfaces(R2.get_routingTable());
    }
}

