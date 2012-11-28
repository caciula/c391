package main.util;

public class ReportRow{
	
	public String col1;
	
	public String col2;
	
	public String col3;
	
	public String col4;
	
	public ReportRow(){
		this.col1 = "";
		this.col2 = "";
		this.col3 = "";
		this.col4 = "";
	}
	
	public String getCol1(){
		return col1;
	}
	
	public void setCol1(String c){
		this.col1 = c;
	}
	
	public String getCol2(){
		return col2;
	}
	
	public void setCol2(String c){
		this.col2 = c;
	}
	
	public String getCol3(){
		return col3;
	}
	
	public void setCol3(String c){
		this.col3 = c;
	}
	
	public String getCol4(){
		return col4;
	}
	
	public void setCol4(String c){
		this.col4 = c;
	}
}