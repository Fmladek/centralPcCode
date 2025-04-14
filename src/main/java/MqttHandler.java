import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MqttHandler {
    private final String topic;
    private final String clientId = "JavaPublisher";
    private MqttClient client;
    private final int qos = 1;

    public MqttHandler(String broker, String topic) {
        this.topic = topic;
        try {
            client = new MqttClient(broker, clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName("centralunit");
            connOpts.setPassword("heslo1".toCharArray());
            connOpts.setSSLHostnameVerifier((hostname, session) -> true);
            client.connect(connOpts);

        } catch (MqttException e) {
            System.out.println("The client could not connect: \n" + e.toString());
        }
    }

    public void publishMessage(String content){
        try{
        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(qos);
        client.publish(topic, message);
        System.out.println("Message sent");

        } catch (MqttException e) {
            System.out.println("error sending message: \n" + e.toString());
        }
    }
    public void disconnect(){
        try{
            this.client.disconnect();
        }catch (MqttException e){
            System.out.println("error disconnecting: \n" + e.toString());
        }
    }
    public void sendMessageTls(String message, String topic, String broker){
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "cmd.exe", "/c",
                    "cd /d \"C:\\Program files\\mosquitto\" && " +
                    "mosquitto_pub -h "+broker+" -p 8883 " +
                            "--cafile \"C:/Program files/mosquitto/passwordFiles/ca.crt\" " +
                            "-t "+topic+" -m \""+message+"\" -u centralunit -P heslo1"
            );
            pb.redirectErrorStream(true); // merge stdout and stderr

            Process process = pb.start();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            if(exitCode==0) {System.out.println("Message sent");}
        } catch (Exception e){
            System.out.println(e);
        }
    }
}
