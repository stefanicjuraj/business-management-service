package businesslayer;

public class DepartmentBusiness extends Business {

    /**
     * Creates a new instance of the 'DepartmentBusiness' class. It inherits the
     * initialization of data access and Gson objects from the parent 'Business'
     * class to maintain consistency across the application's business logic.
     */
    public DepartmentBusiness() {
        super(); // Calls the constructor of the parent class.
    }

    /**
     * Retrieves a list of departments for a given company.
     *
     * @param companyName the name of the company for which departments are to
     *                    be retrieved.
     * @return a JSON response containing a list of departments or an error
     *         message if no departments are found.
     */
    public Response getAll(String companyName) {

        try {
            // Attempt to retrieve all departments for the given company name.
            List<Department> departments = this.dl.getAllDepartment(companyName);

            // Check if no departments were found or if the given company name does not
            // match the expected one.
            if (departments.isEmpty() || !companyName.equals(BusinessConfig.COMPANY_NAME)) {
                // Return a response indicating that no departments were found for the specified
                // company.
                return Response.status(Response.Status.OK)
                        .entity("{\"error\": \"No departments found for the specified company: " + companyName
                                + ".\"}")
                        .build();
            }

            // If departments were found, return them in the response.
            return Response.status(Response.Status.OK).entity(departments).build();
        } catch (Exception e) {
            // In case of a database connection failure, return an error message.
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Unable to connect to the database. Detailed message: " + e.getMessage()
                            + "\"}")
                    .build();
        }
    }

}
