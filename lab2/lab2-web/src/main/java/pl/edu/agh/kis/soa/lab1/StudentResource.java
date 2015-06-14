package pl.edu.agh.kis.soa.lab1;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import pl.edu.agh.kis.soa.resources.model.Student;


@Stateless
@Path("/rest")
public class StudentResource {
	private static final Logger logger = Logger.getLogger("StudentResource");

	private static final String LOGIN_KEY = "LOGIN_KEY";

	private static List<Student> studentList;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/students")
	public Response getStudentList(@Context HttpServletRequest request){
		HttpSession session = request.getSession();
		String login = (String) session.getAttribute(LOGIN_KEY);
		if(login != null){
			logger.info("returning studentList in number:" + studentList.size());
			return Response.ok(studentList, MediaType.APPLICATION_JSON).build();
		}
		else
			return Response.status(Response.Status.UNAUTHORIZED).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/student")
	public Response createStudent(Student student, @Context HttpServletRequest request){
		HttpSession session = request.getSession();
		String login = (String) session.getAttribute(LOGIN_KEY);
		if(login != null){
			if(studentList == null)
				studentList = new ArrayList<Student>();
			studentList.add(student);
			logger.info("new student created");
			return Response.ok().build();
		}
		else
			return Response.status(Response.Status.UNAUTHORIZED).build();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/student")
	public Response updateStudent(Student student, @Context HttpServletRequest request){
		HttpSession session = request.getSession();
		String login = (String) session.getAttribute(LOGIN_KEY);
		if(login != null){
			logger.info("returning studentList in number:" + studentList.size());
			for(Student stud : studentList)
				if(stud.getAlbumNo().equals(student.getAlbumNo())){
					stud.setFirstName(student.getFirstName());
					stud.setLastName(student.getLastName());
					if(student.getSubjects() != null)
						stud.setSubjects(new ArrayList<String>(student.getSubjects()));
					return Response.ok().build();
				}					
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		else
			return Response.status(Response.Status.UNAUTHORIZED).build();
	}
	
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/student/{id}")
	public Response deleteStudent(@PathParam(value = "id") String albumNo, @Context HttpServletRequest request){
		HttpSession session = request.getSession();
		String login = (String) session.getAttribute(LOGIN_KEY);
		if(login != null){
			for(int i=0;i<studentList.size();i++){
				if(albumNo.equals(studentList.get(i).getAlbumNo())){
					studentList.remove(i);
					logger.info("student removed");
					return Response.ok().build();
				}
			}			
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		else
			return Response.status(Response.Status.UNAUTHORIZED).build();
	}
	

	@POST
	@Path("/authorize")
	@Produces(MediaType.APPLICATION_JSON)
	public Response authorize(@QueryParam("login") String login, @QueryParam("password") String password, @Context HttpServletRequest request){
		logger.info(String.format("%s", "authorize invoked, login" + login));
		HttpSession httpSession = request.getSession();
		if(httpSession == null)
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		if(!"login".equals(login) || !"password".equals(password))
			return Response.status(Response.Status.UNAUTHORIZED).build();

		httpSession.setAttribute(LOGIN_KEY, login);
		return Response.ok().build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("helloAuthorized")
	public Response helloAuthorized(@QueryParam("id") String id, @Context HttpServletRequest request){
		logger.info("hello authorized invoked");
		HttpSession session = request.getSession();
		if(session == null){
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		String login = (String) session.getAttribute(LOGIN_KEY);
		logger.info("hello authorized, user login " + login);
		if(login != null){
			Student s = new Student("jan", "nowak", id);
			return Response.ok(s, MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}		
	}
}