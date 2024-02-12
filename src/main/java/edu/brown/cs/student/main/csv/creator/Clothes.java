package edu.brown.cs.student.main.csv.creator;

/** Represents clothing, characterized by a unique code, name, price, and sale status. */
public class Clothes {
  private final String code;
  private final String name;
  private final double price;
  private final boolean sale;

  /**
   * Constructs a Clothes instance with the provided code, name, price, and sale status.
   *
   * @param code The unique code for the clothing item.
   * @param name The name of the clothing item.
   * @param price The price of the clothing item.
   * @param sale The sale status of the clothing item; true if the item is on sale, false otherwise.
   */
  public Clothes(String code, String name, double price, boolean sale) {
    this.code = code;
    this.name = name;
    this.price = price;
    this.sale = sale;
  }

  /**
   * Returns the unique code of the clothing item.
   *
   * @return The code of the clothing item.
   */
  public String getCode() {
    return code;
  }

  /**
   * Returns the name of the clothing item.
   *
   * @return The name of the clothing item.
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the price of the clothing item.
   *
   * @return The price of the clothing item.
   */
  public Double getPrice() {
    return price;
  }

  /**
   * Returns the sale status of the clothing item.
   *
   * @return true if the item is on sale.
   */
  public boolean getSale() {
    return sale;
  }
}
