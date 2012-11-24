package main.util;

public class ReportRow{
	
	public String user;
	
	public String subject;
	
	public String total;
	
	public ReportRow(){
	}
	
	public String getUser(){
		return user;
	}
	
	public void setUser(String u){
		this.user = u;
	}
	
	public String getSubject(){
		return subject;
	}
	
	public void setSubject(String s){
		this.subject = s;
	}
	
	public String getTotal(){
		return total;
	}
	
	public void setTotal(String t){
		this.total = t;
	}
}