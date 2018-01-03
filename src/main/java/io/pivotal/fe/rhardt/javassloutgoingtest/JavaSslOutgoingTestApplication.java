package io.pivotal.fe.rhardt.javassloutgoingtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@SpringBootApplication
public class JavaSslOutgoingTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaSslOutgoingTestApplication.class, args);
	}

	@Controller
	class TestController{

		@GetMapping("/")
		public String testForm(Model model) {
			model.addAttribute(new Test());
			return "test";
		}

		@PostMapping("/")
		public String testFormSubmit(@ModelAttribute Test test, Model model) {
			model.addAttribute("result", poke(test.getHost(), test.getPort()));
			return "test";
		}

		private Object poke(String host, int port) {
			try {
				SSLSocketFactory sssf = (SSLSocketFactory)SSLSocketFactory.getDefault();
				SSLSocket sss = (SSLSocket)sssf.createSocket(host, port);

				//for SNI
				SSLParameters sslParams = new SSLParameters();
				sslParams.setEndpointIdentificationAlgorithm("HTTPS");
				sss.setSSLParameters(sslParams);
				//
				
				InputStream is = sss.getInputStream();
				OutputStream os = sss.getOutputStream();
				os.write(42);
				while(is.available() > 0){
                    System.out.print(is.read());
                }
			} catch (IOException e) {
				e.printStackTrace();
				return e.getMessage();
			}
			return "Success";
		}


	}


}
