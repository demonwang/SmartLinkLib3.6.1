package com.example.smartlinklib;

import java.net.InetAddress;

public class ModuleInfo {
	private String mac;
	private String ModuleIP;
	private String mid;

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getModuleIP() {
		return ModuleIP;
	}

	public void setModuleIP(String moduleIP) {
		this.ModuleIP = moduleIP;
	}

	public void setMid(String string) {
		// TODO Auto-generated method stub
		this.mid = string;
	}
	public String getMid(){
		return this.mid;
	}
}
