package pl.edu.agh.kis.soa.resources.model;

import java.util.List;

public class Student {
	private String firstName;
	private String lastName;
	private String albumNo;
	private List<String> subjects;

	public Student() {
		
	}
	
	public Student(String firstName, String lastName, String id) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.albumNo = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAlbumNo() {
		return albumNo;
	}

	public void setAlbumNo(String albumNo) {
		this.albumNo = albumNo;
	}

	public List<String> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<String> subjects) {
		this.subjects = subjects;
	}

}
