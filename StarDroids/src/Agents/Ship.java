package Agents;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import es.upv.dsic.gti_ia.core.ACLMessage;
import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.SingleAgent;
import helpers.Pair;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Alberto Meana, Andrés Ortiz, Alba Ríos
 * @description Clase que implementa las acciones comunes a todos los agentes
 */
public class Ship extends SingleAgent {

    private ACLMessage out = new ACLMessage();
    private ACLMessage in = new ACLMessage();

    private JsonObject key, answer, msg;
    private Role role; //role of agent

    private AgentID nextAgent;
    private Token token;
    private boolean firstRound; // Primera iteracion
    private int[] roles; // 0: pajaro, 1: halcon, 2: mosca

    /**
     * @author Alberto Meana, Andrés Ortiz, Alba Ríos
     * @description Constructor de la clase agente (ship)
     */
    public Ship(AgentID id, AgentID nextId) throws Exception {
        super(id);
        this.out.setSender(this.getAid());
        this.key = new JsonObject();
        this.answer = new JsonObject();
        this.msg = new JsonObject();
        this.in = null;
        this.nextAgent = nextId;
        this.firstRound = true;      
        this.roles = new int[4];
    }

    /**
     * @author Alberto Meana
     * @description realiza la subscripción a un mapa
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

    /**
     * @author Alberto Meana
     * @description recepción de la clave de mapa
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

    /**
     * @author Alberto Meana,Andrés Ortiz
     * @desription Registro de un agente al sistema
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

    /**
     * @author Alberto Meana,Andrés Ortiz
     * @description Envía un acknowledge de recepción de clave
     */
    private void sendACK(AgentID id) {
        System.out.println("I'm " + this.getName() + " sending ACK to " + id.getLocalName());
        this.out.setReceiver(id);
        this.out.setContent("ACK");
        this.out.setPerformative(ACLMessage.INFORM);
        this.send(this.out);
    }

    /**
     * @author Alberto Meana
     * @description Recibe el acknowledge de recepción de clave
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
     * @author Alberto Meana
     * @description clase que abstrae el envio de la key entre agentes
     *
     * @param name Nombre de a quien se envia la key
     * 
     */
    private void sendKey(String name) {
        System.out.println(this.getName() + " Send Key");
        this.sendKey(new AgentID(name));

    }
    
    /**
     * @author Alberto Meana
     */
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
    
    public void receiveMessage() throws InterruptedException {
        this.in = null;
        try {
            this.in = this.receiveACLMessage();
        } catch (InterruptedException ex) {
            Logger.getLogger(Ship.class.getName()).log(Level.SEVERE, null, ex);
        }

        if(in.getPerformativeInt() == ACLMessage.INFORM)
        {
            
            if(in.getContent().length() <= 15)
            {
                System.out.println("Performativa " + in.getPerformative() + " | Contenido " + in.getContent());
                
            }else
            {
                System.out.println("Performativa " + in.getPerformative() + " | Contenido " + in.getContent());
                JsonObject message = this.role.fillSensors(in, role); //Adds message to token
                this.token.setToken(this.getName(), message); //stores message in token
            }
            
        }else if (in.getPerformativeInt() == ACLMessage.NOT_UNDERSTOOD) {
            
            System.out.println("Performativa " + in.getPerformative() + " | Contenido " + in.getContent());
            
        }else if (in.getPerformativeInt() == ACLMessage.FAILURE) {
            
            System.out.println("Performativa " + in.getPerformative() + " | Contenido " + in.getContent());
            
        }else if (in.getPerformativeInt() == ACLMessage.REFUSE) {
            
            System.out.println("Performativa " + in.getPerformative() + " | Contenido " + in.getContent());
        }

    }

