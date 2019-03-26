package com.hassamum.uchat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JFrame;

public class Client extends JFrame {
	private static final long serialVersionUID = 1L;

	private String name, address;
	private int port;
	private DatagramSocket socket;
	private InetAddress ip;
	private Thread send;
	private int ID = -1;

	// constructor
	public Client(String name, String address, int port) {
		this.name = name;
		this.address = address;
		this.port = port;
	}

	// getter methods
	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	// method to check connection
	public boolean openConnection(String address) {
		try {
			// since socket has no arguments, the method will automatically look for an open
			// port
			socket = new DatagramSocket();
			ip = InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		} catch (SocketException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// grabs incoming packets and converts them into strings
	// returns a string
	public String receive() {
		byte[] data = new byte[1024];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		try {
			socket.receive(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String message = new String(packet.getData());
		return message;
	}

	// if the arguments are in byte form it will send the packet
	public void send(final byte[] data) {
		send = new Thread("Send") {
			public void run() {
				DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		send.start();
	}

	public void close() {
		new Thread() {
			public void run() {
				synchronized (socket) {
					socket.close();
				}
			}
		}.start();
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public int getID() {
		return ID;
	}

}