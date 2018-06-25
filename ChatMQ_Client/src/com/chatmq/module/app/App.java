package com.chatmq.module.app;

import java.util.Scanner;

import com.chatmq.module.connection.ConnectionMQ;
import com.chatmq.module.access.AccessMQ;
import com.chatmq.module.listen.ListenGroup;
import com.chatmq.module.listen.ListenMe;
import com.chatmq.module.services.Services;
import com.chatmq.module.utils.Utils;
import com.rabbitmq.client.Channel;

public class App {
	private final static String pathMyGroups = "C:/Users/caio/workspace/ChatMQ_Client/docs/my_groups.txt";
	
	private static ConnectionMQ conn = new ConnectionMQ();
	private static Channel channel = conn.getChannel();
	private static Services services = new Services();
	private static AccessMQ access = new AccessMQ();
	private static Utils utils = new Utils();
	
	private static Scanner in = new Scanner(System.in);
	private static String username;
	private static String password;
	
	public static void main(String[] argv) throws Exception {
		System.out.println("["+utils.getDate()+"] Bem vindo ao ChatMQ!\n");
		
		int op = -1;
		do {
			System.out.println("___________________________________________________________\n\n");
			System.out.println("[1] - Logar.\n");
			System.out.println("[0] - Sair.\n");
			System.out.println("___________________________________________________________\n\n");
			
			op = utils.option(0, 1);
			switch (op) {
				case 0:
					System.out.println("["+utils.getDate()+"] Você saiu do ChatMQ! x_x");
					channel.close();
				    conn.getConnection().close();
					System.exit(0);
					break;
				
				case 1:
					System.out.println("Username: ");
					username = in.nextLine().trim().replace(" ", "").toLowerCase();
					System.out.println("Password: ");
					password = in.nextLine().trim().replace(" ", "");
					
					if(access.login(channel, username, password)) {
						System.out.println("["+utils.getDate()+"] Usuário logado com sucesso.\n");
						
						String queue_me = username;
						new Thread(new ListenMe(channel, queue_me)).start();
						
						//services.startGroups(channel, pathMyGroups);
						
						op = 0;
					} else {
						System.out.println("["+utils.getDate()+"] Error: Este usuário já existe.\n");
						op = -1;
					}
					break;
					
				default:
					System.out.println("["+utils.getDate()+"] Error: Por favor, escolha uma opção válida.\n");
					break;
			}
		} while(op != 0);
			
		System.out.println("___________________________________________________________\n\n");
		System.out.println("[x] Serviços:\n\n");
		System.out.println("[1] - Enviar mensagem para um usuário.\n");
		System.out.println("[2] - Enviar mensagem para um grupo.\n");
		System.out.println("[3] - Listar usuários.\n");
		System.out.println("[4] - Listar grupos.\n");
		System.out.println("[5] - Listar meus grupos.\n");
		System.out.println("[6] - Criar um grupo.\n");
		System.out.println("[7] - Entrar em um grupo.\n");
		System.out.println("[0] - Sair da aplicação.\n");
		System.out.println("___________________________________________________________\n\n");
		
		do {
			op = utils.option(0, 7);
			switch (op) {
				case 0:
					System.out.println("["+utils.getDate()+"] Você saiu do ChatMQ! x_x");
					channel.close();
				    conn.getConnection().close();
					System.exit(0);
					break;
				
				case 1:
					System.out.println("[x] Formato de envio: [ Usuário, Mensagem ]\n");
					System.out.println("Tag do usuário: ");
					String tag_user = in.nextLine().trim().replace(" ", "");
					System.out.println("Digite sua mensagem: ");
					String messageU = in.nextLine().trim();
					
					services.sendUser(channel, username, tag_user.toLowerCase(), messageU);
					break;
			
				case 2:
					System.out.println("[x] Formato de envio: [ Grupo, Mensagem ]\n");
					System.out.println("Tag do grupo: ");
					String tag_group = in.nextLine().trim().replace(" ", "_");
					System.out.println("Digite sua mensagem: ");
					String messageG = in.nextLine().trim();
					
					services.sendGroup(channel, username, tag_group.toUpperCase(), messageG);
					break;
			
				case 3:
					System.out.println("["+utils.getDate()+"] Solicitando lista...");
					services.getUsers(channel, username);
					break;
			
				case 4:
					System.out.println("["+utils.getDate()+"] Solicitando lista...");
					services.getGroups(channel, username);
					break;
					
				case 5:
					System.out.println("["+utils.getDate()+"] Solicitando lista...");
					utils.readFile(pathMyGroups);
					break;
			
				case 6:
					System.out.println("[x] Formato de resposta: [ TAGNAME ]\n");
					System.out.println("Informe uma TAGNAME para o grupo: ");
					String tagname_group6 = in.nextLine().trim().replace(" ", "_");
					
					String QUEUE_GROUP6 = tagname_group6.toUpperCase();
					
					if(utils.hasLine(pathMyGroups, QUEUE_GROUP6)) {
						//new Thread(new ListenGroup(channel, QUEUE_GROUP6)).start();
						System.out.println("["+utils.getDate()+"] Grupo '"+QUEUE_GROUP6+"' já existe e você entrou!\n");
					} else {
						utils.writeFile(pathMyGroups, QUEUE_GROUP6);
						System.out.println("["+utils.getDate()+"] Grupo '"+QUEUE_GROUP6+"' criado com sucesso!\n");	
					}
					new Thread(new ListenGroup(channel, QUEUE_GROUP6)).start();
					
					services.newGroup(channel, username, QUEUE_GROUP6);
					break;
			
				case 7:
					System.out.println("[x] Formato de resposta: [ TAGNAME ]\n");
					System.out.println("Informe a TAGNAME do grupo que deseja entrar: ");
					String tagname_group7 = in.nextLine().trim().replace(" ", "_");
					
					String QUEUE_GROUP7 = tagname_group7.toUpperCase();
					
					if(utils.hasLine(pathMyGroups, QUEUE_GROUP7)) {
						//new Thread(new ListenGroup(channel, QUEUE_GROUP7)).start();
						System.out.println("["+utils.getDate()+"] Você entrou no novo grupo '"+QUEUE_GROUP7+"'!\n");
					} else {
						utils.writeFile(pathMyGroups, QUEUE_GROUP7);
						System.out.println("["+utils.getDate()+"] Você entrou no grupo '"+QUEUE_GROUP7+"'!\n");
					}
					new Thread(new ListenGroup(channel, QUEUE_GROUP7)).start();
					
					services.newGroup(channel, username, QUEUE_GROUP7);
					break;
					
				default:
					System.out.println("["+utils.getDate()+"] Por favor, escolha um serviço válido.\n");
					break;
			}
			
		} while(op != 0);
	}
}
