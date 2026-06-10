package fr.apgis4.tp508.Model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@JacksonXmlRootElement(localName = "etudiant")
public class Etudiant {
  @Id
  private Integer id;
  @NotBlank(message = "Le nom est obligatoire")
  private String nom;
  @NotBlank(message = "Le prénom est obligatoire")
  private String prenom;

  @Min(value = 18, message = "L'âge doit être supérieur ou égal à 18 ans")
  @Max(value = 25, message = "L'âge doit être inférieur ou égal à 25 ans")
  private Integer age;

  @Size(max = 1, message = "Le groupe doit contenir qu'un seul caractère")
  private String groupe;
}