    /**
     * @author Alberto Meana,Andrés Ortiz
     * @description Ejecución principal del agente
     */
    @Override
    public void execute() {
        if (this.getName().equals(AgentsNames.leaderShip)) { // Si es el controlador
            this.subscribe();
            this.token = new Token(); // Creamos el token
        } 
        else {
            this.receiveKey();
        }

        if (this.nextAgent.getLocalName().equals(AgentsNames.leaderShip)) {
            this.sendACK(this.nextAgent); // El ultimo ha recibido la key
        } 
        else {
            this.sendKey(this.nextAgent);
        }

        this.register(); // Request CHECKIN y receive INFORM
        
        if (this.getName().equals(AgentsNames.leaderShip)) {
            receiveACK(); // Esperar ACK confirmacion ultima key
        }
        
        int count = 0;
        while (!this.role.inGoal()) { // Mientras no esté en la meta
                if (!(this.getName().equals(AgentsNames.leaderShip)) || count != 0) { // Si no eres lider, esperar token
                    try {
                        waitToken();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Ship.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                parseToken(); // Pasar el token a la ED
                
                if (firstRound) { // Actualizar info en la primera iteracion
                    this.sendMessage(ActionsEnum.information); // Enviar QUERY REF
                    
                    try {
                        this.receiveMessage(); // Recibir INFORM y rellenar ED
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Ship.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    this.firstRound = false;
                    count++;
                }
                
                if (!this.role.getFound()) { // Si no se ha encontrado la meta
                    
                    switch(this.getName()) {
                        case AgentsNames.leaderShip:
                            if (roles[0] == 0) // Soy pajaro
                                //Me ejecuto
                            break;
                        case AgentsNames.ship2:
                            if (roles[1] == 0) // Soy pajaro
                                //Me ejecuto
                            break;
                        case AgentsNames.ship3:
                            if (roles[2] == 0) // Soy pajaro
                                //Me ejecuto
                            break;
                        case AgentsNames.ship4:
                            if (roles[3] == 0) // Soy pajaro
                                //Me ejecuto
                            break;
                    }
                    //SI me toca a mi
                    this.role.firstLogic(); // Ejecutar búsqueda
                    this.sendMessage(role.getAction()); // Enviar accion
                    try {
                        this.receiveMessage();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Ship.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //Si no nada
                }
                else {
                    //Heuristica 2
                    //TODO:Logic Here
                    /*Algoritmo alg= new Algoritmo(new Pair(50,50), this.role.getPosition(), this.role.getMap(), this.role.getShipsPosition());
                    ActionsEnum n = actionEnum(alg.heuristic());
                    //TODO: Action Here
                    
                    System.out.println("MUESTRAAAAAAAA " + n);
                    
                    this.sendMessage(n);
                
                    try {

                     this.receiveMessage();
                     } catch (InterruptedException ex) {
                     Logger.getLogger(Ship.class.getName()).log(Level.SEVERE, null, ex);
                     }*/
                }
                
                sendToken(); // Enviar token

        }
        // Controlar cuando se cancela (no bateria o todos en meta)
        this.cancel();         //Hacer cuando esten todos, ahora se cancela 
    }
    
    /**
     * @author Alba Rios, Vicente Martinez
     */
    protected void chooseFinder() {
        
    }
    
    /**
     * @author Andrés Ortiz
     * @description envío de token
     */
    protected void sendToken() {
        ACLMessage out = new ACLMessage();
        out.setReceiver(nextAgent);
        out.setContent(this.token.toJson().toString()); //this should be token
        out.setPerformative(ACLMessage.INFORM);
        System.out.println(this.getName() + " send token to " + nextAgent.name);
        this.send(out);
    }

    /**
     * @author Andrés Ortiz
     * @description Espera y recepción del token
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
     * @author Rafael Ruiz
     */
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

    private ActionsEnum actionEnum(String act)
    {
        if (act == "moveN")
            return ActionsEnum.moveN;
        else if (act == "moveS" )
            return ActionsEnum.moveS;
        else if (act == "moveE")
            return ActionsEnum.moveE;
        else if (act == "moveW") 
            return ActionsEnum.moveW;
        else if (act == "moveNE")
            return ActionsEnum.moveNE;
        else if (act == "moveNW") 
            return ActionsEnum.moveNW;
        else if (act == "moveSW") 
            return ActionsEnum.moveSW;
        else if (act == "refuel") 
            return ActionsEnum.battery;
        else 
            return ActionsEnum.moveSE;
        
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
