package servlet;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
        name = "DialogflowServlet", 
        urlPatterns = {"/DialogflowServlet"}
    )
public class DialogflowServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String link = "https://api.dialogflow.com/v1/query?v=20170712&lang=en&timezone=Asia/Shanghai&query=%s&sessionId=%s";
	private String Authorization = "Bearer 68c6f32f4dac4dceaa3f66b2c9475e51";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// q=hi&sessionId=5681b74d-5c57-0b22-65b2-af07641b705f
		String q = request.getParameter("q");
		String sid = request.getParameter("sessionId");
		URL url = new URL(String.format(link, q, sid));

		//Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.40.14.55", 8080));
		//HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.addRequestProperty("Authorization", Authorization);
		conn.addRequestProperty("Content-Type", "application/json");
		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(false);
		conn.connect();

		final byte[] buffer = new byte[1024];
		int statusCode = conn.getResponseCode();
		response.setStatus(statusCode);
		while (true) {
			final int read = conn.getInputStream().read(buffer);
			if (read <= 0)
				break;
			response.getOutputStream().write(buffer, 0, read);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
