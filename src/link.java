import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class link {
    private PrintWriter output = null;
    private BufferedReader input = null;
    private Socket LinkSocket;
    private String inputLine;
    private String path;

    public static void main(String[] args) throws IOException {

        link link = new link();

        //Start the Ximo Studio
       /* try {
            // print a message
            System.out.println("Executing XimoStudio");

            // create a process and execute Ximo Studio
            Process process = Runtime.getRuntime().exec(new String[]{"java", "-jar", "C:\\Users\\i340902\\git\\XimoStudio\\download\\XimoStudio_v0.8.0.jar"});

            if (process.isAlive()) {
                System.out.println("Ximo is alive");
                serverCommunication(link);
                process.destroy();//kill XimoLink
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }     */
    }
     public link() throws IOException {
            this.connect();
            this.send();
            this.listen();
     }
    private void connect() throws UnknownHostException, IOException {
        //Connect to XimoStudio
        //Opening a socket
        LinkSocket = new Socket("127.0.0.1", 2525);
        System.out.println("Connected to " + LinkSocket.getInetAddress().getHostName());
        //Opening data input stream to receive response from XimoStudio
        input = new BufferedReader(new InputStreamReader(LinkSocket.getInputStream()));
        //Opening data output stream to send command
        output = new PrintWriter(LinkSocket.getOutputStream(), true);
        System.out.println("Connection is open");
    }
    private void listen() throws IOException {
        //listen to server
        while ((inputLine = input.readLine()) != null) {
            //read input from server
            System.out.println("Server: " + inputLine);
            if (inputLine.equals("OK")) { //acknowledgement from XimoStudio for pasting SQL from clipboard
                System.out.println("Ack from XimoStudio received");
            } else if (inputLine.equals("ready")) { // XimoStudio has copied results to clipboard
                System.out.println("Results were copied to clipboard\nExiting XimoLink");
                cleanUp();
            } else {
                System.out.println("Unknown command ");
            }
        }
    }
    private void cleanUp() throws IOException {
        //do a clean up
        output.close();// close the output stream
        input.close();// close the input stream
        LinkSocket.close();// close the socket
        System.exit(0);
    }
    private void send() {
        //send command to XimoStudio
        output.println("pasteClipboard");
        System.out.println("Command paste from clipboard sent");
    }
}
