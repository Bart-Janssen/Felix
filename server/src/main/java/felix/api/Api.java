package felix.api;

import felix.api.configuration.Connection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

@SpringBootApplication
public class Api
{
	public static void main(String[] args)
	{
		new Thread(Api::startServer).start();
		SpringApplication.run(Api.class, args);
	}

	private static void startServer()
	{
		System.setProperty("org.eclipse.jetty.util.log.class", "org.eclipse.jetty.util.log.StdErrLog");
		System.setProperty("org.eclipse.jetty.LEVEL", "OFF");
		Server webSocketServer = new Server();
		ServerConnector connector = new ServerConnector(webSocketServer);
		connector.setPort(6666);
		webSocketServer.addConnector(connector);
		ServletContextHandler webSocketContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
		webSocketContext.setContextPath("/");
		webSocketServer.setHandler(webSocketContext);
		try
		{
			WebSocketServerContainerInitializer.configureContext(webSocketContext).addEndpoint(Connection.class);
			webSocketServer.start();
			webSocketServer.join();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}