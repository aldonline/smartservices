package com.linktegration.smartservices;

public class ServiceManager {

	
	//executor:Executor = null
	
	
	public void triggerEvent( Event e ){
		
	}
	

	
	private static ServiceManager instance = null;
	public static ServiceManager getInstance(){
		if ( instance == null )
			instance = new ServiceManager();
		return instance;
	}
}
