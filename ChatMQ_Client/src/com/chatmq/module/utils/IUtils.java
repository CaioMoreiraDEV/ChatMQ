package com.chatmq.module.utils;

public interface IUtils {

	public abstract String getDate();
	
	public abstract void readFile(String path);
	
	public abstract void writeFile(String path, String line);
	
	public abstract void removeLine(String path, String line);
	
	public abstract String getLineFirst(String path, String line);
	
	public abstract boolean hasLine(String path, String line);
	
	public abstract String toString(String path);

	public abstract boolean hasPart(String line, String part);
	
	public abstract int option(int i, int f);
}
