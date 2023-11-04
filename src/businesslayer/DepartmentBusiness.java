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

    /**
     * Retrieves information about a specific department.
     *
     * @param companyName The name of the company.
     * @param deptId      The unique identifier of the department.
     * @return A JSON response containing the department's information or an
     *         error message if the department is not found.
     */
    public Response get(String companyName, int deptId) {
        try {
            // Validate the company name against the business configuration.
            if (!companyName.equals(BusinessConfig.COMPANY_NAME)) {
                // Return a standardized error message when the company name is invalid.
                return Response.status(Response.Status.BAD_REQUEST) // Using BAD_REQUEST for invalid inputs.
                        .entity("{\"error\":\"Invalid company name provided: " + companyName + ". Expected: "
                                + BusinessConfig.COMPANY_NAME + ".\"}")
                        .build();
            }

            // Retrieve the department by ID for the given company name.
            Department department = this.dl.getDepartment(companyName, deptId);
            if (department == null) {
                // Return a standardized error message when no department is found for the given
                // ID.
                return Response.status(Response.Status.NOT_FOUND) // Using NOT_FOUND for nonexistent resources.
                        .entity("{\"error\":\"Department ID " + deptId + " does not exist for company " + companyName
                                + ".\"}")
                        .build();
            }

            // Successfully return the department entity if found.
            return Response.status(Response.Status.OK).entity(department).build();
        } catch (Exception e) {
            // Return a standardized error message in case of a database connection failure.
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Database connection failed. Error details: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * Inserts a new department into the system.
     *
     * @param companyName The name of the company.
     * @param deptName    The name of the new department.
     * @param deptNo      The department number.
     * @param location    The location of the new department.
     * @return A JSON response containing the new department's information or an
     *         error message.
     */
    public Response insert(String companyName, String deptName, String deptNo, String location) {
        try {
            // Validate the company name to match business configuration.
            if (!companyName.equals(BusinessConfig.COMPANY_NAME)) {
                // Respond with an error if the company name does not match.
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"Invalid company name provided. Expected: '" + BusinessConfig.COMPANY_NAME
                                + "' but received: '" + companyName + "'.\"}")
                        .build();
            }

            // Check for the uniqueness of the department number.
            Department existingDept = this.dl.getDepartmentNo(companyName, deptNo);
            if (existingDept != null) {
                // Respond with an error if the department number is already in use.
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"Department number '" + deptNo
                                + "' is already in use. Please provide a unique department number.\"}")
                        .build();
            }

            // Instantiate and set properties for the new department.
            Department newDept = new Department();
            newDept.setCompany(companyName);
            newDept.setDeptName(deptName);
            newDept.setDeptNo(deptNo);
            newDept.setLocation(location);

            // Attempt to insert the new department.
            Department insertedDept = this.dl.insertDepartment(newDept);
            if (insertedDept == null) {
                // Respond with an error if the department could not be inserted.
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("{\"error\":\"Insertion of the new department failed. Please try again later or contact support.\"}")
                        .build();
            }

            // Return the successfully inserted department details.
            return Response.status(Response.Status.CREATED).entity(gson.toJson(
                    Collections.singletonMap("success", insertedDept))).build();
        } catch (Exception e) {
            // Respond with an error in case of an unexpected exception.
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"An unexpected error occurred: " + e.getMessage()
                            + ". Please contact support.\"}")
                    .build();
        }
    }

}
