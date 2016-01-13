package Agents;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import es.upv.dsic.gti_ia.core.ACLMessage;
import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.SingleAgent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que implementa las acciones comunes a todos los agentes.
 *
 *
 * @author Alberto Meana, Andrés Ortiz, Alba Ríos
 */
public class Ship extends SingleAgent {

    private ACLMessage out = new ACLMessage();
    private ACLMessage in = new ACLMessage();

    private JsonObject key, answer, msg;
    private Role role; //role of agent

    private AgentID nextAgent;
    private Token token;

    /*
     * @author Alberto Meana,Andrés Ortiz,Alba Ríos
     */
    public Ship(AgentID id, AgentID nextId) throws Exception {
        super(id);
        this.out.setSender(this.getAid());
        this.key = new JsonObject();
        this.answer = new JsonObject();
        this.msg = new JsonObject();
        this.in = null;
        this.nextAgent = nextId;

    }

    /*
     * @author Alberto Meana
     */
    private void subscribe() {
        //Composición de Json de subscripcion.
        this.msg.add("world", "map1");
        // Creación del ACL
        this.out.setPerformative(ACLMessage.SUBSCRIBE);
        this.out.setReceiver(new AgentID("Furud"));
        this.out.setContent(msg.toString());
        this.send(out);
        this.receiveKey();
    }

