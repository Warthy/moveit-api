package fr.moveit.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

@Data
@Entity
public class Interest {
	@Id
	@GeneratedValue
	private Long id;

	private String name;

	@Enumerated(EnumType.STRING)
	private InterestType type;

	@JsonIgnore
	@ManyToMany(mappedBy = "interest")
	private Collection<User> users;

}
