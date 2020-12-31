
import java.io.*;
import javax.jms.*;
import javax.naming.*;
import javax.servlet.http.*;
import javax.servlet.ServletException;

public class QueueSenderServlet extends HttpServlet
{
	static PrintWriter out;

	public final static String CNN_FACTORY="/ConnectionFactory";

	public final static String QUEUE_NAME="queue/Test";

	private QueueConnectionFactory qconFactory;
	private QueueConnection qcon;
	private QueueSession qsession;
	private static QueueSender qsender;
	private Queue queue;
	private static TextMessage msg;

	
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
 	 {
		try
		{
			String queueName = "queue/ECGqueue";
			out=response.getWriter();
			InitialContext ic = new InitialContext();
			qconFactory = (QueueConnectionFactory) ic.lookup(CNN_FACTORY);
			qcon = qconFactory.createQueueConnection();
			qsession = qcon.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			queue = (Queue) ic.lookup(queueName);
			qsender = qsession.createSender(queue);
			msg = qsession.createTextMessage();
			qcon.start();
			
				out.println("");
				out.println("");
				out.println("");
				out.println("<h1>Queue Sender Servlet</h1>");
				out.println("Following Messages has been sent !!!");
				out.println("====================================");
						msg.setText("this is servlet text message"); 	// Messages
						qsender.send(msg); 	// Messages sent
						out.println("Message Sent");
				
				out.println("====================================");
				out.println("");
				out.println("");
				out.println("");
			
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