    /*
     * @author Alberto Meana
     */
    private void receiveKey() {
        this.in = null;
        try {
            this.in = this.receiveACLMessage();
        } catch (InterruptedException ex) {
            Logger.getLogger(Ship.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (in.getPerformativeInt() == ACLMessage.INFORM) {
            this.key = Json.parse(this.in.getContent()).asObject();
        } else {
            System.out.println("ERROR: " + in.getPerformative());
        }
    }

    /*
     * @author Alberto Meana,Andrés Ortiz
     */
    private void register() {
        this.msg = new JsonObject();
        this.msg.add("command", "checkin");
        this.msg.add("key", key.get("result").asString());
        this.out.setPerformative(ACLMessage.REQUEST);
        this.out.setReceiver(new AgentID("Furud"));
        this.out.setContent(this.msg.toString());
        this.send(this.out);
        this.in = null;
        try {
            this.in = this.receiveACLMessage();
        } catch (InterruptedException ex) {
            Logger.getLogger(Ship.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (in.getPerformativeInt() == ACLMessage.INFORM) {
            this.answer = Json.parse(this.in.getContent()).asObject();
            switch (this.answer.get("rol").asInt()) {
                case (0):
                    System.out.println(this.getName() + " dice: Soy un Xwing (mosca)!");
                    this.role = new XWing();
                    break;
                case (1):
                    System.out.println(this.getName() + " dice: Soy un Ywing pajaro!");
                    this.role = new YWing();
                    break;
                case (2):
                    System.out.println(this.getName() + " dice: Soy un Halcon Milenariooo!");
                    this.role = new MillenniumFalcon();
                    break;
            }
        } else {
            System.out.println("ERROR: " + in.getPerformative());
        }
    }

    /**
     * @author Alberto Meana
     */
    private void cancel() {
        System.out.println("Cancel sent");
        this.msg = new JsonObject();
        this.msg.add("key", this.key.get("result").asString());
        this.out.setContent(this.msg.toString());
        this.out.setReceiver(new AgentID("Furud"));
        this.out.setPerformative(ACLMessage.CANCEL);
        this.send(this.out);
        // Test del jdialog de ganar.
        // WinnerDialog winrar = new WinnerDialog( new Frame(), true );
    }

    /*
     * @author Alberto Meana,Andrés Ortiz
     */
    private void sendACK(AgentID id) {
        System.out.println("I'm " + this.getName() + " sending ACK to " + id.getLocalName());
        this.out.setReceiver(id);
        this.out.setContent("ACK");
        this.out.setPerformative(ACLMessage.INFORM);
        this.send(this.out);
    }

    /*
     * @author Alberto Meana
     */
    private void receiveACK() {
        this.in = null;
        try {
            this.in = this.receiveACLMessage();
            System.out.println("ACK received");
        } catch (InterruptedException ex) {
            Logger.getLogger(Ship.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * clase que abstrae el envio de la key entre agentes
     *
     *
     * @param name Nombre de a quien se envia la key
     * @author Alberto Meana
     */
    private void sendKey(String name) {
        System.out.println(this.getName() + " Send Key");
        this.sendKey(new AgentID(name));

    }

    private void sendKey(AgentID id) {
        this.out.setReceiver(id);
        this.out.setContent(this.key.toString());
        this.out.setPerformative(ACLMessage.INFORM);
        this.send(this.out);
    }

    /**
     *
     * @autor Rafael Ruiz
     *
     * @throws InterruptedException
     */
    //COMMENTED UNTIL WORKING
    public void receiveMessage() throws InterruptedException {
        this.in = null;
        try {
            this.in = this.receiveACLMessage();
        } catch (InterruptedException ex) {
            Logger.getLogger(Ship.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (in.getPerformativeInt() == ACLMessage.INFORM && !in.getContent().equals("ACK")) // DANGER DANGER ACK RARO!!
        {
            System.out.println("PERFORMATIVA " + in.getPerformative());
            JsonObject message = this.role.fillSensors(in, role); //Adds message to token
            this.token.setToken(this.getName(), message); //stores message in token

        } else if (in.getPerformativeInt() == ACLMessage.NOT_UNDERSTOOD) {
            System.out.println("PERFORMATIVA " + in.getPerformative());
        } else if (in.getPerformativeInt() == ACLMessage.FAILURE) {
            System.out.println("PERFORMATIVA " + in.getPerformative());
        } else if (in.getPerformativeInt() == ACLMessage.REFUSE) {
            System.out.println("PERFORMATIVA " + in.getPerformative());
        }

    }

    /*
     * @author Alberto Meana,Andrés Ortiz
     */
    @Override
    public void execute() {
        if (this.getName().equals(AgentsNames.leaderShip)) {
            this.subscribe();
            this.token = new Token(); //creamos el token
        } else {
            this.receiveKey();
        }

        if (this.nextAgent.getLocalName().equals(AgentsNames.leaderShip)) {
            //this.sendKey(this.nextAgent); //same as before, but with store nextAgent
            this.sendACK(this.nextAgent);
        } else {
            this.sendKey(this.nextAgent);
            //this.sendACK(this.nextAgent);
        }

        this.register();
        if (this.getName().equals(AgentsNames.leaderShip)) {
            receiveACK();
        }
        int count = 0;
        while (!this.role.inGoal()) {
            System.out.println("Main Loop");
            try {
                if (!(this.getName().equals(AgentsNames.leaderShip)) || count != 0) {
                    waitToken();
                }
                //this. token should have all data (one token for each ship)
                
                parseToken();
                
                //TODO:Logic Here
                //TODO: Action Here

                //Simple example
                 this.sendMessage(ActionsEnum.information);
                 try {

                 this.receiveMessage();
                 } catch (InterruptedException ex) {
                 Logger.getLogger(Ship.class.getName()).log(Level.SEVERE, null, ex);
                 }

                //this.token.setMeta(/*json object here*/); //if metadata, set here in token
                sendToken(); //sends token to next agent

                count++;
            } catch (InterruptedException ex) {
                Logger.getLogger(Ship.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        this.cancel();

        
         // Testing lo sensores y envio de mensajes!!!
         /*this.sendMessage(ActionsEnum.information);
        
         try {
            
         this.receiveMessage();
         }
         catch(InterruptedException ex) {
         Logger.getLogger(Ship.class.getName()).log(Level.SEVERE, null, ex);
         }
        
         if(this.getName().equals(AgentsNames.leaderShip)) {
            
         this.cancel();
         }*/
         
    }

    /*
     * @author Andrés Ortiz
     */
    protected void sendToken() {
        ACLMessage out = new ACLMessage();
        out.setReceiver(nextAgent);
        out.setContent(this.token.toJson().toString()); //this should be token
        out.setPerformative(ACLMessage.INFORM);
        System.out.println(this.getName() + " send token to " + nextAgent.name);
        this.send(out);
    }

    /*
     * @author Andrés Ortiz
     */
    protected void waitToken() throws InterruptedException {
        System.out.println(this.getName() + " Waiting token");
        ACLMessage in = new ACLMessage();
        in = this.receiveACLMessage();
        if (in.getPerformativeInt() == ACLMessage.INFORM) {
            this.token = new Token(Json.parse(in.getContent()).asObject());
            System.out.println(this.getName() + " received token");
        }
    }

    /**
     * @author Andrés Ortiz
     */
    private void executeAction() {
        ActionsEnum action = role.getAction();
        //execute(action)
    }

    
    public void parseToken()
    {
        this.role.parseTokenAgent(this.token);
    }
    
    /**
     * @author Rafael Ruiz
     *
     * @param msg
     */
    public void sendMessage(ActionsEnum action) {
        if (action.equals(ActionsEnum.battery)) {
            this.msg = new JsonObject();
            this.msg.add("command", "refuel");
            this.msg.add("key", key.get("result").asString());
            this.out.setPerformative(ACLMessage.REQUEST);
            this.out.setReceiver(new AgentID("Furud"));
            this.out.setContent(this.msg.toString());
            this.send(this.out);

        } else if (action == ActionsEnum.information) {
            this.msg = new JsonObject();
            this.msg.add("key", key.get("result").asString());
            this.out.setPerformative(ACLMessage.QUERY_REF);
            this.out.setReceiver(new AgentID("Furud"));
            this.out.setContent(this.msg.toString());
            this.send(this.out);

        } else {
            this.msg = new JsonObject();

            if (action == ActionsEnum.moveN) {
                this.msg.add("command", "moveN");

            } else if (action == ActionsEnum.moveS) {
                this.msg.add("command", "moveS");

            } else if (action == ActionsEnum.moveE) {
                this.msg.add("command", "moveE");

            } else if (action == ActionsEnum.moveW) {
                this.msg.add("command", "moveW");

            } else if (action == ActionsEnum.moveNE) {
                this.msg.add("command", "moveNE");

            } else if (action == ActionsEnum.moveNW) {
                this.msg.add("command", "moveNW");

            } else if (action == ActionsEnum.moveSW) {
                this.msg.add("command", "moveSW");

            } else if (action == ActionsEnum.moveSE) {
                this.msg.add("command", "moveSE");

            }

            this.msg.add("key", key.get("result").asString());
            this.out.setPerformative(ACLMessage.REQUEST);
            this.out.setReceiver(new AgentID("Furud"));
            this.out.setContent(this.msg.toString());
            this.send(this.out);
        }
    //    this.actualiceToken(action);
    }

    /**
     *
     * @author Nikolai Giovanni González López
     */
    private void actualiceToken(ActionsEnum action) {
        JsonObject msgToken = new JsonObject();

        if (action.equals(ActionsEnum.battery)) {
            if (this.getAid().name == AgentsNames.leaderShip) {
                msgToken.add(AgentsNames.leaderShip, "refuel");
                this.token.parse(msgToken);
            } else if (this.getAid().name == AgentsNames.ship2) {
                msgToken.add(AgentsNames.ship2, "refuel");
                this.token.parse(msgToken);
            } else if (this.getAid().name == AgentsNames.ship3) {
                msgToken.add(AgentsNames.ship3, "refuel");
                this.token.parse(msgToken);
            } else if (this.getAid().name == AgentsNames.ship4) {
                msgToken.add(AgentsNames.ship4, "refuel");
                this.token.parse(msgToken);
            }

        } else if (action == ActionsEnum.information) {
            if (this.getAid().name == AgentsNames.leaderShip) {
                msgToken.add(AgentsNames.leaderShip, "key");
                this.token.parse(msgToken);
            } else if (this.getAid().name == AgentsNames.ship2) {
                msgToken.add(AgentsNames.ship2, "key");
                this.token.parse(msgToken);
            } else if (this.getAid().name == AgentsNames.ship3) {
                msgToken.add(AgentsNames.ship3, "key");
                this.token.parse(msgToken);
            } else if (this.getAid().name == AgentsNames.ship4) {
                msgToken.add(AgentsNames.ship4, "key");
                this.token.parse(msgToken);
            }

        } else {
            String accion = new String();
            if (action == ActionsEnum.moveN) {
                accion = "moveN";

            } else if (action == ActionsEnum.moveS) {
                accion = "moveS";

            } else if (action == ActionsEnum.moveE) {
                accion = "moveE";

            } else if (action == ActionsEnum.moveW) {
                accion = "moveW";

            } else if (action == ActionsEnum.moveNE) {
                accion = "moveNE";

            } else if (action == ActionsEnum.moveNW) {
                accion = "moveNW";

            } else if (action == ActionsEnum.moveSW) {
                accion = "moveSW";

            } else if (action == ActionsEnum.moveSE) {
                accion = "moveSE";

            }

            if (this.getAid().name == AgentsNames.leaderShip) {
                msgToken.add(AgentsNames.leaderShip, accion);
                this.token.parse(msgToken);
            } else if (this.getAid().name == AgentsNames.ship2) {
                msgToken.add(AgentsNames.ship2, accion);
                this.token.parse(msgToken);
            } else if (this.getAid().name == AgentsNames.ship3) {
                msgToken.add(AgentsNames.ship3, accion);
                this.token.parse(msgToken);
            } else if (this.getAid().name == AgentsNames.ship4) {
                msgToken.add(AgentsNames.ship4, accion);
                this.token.parse(msgToken);
            }
        }
    }

}
