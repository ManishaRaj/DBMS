

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.net.aso.s;

/**
 * Servlet implementation class Query10Result
 */
@WebServlet("/getQuery10")
public class getQuery10 extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public getQuery10() {
        super();
        // TODO Auto-generated constructor stub
    }

    String getOutput(ResultSet rs, String carrier, String startDate, String endDate) throws SQLException{

    	String startHtml = "<!DOCTYPE html ><html lang=\"en\"><head>	<meta charset=\"utf-8\">	<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">	<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">	<meta name=\"author\" content=\"Magdalena Myka\">	<title>Flight Search</title>	<!-- Latest compiled and minified CSS -->	<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css\">	<!-- Optional theme -->	<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap-theme.min.css\">	<link rel=\"stylesheet\" href=\"http://code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css\">	<style>	body {	background:url('http://www.beautiful-views.net/views/clouds-plane-sky-flight.jpg') no-repeat 0px 0px;	background-size: cover;	font-family: 'Open Sans', sans-serif;	background-attachment: fixed;    background-position: center;}</style></head><body>	<div class=\"container\">		<div class=\"row\">			<div class=\"col-md-3\">				<!-- Refine price -->			</div>			<!-- Search results -->			<div class=\"col-sm-12 col-md-9\">				<!-- First row with header and dates -->				<div class=\"row\" id=\"results-header\">					<div class=\"col-md-6\">"
    			+ "	</div>	</div>	<!-- All results (these are sample static data)-->				<div class=\"row\">					<div class=\"col-md-8\" id=\"results\">";

    	String endHtml = "</div> <!-- End of search results -->				</div>			</div>		</div>	</div> <!-- end of container -->";
    	String res = "<div class=\"panel panel-default\"> "
				+ "<div class=\"panel-body\"> "
				+ "<div class=\"col-sm-2 col-md-12 vertically-centered\"> "
				+ "<ul class=\"list-unstyled\"> "
				+ "<li>"+ "<b> Delay trend of "+carrier+ " from " + startDate+" to "+ endDate+" </b> </li> "
				+ "</ul> "
				+ "</div> "
				+ "</div> "
				+ "</div>";

    	Double d;
    	DecimalFormat numberFormat = new DecimalFormat("#0.00");
    	while(rs.next()){
    		d = Double.valueOf(rs.getString(2));
			//System.out.println(rs.getString(2)+" "+rs.getString(3)+" "+rs.getString(4)+"\n");
			res = res + "<div class=\"panel panel-default\"> "
					+ "<div class=\"panel-body\"> "
					+ "<div class=\"col-sm-4 col-md-3 vertically-centered\"> "
					+ "<ul class=\"list-unstyled\"> "
					+ "<li>"+ rs.getString(1).split(" ")[0]+"</li> "
					+ "</ul> "
					+ "</div> "
					+ "<div class=\"col-sm-4 col-md-3 vertically-centered\"> "
					+ "<ul class=\"list-unstyled\"> "
					+ "<li>"+ numberFormat.format(d) +" Minutes</li> "
					+ "</ul> "
					+ "</div> "
					+ "</div> "
					+ "</div>";


		}
    	return startHtml+res+endHtml;
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		String start, end, carrier;

		start = request.getParameter("startDate");
		end = request.getParameter("endDate");
		carrier = request.getParameter("carrier").split(" - ")[0];

		try{
			//step1 load the driver class
			Class.forName("oracle.jdbc.driver.OracleDriver");

			//step2 create  the connection object
			Connection con=DriverManager.getConnection(
					"jdbc:oracle:thin:@oracle.cise.ufl.edu:1521:orcl","thota", "jayakrishna");

			//step3 create the statement object
			Statement stmt=con.createStatement();

			//step4 execute query
			String query = "Select FlightDate, avg(CarrierDelay) as CarrierDelayofaDay "
							+ " from PASTFLIGHTSCHEDULE p"
							+ "	Where p.Operatedby like '" + carrier + "' and "
							+ " FlightDate >= to_date('"+ start +"', 'yyyy-mm-dd')" + " and "
							+ " FlightDate <= to_date('"+ end +"', 'yyyy-mm-dd') "
							+ "Group by FlightDate Order by FlightDate asc";
			System.out.println(query);
			ResultSet rs=stmt.executeQuery(query);
			out.println(getOutput(rs, request.getParameter("carrier").split(" - ")[1], start, end));


			System.out.println("Done");
			//step5 close the connection object
			con.close();

			}catch(Exception e){ System.out.println(e);}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
