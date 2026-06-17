package fr.but3.simondesdevises.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Question {

	@Id
	private Integer qno;

	private String libelle;

	private boolean active;

	@OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
	@OrderBy("cno asc")
	private List<Choix> choix = new ArrayList<>();
}
