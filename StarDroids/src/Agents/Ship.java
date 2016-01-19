package Agents;

import GUI.WinnerDialog;
import GUI.WorldDialog;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import es.upv.dsic.gti_ia.core.ACLMessage;
import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.SingleAgent;
import helpers.Pair;
import java.awt.Frame;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import mapproject.MapProject;
import mapproject.TileType;

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
    private boolean firstRound, chosen; // Primera iteracion y segunda
    private int[] roles; // 1: pajaro, 2: halcon, 3: mosca
    private int finder; // Bot que hace la búsqueda

    // Instancias de la interfaz
    public MapProject gui;
    private String worldToSolve;
    
    
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
        this.chosen = false;
        this.roles = new int[4];
        this.finder = -1;
    }
    
    /**
     * Establece la instancia de la interfaz.
     * Solo debe usarse con el lider del ring Token.
     * 
     * @param map Instancia del grid.
     * @author Alberto Meana
     */
    public void setInterface( MapProject map ){
        
        this.worldToSolve = (new WorldDialog( new JFrame(), true) ).getWordl();
        this.gui = map;
    
    }
    
    /**
     * Pintado en interfaz version StarDroids
     * 
     * @author Alberto Meana
     */
    public void paint(){
        
        // Pinto el mapa
        int size;
        if( this.worldToSolve.equals( "map8" ) || this.worldToSolve.equals( "map80" ) )
            size = 500;
        else
            size = 100;
        
        for( int i = 0; i< size; i++ ){
            for( int j = 0; j< size; j++ ){
            
                switch( this.role.getMapPosition( i, j ) ){
                    
                    case 0:
                        this.gui.grid.setTile( i, j, TileType.Dirt );
                        break;
                    case 1:
                        this.gui.grid.setTile( i, j, TileType.Rock );
                        break;
                    case 2:
                        this.gui.grid.setTile( i, j, TileType.Rock );
                        break;
                    case 3:
                        this.gui.grid.setTile( i, j, TileType.Goal );
                        break;
                    default:
                        break;
                }
            
            }
        }
        
        // Pinto mi posicion
        Pair <Integer,Integer> myPosition = this.role.getPosition();
        TileType myRole;
        
        if( this.role.getClass().equals( XWing.class ) ){
            
            myRole = TileType.Xwing;
        
        }else if( this.role.getClass().equals( YWing.class ) ){
        
            myRole = TileType.Ywing;
        
        }else{
        
            myRole = TileType.Falcon;
        
        }
        this.gui.grid.setTile( myPosition.first, myPosition.second, myRole );
        
        // Pinto la posición de los demás
        for( int i = 0; i<4; i++ ){
            Pair <Integer,Integer> shipPoistion = this.role.getShipPosition( i );
            switch (this.roles[i]) {
                case 0:
                    myRole = TileType.Xwing;
                    this.gui.grid.setTile( shipPoistion.first, shipPoistion.second, myRole );
                    break;
                case 1:
                    myRole = TileType.Ywing;
                    this.gui.grid.setTile( shipPoistion.first, shipPoistion.second, myRole );
                    break;
                case 2:
                    myRole = TileType.Falcon;
                    this.gui.grid.setTile( shipPoistion.first, shipPoistion.second, myRole );
                    break;
                default:
                    break;
            }
            
        }
        
    }
    
    /**
     * @author Alberto Meana
     * @description realiza la subscripción a un mapa
     */
    private void subscribe() {
        //Composición de Json de subscripcion.
        this.msg.add( "world", this.worldToSolve );
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
        WinnerDialog winrar = new WinnerDialog( new Frame(), true );
    }

    /**
     * @author Alberto Meana,Andrés Ortiz
     * @description Envía un acknowledge de recepción de clave
     */
    private void sendACK(AgentID id) {
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
        } catch (InterruptedException ex) {
            Logger.getLogger(Ship.class.getName()).log(Level.SEVERE, null, ex);
        }
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
                System.out.println( this.getName() + " Performativa " + in.getPerformative() + " | Contenido " + in.getContent());
                
            }else
            {
                System.out.println( this.getName() + " Performativa " + in.getPerformative() + " | Contenido " + in.getContent());
                JsonObject message = this.role.fillSensors(in); //Adds message to token
                this.token.setToken(this.getName(), message); //stores message in token
            }
            
        }else if (in.getPerformativeInt() == ACLMessage.NOT_UNDERSTOOD) {
            
            System.out.println( this.getName() + " Performativa " + in.getPerformative() + " | Contenido " + in.getContent());
            while(true){}
        }else if (in.getPerformativeInt() == ACLMessage.FAILURE) {
            
            System.out.println( this.getName() + " Performativa " + in.getPerformative() + " | Contenido " + in.getContent());
            while(true){}
        }else if (in.getPerformativeInt() == ACLMessage.REFUSE) {
            
            System.out.println( this.getName() + " Performativa " + in.getPerformative() + " | Contenido " + in.getContent());
            while(true){}
        }

    }

    /**
     * @author Alberto Meana,Andrés Ortiz, Alba Rios, Vicente Martinez
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

        do { // Mientras no esté en la meta
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
                else{
                    if (!this.chosen) {
                        this.chooseFinder();
                        this.chosen = true;
                    }
                
                    if (!this.role.getFound()) { // Si no se ha encontrado la meta                  
                        switch(this.getName()) { // Si somos the chosen one
                            case AgentsNames.leaderShip:
                                // El agente lider es el unico que puede pintar
                                // Ya que es el unico que tiene la instancia de la interfaz
                                this.paint();
                                if (this.finder == 0) {
                                    System.out.println("----------------------- FINDER: " + this.getName());
                                    this.firstLogic();
                                }
                                break;
                            case AgentsNames.ship2:
                                if (this.finder == 1) {
                                    System.out.println("----------------------- FINDER: " + this.getName());
                                    this.firstLogic();
                                }
                                break;
                            case AgentsNames.ship3:
                                if (this.finder == 2) {
                                    System.out.println("----------------------- FINDER: " + this.getName());
                                    this.firstLogic();
                                }
                                break;
                            case AgentsNames.ship4:
                                if (this.finder == 3) {
                                    System.out.println("----------------------- FINDER: " + this.getName());
                                    this.firstLogic();
                                }
                                break;
                            default:
                                System.out.println("ERROR: selección de finder.");
                                break;
                        }
                    }
                    else {
                        if( this.getName().equals(AgentsNames.leaderShip) ){
                        
                            this.paint();
                        }
                            
                        System.out.println("----------------------- META VISUALIZADA! ---------------------");
                        //Heuristica 2
                        this.secondLogic();
                    }
                }
                
                if (this.role.noFuel()){
                    System.out.println("Cancel, no fuel left");
                    this.cancel();
                } //se acabo el fuel
                
                sendToken(); // Enviar token

        } while (!this.role.inGoal());
        
        if(this.role.allInGoal()){
            System.out.println("Cancel, al in goal");
            this.cancel();
        } //Si todos en meta
        // Controlar cuando se cancela (no bateria o todos en meta)
    }
    
    /**
     * @author Alba Rios
     * @description Se aplica la lógica de búsqueda y se envía y recibe respuesta del servidor
     */
    public void firstLogic() {

        this.sendMessage(ActionsEnum.information); // Enviar QUERY REF
                    
        try {
            this.receiveMessage(); // Recibir INFORM y rellenar ED
        } catch (InterruptedException ex) {
            Logger.getLogger(Ship.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.role.firstLogic(); // Ejecutar búsqueda
        this.sendMessage(role.getAction()); // Enviar accion
        try {
            this.receiveMessage();
        } catch (InterruptedException ex) {
            Logger.getLogger(Ship.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * @author Vicente Martinez
     * @description Se aplica la lógica de ir al objetivo y se envía y recibe respuesta del servidor
     */
    public void secondLogic() {

        this.sendMessage(ActionsEnum.information); // Enviar QUERY REF
                    
        try {
            this.receiveMessage(); // Recibir INFORM y rellenar ED
        } catch (InterruptedException ex) {
            Logger.getLogger(Ship.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.role.secondLogic();
        System.out.println("Action ROLe: " + role.getAction());
        this.sendMessage(role.getAction());
        try {
            this.receiveMessage();
        } catch (InterruptedException ex) {
            Logger.getLogger(Ship.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * @author Alba Rios
     * @description Elegir que bot hace la búsqueda
     */
    protected void chooseFinder() {
        int numBird = 0, numFalcon = 0, numFly = 0;
        
        for (int i = 0; i < this.roles.length; i++) {
            switch (roles[i]) {
                case 1:
                    numBird++;
                    break;
                case 2:
                    numFalcon++;
                    break;
                case 0:
                    numFly++;
                    break;
            }
        }
        
        if (numBird != 0) {
            for (int i = 0; i < this.roles.length; i++) {
                if (roles[i] == 1) this.finder = i;
            }
        }
        else if (numFalcon != 0) {
            for (int i = 0; i < this.roles.length; i++) {
                if (roles[i] == 2) this.finder = i;
            }
        } 
        else {
            for (int i = 0; i < this.roles.length; i++) {
                if (roles[i] == 0) this.finder = i;
            }
        }
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
        this.send(out);
    }

    /**
     * @author Andrés Ortiz
     * @description Espera y recepción del token
     */
    protected void waitToken() throws InterruptedException {
        ACLMessage in = new ACLMessage();
        in = this.receiveACLMessage();
        if (in.getPerformativeInt() == ACLMessage.INFORM) {
            this.token = new Token(Json.parse(in.getContent()).asObject());
        }
    }

    /**
     * @author Rafael Ruiz
     */
    public void parseToken()
    {
        this.role.parseTokenAgent(this.token);
        this.roles = this.role.getRoles();
    }
    
    /**
     * @author Rafael Ruiz
     *
     * @param action
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


}
