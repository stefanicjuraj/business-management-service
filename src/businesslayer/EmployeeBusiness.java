package businesslayer;

import java.util.List;

public class EmployeeBusiness extends Business {

    /**
     * Creates a new instance of the 'EmployeeBusiness' class. It inherits the
     * initialization of data access and Gson objects from the parent 'Business'
     * class to maintain consistency across the application's business logic.
     */
    public EmployeeBusiness() {
        super(); // Calls the constructor of the parent class.
    }

    /**
     * Retrieves a list of employees for a given company.
     *
     * @param companyName the name of the company of which employees are to be
     *                    retrieved.
     * @return a JSON response containing a list of employees or an error
     *         message if no employees are found.
     */
    public Response getAll(String companyName) {
        try {
            // Retrieve all employees for the given company name
            List<Employee> employees = this.dl.getAllEmployee(companyName);

            // Check if the employees list is empty or if the company name does not match
            // the expected value
            if (employees.isEmpty() || !companyName.equals(BusinessConfig.COMPANY_NAME)) {
                // Return an OK response with a message indicating no employees were found for
                // the company
                return Response.status(Response.Status.OK)
                        .entity("{\"error\": \"No employees found for the company name provided: " + companyName
                                + ".\"}")
                        .build();
            }

            // Return an OK response with the list of employees
            return Response.status(Response.Status.OK).entity(employees).build();

        } catch (Exception e) {
            // Return an Internal Server Error response with a message indicating a database
            // connection failure
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to connect to the database: " + e.getMessage() + ".\"}").build();
        }
    }

}
