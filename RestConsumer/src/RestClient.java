

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class RestClient {
	private static final String WEBSERVICE_URL = "http://localhost:8082/lab2-web/rest";
	private Client wsClient;

	public static void main(String[] args) {
		RestClient restClient = new RestClient();
		restClient.authorizeLogin("login", "password");
		restClient.addStudent();
		restClient.getStudents();

		
	}

	public RestClient() {
		wsClient = ClientBuilder.newClient();
	}

	public void getStudents() {
		WebTarget wt = wsClient.target(WEBSERVICE_URL + "/students");
		Invocation webServiceCall = wt.request().build("GET");
//		Response response = webServiceCall.invoke();
		
		Student[] students = webServiceCall.invoke(Student[].class);
		printStudents(students);		
	}

	public void addStudent() {
		WebTarget wt = wsClient.target(WEBSERVICE_URL + "/student");
		String input = "{\"firstName\":\"Jan\",\"lastName\":\"Kowalski\",\"albumNo\":\"265859\"}";
		Invocation webServiceCall = wt.request().accept("application/json")
				.build("POST", Entity.json(input));
		Response response = webServiceCall.invoke();
		if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
			System.out.println("BAD REQUEST");
		} 
		else
			System.out.println("User added!");
		response.close();
	}

	public void authorizeLogin(String login, String password) {
		String path = WEBSERVICE_URL + "/authorize?login=" + login
				+ "&password=" + password;
		WebTarget wt = wsClient.target(path);
		Invocation webServiceCall = wt.request().build("POST");
		Response response = webServiceCall.invoke();
		if (response.getStatus() == Response.Status.UNAUTHORIZED.getStatusCode()) {
			System.out.println("B³êdne dane.");			
		}else if(response.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()){
			System.out.println("B³¹d serwera");
		}else {
			System.out.println("Poprawne dane.");
			response.close();
//			webServiceCall = wt.request().buildGet();
//			response = webServiceCall.invoke();
			System.out.println("OK");
		}
	}
		
	private void printStudents(Student[] students) {
		for (Student st : students) {
			System.out.println(st.getFirstName() + " " + st.getLastName() + " " + st.getAlbumNo());
		}
	}
}
