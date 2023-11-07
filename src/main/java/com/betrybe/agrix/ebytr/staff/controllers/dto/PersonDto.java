package com.betrybe.agrix.ebytr.staff.controllers.dto;

import com.betrybe.agrix.ebytr.staff.entity.Person;
import com.betrybe.agrix.ebytr.staff.security.Role;

/**
 * PersonDto.
 */
public record PersonDto(Long id, String username, String password, Role role) {
  /**
   * Método fromPerson.
   */
  public PersonDto fromPerson(Person person) {
    return new PersonDto(
        person.getId(),
        person.getUsername(),
        person.getPassword(),
        person.getRole()
    );
  }

  /**
   * Método toPerson.
   */
  public Person toPerson() {
    Person person = new Person();
    person.setUsername(username);
    person.setPassword(password);
    person.setRole(role);
    return person;
  }
}
