package edu.brown.cs.student.main.domain;

/** Represents an animal with a unique identifier, name, and species. */
public class Animal {
  private final int id;
  private final String name;
  private final String species;

  /**
   * Constructs for Animal instance with the id, name, and species.
   *
   * @param id The unique identifier for the animal. Type int.
   * @param name The name of the animal.
   * @param species The species to which the animal belongs.
   */
  public Animal(int id, String name, String species) {
    this.id = id;
    this.name = name;
    this.species = species;
  }

  /**
   * Returns the animal's identifier.
   *
   * @return The id of the animal.
   */
  public int getId() {
    return id;
  }

  /**
   * Returns the animal's name.
   *
   * @return The name of the animal.
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the animal's species.
   *
   * @return The species of the animal.
   */
  public String getSpecies() {
    return species;
  }
}
