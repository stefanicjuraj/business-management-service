package businesslayer;

import com.google.gson.*;

/**
 * The 'Business' class is a base class (parent) and a part of the business
 * layer of the application responsible for handling business logic common to
 * all classes found in the business layer. This class initializes the data
 * layer to perform operations related to the company's data and Gson (Google's
 * JSON library) for data serialization.
 *
 */
public class Business {

    /**
     * DataLayer instance for accessing data from the data layer.
     */
    protected DataLayer dl = null;

    /**
     * Gson instance for JSON serialization and deserialization.
     */
    protected Gson gson = null;

    /**
     * Constructs a new `Business` object. It initializes the DataLayer instance
     * using the company name specified in the 'BusinessConfig' class and
     * configures the Gson instance to serialize Date objects according to the
     * pattern "yyyy-MM-dd HH:mm:ss." If any exception occurs during
     * initialization, it prints an error message.
     */
    public Business() {
        try {
            this.dl = new DataLayer(BusinessConfig.COMPANY_NAME);
            this.gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        } catch (Exception ex) {
            System.out.println("Error while connecting to the DB: " + ex.getMessage());
        } finally {
            dl.close();
        }
    }

}
