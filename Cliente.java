import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.xml.crypto.Data;

public class Cliente {
    public static void main(String[] args) throws SocketException, UnknownHostException, IOException {
        String mensaje = new String("Dame la hora local.");
        String servidor = new String("localhost");

        int puerto = 8080;
        int espera = 5000;

        DatagramSocket socketUDP = new DatagramSocket();
        InetAddress hostservidor = InetAddress.getByName(servidor);

        DatagramPacket peticion = new DatagramPacket(mensaje.getBytes(), mensaje.getBytes().length, hostservidor, puerto);
        socketUDP.setSoTimeout(espera);
        System.out.println("Esperamos datos en un máximo de " + espera + " milisegundos...");
        socketUDP.send(peticion);

        try {
            byte[] buffer = new byte[1024];
            DatagramPacket respuesta = new DatagramPacket(buffer, buffer.length);
            socketUDP.receive(respuesta);

            String strText = new String(respuesta.getData(), 0, respuesta.getLength());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime horaServidor = LocalDateTime.parse(strText, formatter);

            LocalDateTime horaCliente = LocalDateTime.now();

            Duration diferencia = Duration.between(horaServidor, horaCliente);

            long horas = diferencia.toHours();
            long minutos = diferencia.toMinutes() % 60;
            long segundos = diferencia.getSeconds() % 60;

            System.out.println("Hora del servidor es: " + horaServidor.format(formatter));
            System.out.println("Diferencia entre la hora del servidor y la hora del cliente: " + horas + "h, " + minutos + "m, " + segundos + "s.");
        } catch (SocketException s) {
            System.out.println("Tiempo expirado para recibir respuesta: " + s.getMessage());
        }

        socketUDP.close();
    }
}
