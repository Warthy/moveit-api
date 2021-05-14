package fr.moveit.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Activity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDateTime createdAt;

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

	private LocalDateTime start;

	private LocalDateTime end;

}
