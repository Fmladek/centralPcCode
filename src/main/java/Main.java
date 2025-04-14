import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Main {

    public static void main(String[] args) {

        //Multiple threads so the application can run multiple tasks at once
        new Thread(() -> {
        FileReceiver fr = new FileReceiver();
        fr.startReceiving();
         }).start();

        new Thread(() -> {
        try {

            MqttHandler mqttHandler = new MqttHandler("tcp://localhost:1883", "camera/status");
            /*
            mqttHandler.publishMessage("CAMERA_ON");

            Thread.sleep(120000); // for testing purposes, turn of after X sec

            mqttHandler.publishMessage("CAMERA_OFF");
            mqttHandler.disconnect(); */
            mqttHandler.sendMessageTls("CAMERA_ON", "camera/status", "192.168.1.46");
            Thread.sleep(25000);
            mqttHandler.sendMessageTls("CAMERA_OFF", "camera/status", "192.168.1.46");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        }).start();
    }
}
