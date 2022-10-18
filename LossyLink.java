package Sim;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class LossyLink extends Link {
    private  double delay;
    private  double probability;
    private  Random rnd;
    private double jitter;
    private double prevDelay;
//    public List<Integer> lostpackets = new ArrayList<>();
    public LossyLink(double delay, double jitter, double probability) {

        this.delay = delay;
        this.jitter = jitter;
        this.probability = probability;
        this.rnd = new Random();
    }

    public void recv(SimEnt src, Event ev) {

        if (ev instanceof Message) {
            double rnd1 = Math.random();
            if (rnd1 > probability) {
                double nextdelay = Math.abs(this.delay + rnd.nextDouble(jitter + 1));
//                System.out.println("The next delay is  :" + nextdelay);

                System.out.println("Link recv msg with seq: " +
                        ((Message) ev).seq() + " , passes it through");
//                System.out.println(((Message) ev).seq()+"LossylinkPacket");
//                lostpackets.add(((Message) ev).seq());
//                System.out.println(lostpackets+"LossylinkPacket");



                if (src == _connectorA) {

                    jitter = Math.abs(nextdelay - prevDelay); //add comments
//                    System.out.println("jitter from A :" + jitter);
                    prevDelay = nextdelay;

                    send(_connectorB, ev, nextdelay);

                } else {
                    jitter = Math.abs(nextdelay - prevDelay);
//                    System.out.println("jitter from B :" + jitter);
                    prevDelay = nextdelay;

                    send(_connectorA, ev, nextdelay);
                }
            }

        }
    }
}
