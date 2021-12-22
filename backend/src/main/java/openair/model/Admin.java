package openair.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@DiscriminatorValue("Admin")
@Getter
@Setter
public class Admin extends User {



}
