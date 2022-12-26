package com.notesapp.notesapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NotesApp {

	public static void main(String[] args) {
		try {
			Class.forName("org.postgresql.Driver");
			System.out.println("Success");
		} catch(ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		SpringApplication.run(NotesApp.class, args);
	}

}