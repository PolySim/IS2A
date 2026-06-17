package fr.but3.simondesdevises.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Choix {

	@Id
	private Integer cno;

	private String libchoix;

	private boolean statut;

	private int nbChoix;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "qno", nullable = false)
	private Question question;
}
