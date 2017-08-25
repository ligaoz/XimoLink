import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class link {

    public static void main(String[] args) throws IOException {
        PrintWriter output = null;
        BufferedReader input = null;
        Socket LinkSocket;
        String inputLine;

        //Check if XimoStudio is running if its not start the program
      /*  if(CheckXimoStudio.isRunning()){
            System.out.println("Two instances of this program cannot " +
                        "be running at the same time.  Exiting now");
        }
        else{
            CheckXimoStudio.onStart();
            CheckXimoStudio.epicHeavyWorkGoesHere();
            CheckXimoStudio.onFinish();
        }
*/
        //Start to communicate with XimoStudio
        try {
            LinkSocket = new Socket("127.0.0.1", 2525); //Opening a socket
            System.out.println("Connected to " + LinkSocket.getInetAddress().getHostName());

            input = new BufferedReader(new InputStreamReader(LinkSocket.getInputStream()));//Opening data input stream to receive response from XimoStudio
            output = new PrintWriter(LinkSocket.getOutputStream(), true);//Opening data output stream to send command

            System.out.println("Started talking");
            output.println("pasteClipboard");//send command to XimoStudio
            System.out.println("Command paste from clipboard sent");

            while (( inputLine = input.readLine()) != null){
                //read input from server
                System.out.println("Server: " + inputLine);

                if (inputLine.equals("OK")) { //acknowledgement for paste from clipboard
                    System.out.println("Ack from XimoStudio received");
                }else if(inputLine.equals("ready")) { //Wait until command "ready" is received from XimoStudio
                    System.out.println("Results copied to clipboard\n Exiting XimoLink");
                    //do a clean up
                    try {
                        output.close();// close the output stream
                        input.close();// close the input stream
                        LinkSocket.close();// close the socket
                        System.exit(0);
                    }
                    catch (IOException e) {
                        System.out.println(e);
                    }
                }else
                    System.out.println("Unknown command ");
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
