package com.chatmq.module.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Utils {
	private static Scanner in = new Scanner(System.in);
	
	public String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
		return sdf.format(new Date());
	}
	
	public void readFile(String path) {
		BufferedReader fileRead;
        String line = "";
        
		try {
			fileRead = new BufferedReader(new FileReader(path));

	        while (true) {
	        	line = fileRead.readLine();
	            if (line != null) {
	                System.out.println(line);
	            } else {
	            	break;
	            }
	        }
	        fileRead.close();
		} catch (FileNotFoundException e) {
			System.out.println("["+getDate()+"] Error readFile Utils - "+e.getMessage());
		} catch (IOException e) {
			System.out.println("["+getDate()+"] Error readFile Utils - "+e.getMessage());
		}
	}
	
	public void writeFile(String path, String line) {
		BufferedWriter fileWrite;
		BufferedReader fileRead;
		ArrayList<String> file = new ArrayList<>();
		String lineRead = "";
		
		try {
			fileRead = new BufferedReader(new FileReader(path));	
			while (true) {
				lineRead = fileRead.readLine();
	            if (lineRead != null) {
	            	file.add(lineRead);
	            } else {
	            	break;
	            }
	        }
			fileRead.close();
		
			fileWrite = new BufferedWriter(new FileWriter(path));
			for (String l : file) {
				fileWrite.append(l);
				fileWrite.newLine();
			}
			fileWrite.append(line);
			fileWrite.newLine();
			fileWrite.close();
		} catch (FileNotFoundException e) {
			System.out.println("["+getDate()+"] Error writeFile Utils - "+e.getMessage());
		} catch (IOException e) {
			System.out.println("["+getDate()+"] Error writeFile Utils - "+e.getMessage());
		}
	}
	
	public void removeLine(String path, String line) {
		BufferedWriter fileWrite;
		BufferedReader fileRead;
		ArrayList<String> file = new ArrayList<>();
		String lineRead = "";
		try {
			fileRead = new BufferedReader(new FileReader(path));	
			while (true) {
				lineRead = fileRead.readLine();
	            if (lineRead != null) {
	            	file.add(lineRead.trim());
	            } else {
	            	break;
	            }
	        }
			fileRead.close();
		
			fileWrite = new BufferedWriter(new FileWriter(path));
			for (String l : file) {
				if(l.equals(line)) continue;
				fileWrite.append(l);
				fileWrite.newLine();
			}
			fileWrite.close();
		} catch (FileNotFoundException e) {
			System.out.println("["+getDate()+"] Error removeLine Utils - "+e.getMessage());
		} catch (IOException e) {
			System.out.println("["+getDate()+"] Error removeLine Utils - "+e.getMessage());
		}
	}

	public String getLineFirst(String path, String line) {
		try {
			BufferedReader fileRead = new BufferedReader(new FileReader(path));	
			while (true) {
				String lineRead = fileRead.readLine();
	            if (lineRead != null) {
					String[] parts = lineRead.split(":");
	            	if (line.equals(parts[0])) {
	            		return lineRead;
	            	}
	            } else {
	            	break;
	            }
	        }
			fileRead.close();
		} catch (FileNotFoundException e) {
			System.out.println("["+getDate()+"] Error removeLine Utils: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("["+getDate()+"] Error removeLine Utils: " + e.getMessage());
		}
		return null;
	}

	public boolean hasLine(String path, String line) {
		try {
			BufferedReader fileRead = new BufferedReader(new FileReader(path));
			while(true) {
				String lineRead = fileRead.readLine();
				if(lineRead != null) {
					if(line.equals(lineRead))
						return true;
				} else {
					break;
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("["+getDate()+"] Error hasLine Utils: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("["+getDate()+"] Error hasLine Utils: " + e.getMessage());
		}
		return false;
	}
	
	public boolean hasPart(String line, String part) {
		String[] parts = line.split(":");
		for(String a : parts) {
			if(a.equals(part))
				return true;
		}
		return false;
	}
	
	public String toString(String path) {
		String to = "";
		try {
			BufferedReader fileRead = new BufferedReader(new FileReader(path));
			while(true) {
				String lineRead = fileRead.readLine();
				if(lineRead != null) {
					to += lineRead+"\n";
				} else {
					break;
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("["+getDate()+"] Error hasLine Utils: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("["+getDate()+"] Error hasLine Utils: " + e.getMessage());
		}
		return to;
	}

	public int option(int i, int f) {
		int num = in.nextInt();
		if(num >= i && num <= f)
			return num;
		return -1;
	}
}
