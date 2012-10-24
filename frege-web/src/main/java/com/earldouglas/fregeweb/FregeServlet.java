package com.earldouglas.fregeweb;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.earldouglas.fregeweb.FregeWeb.TRequest;
import com.earldouglas.fregeweb.FregeWeb.TResponse;

import frege.prelude.PreludeBase.TList;
import frege.prelude.PreludeBase.TList.DCons;
import frege.prelude.PreludeBase.TList.DList;
import frege.rt.Box;
import frege.rt.FV;
import frege.rt.Lam1;
import frege.rt.Lazy;

@SuppressWarnings("serial")
public class FregeServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {

		TRequest request = TRequest.mk(Box.mk("GET"), Box.mk(uriOf(req)), DList.mk(), DList.mk(), Box.mk(TList.DList.mk()));
		TResponse response = (TResponse) FregeWeb.service(request);

		TList responseBodyL = (TList) TResponse.body(response);
		write(responseBodyL, res.getOutputStream());
	}

	public static void write(TList chars, OutputStream outputStream) throws IOException {
		TList curr = chars;
		while (curr instanceof DCons) {
			DCons cons = (DCons) curr;
			
			frege.rt.Box.Byte by = (frege.rt.Box.Byte) cons.mem1._e();
			byte b = by.j;
			outputStream.write(b);

			curr = (TList) cons.mem2._e();
		}
	}

	public static TResponse service(TRequest request) {
		Lam1 service1 = (Lam1) FregeWeb.service(request);
		Lazy<FV> service2 = service1.eval(Box.mk(request));
		return (TResponse) service2._e();
	}

	public static String uriOf(HttpServletRequest req) {
		if (req.getRequestURI().startsWith(req.getServletPath())) {
			return req.getRequestURI().substring(req.getServletPath().length());
		} else {
			return req.getRequestURI();
		}
	}
}
