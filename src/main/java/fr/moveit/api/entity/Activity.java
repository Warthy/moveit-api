package fr.moveit.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Activity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Date createdAt;

	@ManyToOne
	private Interest interest;

	@ManyToOne(cascade = CascadeType.ALL)
	private User author;

	@ManyToMany
	private Set<User> participants;

	@Enumerated(EnumType.STRING)
	private ActivityVisibility visibility;

	private String name;

	@Column(length = 500)
	private String description;

	private String location;

	private Float price;

	private Date start;

	private Date end;

}
