/**
 * John Meyer
 * CSE 271
 * Dr. Angel Bravo
 * Lab03
 */
public class Address {

    // Define instance variables
    private int houseNumber;
    private String street;
    private int apartmentNumber;
    private String city;
    private String state;
    private int zipCode;

    /**
     * Gets the house number
     * @return The house number
     */
    public int getHouseNumber() {
        return houseNumber;
    }

    /**
     * Gets the street
     * @return The street
     */
    public String getStreet() {
        return street;
    }

    /**
     * Gets the apartment number
     * @return The apartment number or -1 if it does not exist
     */
    public int getApartmentNumber() {
        return apartmentNumber;
    }

    /**
     * Gets the city
     * @return The city
     */
    public String getCity() {
        return city;
    }

    /**
     * Gets the state
     * @return The state
     */
    public String getState() {
        return state;
    }

    /**
     * Gets the zip code
     * @return The zip code
     */
    public int getZipCode() {
        return zipCode;
    }

    /**
     * Sets house number
     * @param houseNumber The house number; must be greater than 0
     */
    public void setHouseNumber(int houseNumber) {
        if (houseNumber >= 0) {
            this.houseNumber = houseNumber;
        }
    }

    /**
     * Sets street
     * @param street The street; must be longer than 0
     */
    public void setStreet(String street) {
        if (street.length() > 0) {
            this.street = street;
        }
    }

    /**
     * Sets apartment number
     * @param apartmentNumber The apartment number; must be greater than 0
     */
    public void setApartmentNumber(int apartmentNumber) {
        if (apartmentNumber >= 0) {
            this.apartmentNumber = apartmentNumber;
        }
    }

    /**
     * Sets city
     * @param city The city; must be longer than 0
     */
    public void setCity(String city) {
        if (city.length() > 0) {
            this.city = city;
        }
    }

    /**
     * Sets state
     * @param state The state; must be in two-character abbreviation
     */
    public void setState(String state) {
        state = parseState(state);
        if (!state.equals("N/A")) {
            this.state = state;
        }
    }

    /**
     * Sets zip code
     * @param zipCode The zip code; must be greater than 0 and have five digits
     */
    public void setZipCode(int zipCode) {
        if (zipCode > 0 && zipCode <= 99999) {
            this.zipCode = zipCode;
        }
    }

    /**
     * Determines whether a string represents a U.S. state
     * @param state The string to check
     * @return Returns a standardized state notation or "N/A" if the state is invalid
     */
    private String parseState(String state) {
        state.toUpperCase();
        String[] stateAbrevs = {
            "AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA",
            "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD",
            "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ",
            "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC",
            "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"
        };
        for (String stateAbrev : stateAbrevs) {
            if (state.equals(stateAbrev)) {
                return state;
            }
        }
        return "N/A";
    }

    /**
     * Returns a address in written form
     * @return The string of the written address
     */
    public String toString() {
        String fullAddress = "";
        fullAddress += houseNumber;
        fullAddress += street;
        if (apartmentNumber != -1) {
            fullAddress += apartmentNumber;
        }
        fullAddress += city;
        fullAddress += state;
        fullAddress += zipCode;
        return fullAddress;
    }

    /**
     * Determines whether the argument address comes before this address (whether its zip code is less than this
     * zip code)
     * @param other The other address
     * @return True if other's zip code < this zip code
     */
    public boolean comesBefore(Address other) {
        return other.getZipCode() < this.zipCode;
    }

    // Define constructors
    public Address(int houseNumber, String street, String city, String state, int zipCode) {
        this.houseNumber = houseNumber;
        this.street = street;
        this.apartmentNumber = -1;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.street = street;
    }

    public Address(int houseNumber, String street, int apartmentNumber, String city, String state, int zipCode) {
        this.houseNumber = houseNumber;
        this.street = street;
        this.apartmentNumber = apartmentNumber;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

}
