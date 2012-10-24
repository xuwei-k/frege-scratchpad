package com.earldouglas.fregeweb;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.webapp.WebAppContext;

public class FregeWebLauncher {
	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);

		ServletHolder sh = new ServletHolder();
		sh.setServlet(new FregeServlet());

		WebAppContext wac = new WebAppContext();
		wac.setWar("src/main/webapp");
		wac.setContextPath("/");
		server.setHandler(wac);

		server.start();
		server.join();
	}
}