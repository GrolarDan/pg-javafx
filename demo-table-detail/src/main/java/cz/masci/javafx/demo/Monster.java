package cz.masci.javafx.demo;

/**
 *
 * @author Daniel
 */
public class Monster {
  private String name;
  private String description;

  public Monster(String name, String description) {
    this.name = name;
    this.description = description;
  }

  
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
